package BoardGames;

import BoardGames.Board.*;
import BoardGames.Piece.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Controller implements MouseListener, ActionListener {

    private MainGUI mainGUI;
    private BoardGUI checkersPanel;
    private JLabel[][] tiles;

    private Board checkersBoard;
    private Agent AI;

    private boolean currentPlayer; // true = black AI, false = red human
    private Piece selectedPiece;
    private CheckersMove selectedMove;
    private ArrayList<CheckersMove> legalMoves; // list of all possible moves for the current player

    final ScheduledExecutorService execute = Executors.newSingleThreadScheduledExecutor();

    public Controller(MainGUI mg, Board b) {
        mainGUI = mg;
        this.checkersBoard = b;
        this.checkersPanel = mainGUI.makeCheckersPanel(b);
        mainGUI.setActionListeners(this);
        tiles = checkersPanel.getTiles();
    }

    private void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void endGame(boolean winner) {
        updateBoard();
        checkersPanel.removeMouseListeners(this);
        execute.schedule(new Runnable() {
            @Override
            public void run() {
                mainGUI.endGame(winner);
            }
        }, 1, TimeUnit.SECONDS);
    }

    private static int round = 1;
    private void aiMove() {
        currentPlayer = true;
        selectedMove = AI.miniMax(checkersBoard);
        if (selectedMove == null)
            endGame(!currentPlayer);
        else {
            selectedPiece = checkersBoard.getPiece(selectedMove.getOldRow(), selectedMove.getOldCol());
            selectedPiece.moveTo(selectedMove);
            if (checkersBoard.checkWin(currentPlayer))
                endGame(currentPlayer);
            else updateToHumanPlayer();
        }
    }

    // prepare the GUI to allow Player turn
    public void updateToHumanPlayer() {
        System.out.println("Human is thinking...");
        currentPlayer = false;
        legalMoves = checkersBoard.getLegalMoves(checkersBoard, currentPlayer);
        selectedPiece = null;
//        System.out.println("Number of moves for MIN: " + legalMoves.size());
        AI.miniMaxPlayer(checkersBoard);
    }

    private void updateBoard() {
        checkersPanel.drawPieces(); //re-calls all the active pieces
        checkersPanel.repaint(); // re-displays the pieces' positions
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start Game") || e.getActionCommand().equals("Play Again")){
            AI = new Agent();
            checkersBoard.initCheckers();
            checkersPanel.reset();
            mainGUI.setMouseListeners(this);
            mainGUI.showGame();
            legalMoves = null;
            System.out.println("AI is thinking...");
            execute.schedule(new Runnable() {
                @Override
                public void run() {
                    aiMove();
                    updateBoard();
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void mouseClicked (MouseEvent e){}

    @Override
    public void mousePressed (MouseEvent e){
        if (!currentPlayer && legalMoves!= null) { // human player
            // get clicked tile
            Object src = e.getSource();
            int clickedRow = -1;
            int clickedCol = -1;
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    if (src.equals(tiles[i][j])) {
                        clickedRow = i;
                        clickedCol = j;
                        break;
                    }
                }
            }

            Piece clickedPiece = checkersBoard.getPiece(clickedRow, clickedCol);

            // If the clicked space is occupied, and the player has not yet selected a piece
            if (clickedPiece != null && selectedPiece == null) {
                //if clicked piece is not owned by current player
                if (currentPlayer != clickedPiece.isBlack())
                    selectedPiece = null;

                    // else assign the clickedPiece as the selectedPiece
                else {
                    selectedPiece = clickedPiece;
                    checkersPanel.setSelected(clickedRow, clickedCol, selectedPiece.getLegalPieceMoves(legalMoves));
                }
            }

            // if the clicked space is currently the selectedPiece and is not forced to take any
            // jumps, remove selectedPiece
            else if (selectedPiece != null && selectedPiece.equals(clickedPiece)) {
                selectedPiece = null;
                checkersPanel.removeSelected();
            }

            //player attempts to move selectedPiece
            else if (selectedPiece != null && selectedPiece.isLegalMove(clickedRow, clickedCol) != null) {
                // If the player chose a legal move, move the selectedPiece
                selectedMove = selectedPiece.isLegalMove(clickedRow, clickedCol);
                selectedPiece.moveTo(selectedMove);
                checkersPanel.removeSelected();
                // Check if player won
                if (checkersBoard.checkWin(currentPlayer))
                    endGame(currentPlayer);
                // Else let AI take turn
                else currentPlayer = true;
            }
        }
        updateBoard();
    }

    @Override
    public void mouseReleased (MouseEvent e){
        if (currentPlayer) { // AI player
            round++;
            System.out.println("\nROUND "+ round);
            System.out.println("AI is thinking...");
            delay();
            aiMove();
            updateBoard();
        }
    }

    @Override
    public void mouseEntered (MouseEvent e){}

    @Override
    public void mouseExited (MouseEvent e){}
}
