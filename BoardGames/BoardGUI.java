package BoardGames;

import BoardGames.Board.*;
import BoardGames.Piece.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class BoardGUI extends JPanel {
    protected final int ROW = 8;
    protected final int COL = 8;

    private Board checkersBoard;

    protected JLabel[][] tiles;
    protected ArrayList<Piece> allPieces; // list of all active pieces

    protected int[] selectedPiece; // current active piece's position
    protected final String selected_tile_path = "images/highlight.png";
    protected Image selected_icon;

    protected ArrayList<int[]> legalPieceMoves; //all legal moves of the selected piece
    protected final String legal_move_path = "images/legalmove.png";
    protected Image legal_move_icon;
    
    public BoardGUI(Board b) {
        super();
        setLayout(new GridLayout(ROW, COL));
        tiles = new JLabel[ROW][COL];
        allPieces = new ArrayList<Piece>();
        selectedPiece = new int[] {-1,-1}; //-1 -1 indicates that there is no selected piece
        legalPieceMoves = new ArrayList<int[]>();
        selected_icon = loadImage(selected_tile_path);
        legal_move_icon = loadImage(legal_move_path);
        checkersBoard = b;
        createBoard();
    }

    public void setMouseListeners (MouseListener listener){
        for (int i = 0; i<ROW; i++){
            for (int j = 0; j<COL; j++){
                tiles[i][j].addMouseListener(listener);
            }
        }
    }

    public void removeMouseListeners(MouseListener listener) {
        for (int i = 0; i<ROW; i++){
            for (int j = 0; j<COL; j++){
                tiles[i][j].removeMouseListener(listener);
            }
        }

    }

    public JLabel[][] getTiles () {
        return tiles;
    }

    // changes the size of the square icons to fit into the tiles
    private void changeSize(){
        if (selected_icon != null)
            selected_icon = selected_icon.getScaledInstance (tiles[0][0].getWidth(),
                    tiles[0][0].getHeight(), Image.SCALE_DEFAULT);

        if (legal_move_icon != null)
            legal_move_icon = legal_move_icon.getScaledInstance (tiles[0][0].getWidth(),
                    tiles[0][0].getHeight(), Image.SCALE_DEFAULT);
    }

    // creates squares of the board
    private void createBoard() {
        tiles = new JLabel[ROW][COL];
        for (int i = 0; i<ROW; i++) {
            for (int j = 0; j<COL; j++){
                tiles[i][j] = new JLabel();
                tiles[i][j].setOpaque(true);
                if ((i%2 == 0 && j%2 == 0) || (i%2 != 0 && j%2 != 0))
                    tiles[i][j].setBackground(Color.LIGHT_GRAY);
                else tiles[i][j].setBackground(Color.DARK_GRAY);

                this.add(tiles[i][j]);
            }
        }
        drawPieces();
    }

    // sets position of the highlighted square
    public void setSelected(int row, int col, ArrayList<CheckersMove> legalMoves){
        changeSize();
        selectedPiece[0] = row;
        selectedPiece[1] = col;
        setMoves(legalMoves);
    }

    // removes the highlighted square
    public void removeSelected () {
        tiles[selectedPiece[0]][selectedPiece[1]].setIcon(null);
        selectedPiece[0] = -1;
        selectedPiece[1] = -1;
        legalPieceMoves.clear();
    }

    // sets positions of legal piece moves
    private void setMoves(ArrayList<CheckersMove> legalMoves){
        changeSize();
        for (int i = 0; i < legalMoves.size(); i++){
            int[] move = {legalMoves.get(i).getNewRow(), legalMoves.get(i).getNewCol()};
           legalPieceMoves.add(move);
        }
    }

    public void drawPieces () { // calls the list of active pieces from board
        allPieces = checkersBoard.getAllPieces();
    }

    public void reset() {
        drawPieces();
        if (selectedPiece[0]!=-1 && selectedPiece[1]!=-1)
            removeSelected();
        repaint();
    }

    // checks if the image file is available
    protected Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e){
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // displays all pieces and the current active piece
    @Override
    public void paint (Graphics g){
        super.paint(g);

        for (int i = 0; i< allPieces.size(); i++){
            Piece temp = allPieces.get(i);
            g.drawImage(loadImage(temp.getFilePath()),tiles[temp.getRow()][temp.getCol()].getX(),
                    tiles[temp.getRow()][temp.getCol()].getY(), null);
        }

        // if a piece is selected
        if (selectedPiece[0] != -1 && selectedPiece[1] != -1) {
            g.drawImage(selected_icon, tiles[selectedPiece[0]][selectedPiece[1]].getX(),
                    tiles[selectedPiece[0]][selectedPiece[1]].getY(), null);
            for (int j = 0; j<legalPieceMoves.size(); j++){
                g.drawImage(legal_move_icon, tiles[legalPieceMoves.get(j)[0]][legalPieceMoves.get(j)[1]].getX(),
                        tiles[legalPieceMoves.get(j)[0]][legalPieceMoves.get(j)[1]].getY(), null);
            }
        }
    }


}
