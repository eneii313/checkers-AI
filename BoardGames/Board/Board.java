package BoardGames.Board;

import BoardGames.Piece.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    protected final int ROW = 8;
    protected final int COL = 8;
    protected Tile[][] board;

    private ArrayList<Piece> blackPieces;
    private ArrayList<Piece> redPieces;

    public Board() {
        board = new Tile[ROW][COL];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++)
                board[i][j] = new Tile(i, j, null);
        }
        blackPieces = new ArrayList<>();
        redPieces = new ArrayList<>();
        initCheckers();
    }

    public Board(Board b) {
        board = new Tile[ROW][COL];
        redPieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();

        // remove all pieces from b.board, then re-assign new pieces in each tile
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++)
                board[i][j] = new Tile(b.board[i][j]);
        }

        for (int i = 0; i < b.redPieces.size(); i++) {
            Piece newPiece = new Piece(this, b.redPieces.get(i));
            redPieces.add(newPiece);
            board[newPiece.getRow()][newPiece.getCol()].setPiece(newPiece);
        }

        for (int i = 0; i < b.blackPieces.size(); i++) {
            Piece newPiece = new Piece(this, b.blackPieces.get(i));
            blackPieces.add(newPiece);
            board[newPiece.getRow()][newPiece.getCol()].setPiece(newPiece);
        }
    }

    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    // initialize checkers pieces
    public void initCheckers() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++)
                board[i][j].removePiece();
        }
        blackPieces.clear();
        redPieces.clear();
        // initialize all black pieces
        blackPieces.add(new Piece(this, 7, 0, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 7, 2, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 7, 4, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 7, 6, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 6, 1, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 6, 3, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 6, 5, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 6, 7, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 5, 0, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 5, 2, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 5, 4, true, "images/check_black.png"));
        blackPieces.add(new Piece(this, 5, 6, true, "images/check_black.png"));

        for (int i = 0; i < blackPieces.size(); i++) {
            int row = blackPieces.get(i).getRow();
            int col = blackPieces.get(i).getCol();
            board[row][col].setPiece(blackPieces.get(i));
        }

        // initialize all red pieces
        redPieces.add(new Piece(this, 0, 1, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 0, 3, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 0, 5, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 0, 7, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 1, 0, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 1, 2, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 1, 4, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 1, 6, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 2, 1, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 2, 3, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 2, 5, false, "images/check_red.png"));
        redPieces.add(new Piece(this, 2, 7, false, "images/check_red.png"));

        for (int i = 0; i < redPieces.size(); i++) {
            int row = redPieces.get(i).getRow();
            int col = redPieces.get(i).getCol();
            board[row][col].setPiece(redPieces.get(i));
        }
    }


    /**
     * Features:
     * Index 0 - Number of pawns
     * Index 1 - Number of kings
     * Index 2 - Number of pieces protecting the non-ideal back row positions
     * Index 3 - Number of pieces protecting the ideal back row positions
     * Index 4 - Number of pawns in the center of the board
     * Index 5 - Number of pawns in the center-edge of the board
     * Index 6 - Number of kings in the center of the board
     * Index 7 - Number of kings in the center-edge of the board
     * Index 8 - Number of vulnerable pieces
     * Index 9 - Number of protected pieces
     * @return the utility value of this board state
     */
    public double getPointValue() {
        int[] blackHeuristics = new int[10];
        int[] redHeuristics = new int[10];

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // Check every piece on the board
                if (board[i][j].isOccupied()) {
                    Piece checkedPiece = board[i][j].getPiece();

                    // check if pieces are protecting its king row
                    if (checkedPiece.isBlack() && i == 7) {
                        if (j == 2 || j == 6)
                            blackHeuristics[3]++;
                        else blackHeuristics[2]++;
                    } else if (!checkedPiece.isBlack() && i == 0) {
                        if (j == 1 || j == 5)
                            redHeuristics[3]++;
                        else redHeuristics[2]++;
                    }

                    // check if the piece is a king
                    if (checkedPiece.isKing()) {
                        if (checkedPiece.isBlack())
                            blackHeuristics[1]++;
                        else redHeuristics[1]++;

                        // check if the king piece is in the center
                        if (i >= 2 && i <= 5) {
                            // center-edge
                            if (j == 0 || j == 7)
                                if (checkedPiece.isBlack())
                                    blackHeuristics[7]++;
                                else redHeuristics[7]++;
                                // center-center
                            else if (checkedPiece.isBlack())
                                blackHeuristics[6]++;
                            else redHeuristics[6]++;
                        }
                    }
                    // pawn pieces
                    else {
                        // check if the pawn piece is in the center
                        if (i >= 2 && i <= 5) {
                            // center-edge
                            if (j == 0 || j == 7)
                                if (checkedPiece.isBlack())
                                    blackHeuristics[5]++;
                                else redHeuristics[5]++;
                                // center-center
                            else if (checkedPiece.isBlack())
                                blackHeuristics[4]++;
                            else redHeuristics[4]++;
                        }
                    }

                    // Check whether the black pieces can be eaten
                    if (checkedPiece.isBlack() && (i > 0 && i < 7)) {
                        // check non-edge pieces
                        if (j > 0 && j < 7) {
                            // upper left
                            if (board[i - 1][j - 1].isOccupied() && !board[i - 1][j - 1].getPiece().isBlack()
                                    && !board[i + 1][j + 1].isOccupied())
                                blackHeuristics[8]++;
                            // upper right
                            else if (board[i - 1][j + 1].isOccupied() && !board[i - 1][j + 1].getPiece().isBlack()
                                    && !board[i + 1][j - 1].isOccupied())
                                blackHeuristics[8]++;
                            //lower left has king
                            else if (board[i + 1][j - 1].isOccupied() && (!board[i + 1][j - 1].getPiece().isBlack() &&
                                    board[i + 1][j - 1].getPiece().isKing()) && !board[i - 1][j + 1].isOccupied())
                                blackHeuristics[8]++;
                            // lower right has king
                            else if (board[i + 1][j + 1].isOccupied() && (!board[i + 1][j + 1].getPiece().isBlack() &&
                                    board[i + 1][j + 1].getPiece().isKing()) && !board[i - 1][j - 1].isOccupied())
                                blackHeuristics[8]++;
                        }
                    }

                    // Check whether the red pieces can be eaten
                    else if (!checkedPiece.isBlack() && (i > 0 && i < 7)) {
                        // check non-edge pieces
                        if (j > 0 && j < 7) {
                            // lower left
                            if (board[i + 1][j - 1].isOccupied() && !board[i + 1][j - 1].getPiece().isBlack()
                                    && !board[i - 1][j + 1].isOccupied())
                                redHeuristics[8]++;
                            // lower right
                            else if (board[i + 1][j + 1].isOccupied() && !board[i + 1][j + 1].getPiece().isBlack()
                                    && !board[i - 1][j - 1].isOccupied())
                                redHeuristics[8]++;
                            //upper left has king
                            else if (board[i - 1][j - 1].isOccupied() && (!board[i - 1][j - 1].getPiece().isBlack() &&
                                    board[i - 1][j - 1].getPiece().isKing()) && !board[i + 1][j + 1].isOccupied())
                                redHeuristics[8]++;
                            // upper right has king
                            else if (board[i - 1][j + 1].isOccupied() && (!board[i - 1][j + 1].getPiece().isBlack() &&
                                    board[i - 1][j + 1].getPiece().isKing()) && !board[i + 1][j - 1].isOccupied())
                                redHeuristics[8]++;
                        }
                    }
                }
            }
        }
        // compute number of pawns
        blackHeuristics[0] = blackPieces.size() - blackHeuristics[1];
        redHeuristics[0] = redPieces.size() - redHeuristics[1];

        // compute number of protected pieces
        blackHeuristics[9] = blackPieces.size() - blackHeuristics[8];
        redHeuristics[9] = redPieces.size() - redHeuristics[8];

        double[] weights = {1, 2, 0.7, 0.75, 0.4, 0.2, 0.8, 0.4, -0.5, 0.5};
        double pointValue = 0;
        for (int i = 0; i < 10; i++)
            pointValue += (blackHeuristics[i] - redHeuristics[i]) * weights[i];

        return pointValue;
    }

    public ArrayList<Piece> getAllPieces() {
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(blackPieces);
        allPieces.addAll(redPieces);
        return allPieces;
    }

    public Piece getPiece(int row, int col) {
        if (board[row][col].isOccupied())
            return board[row][col].getPiece();
        else return null;
    }

    public void removePiece(Piece piece) {
        if (piece != null) {
            if (piece.isBlack())
                blackPieces.remove(piece);
            else redPieces.remove(piece);
            board[piece.getRow()][piece.getCol()].removePiece(); // removes from the tile
        }
    }

    private ArrayList<CheckersMove> updateJumpMoves(Board state, boolean currentTurn, ArrayList<CheckersMove> jumpMoves) {
        ArrayList<CheckersMove> maxJumpMoves = new ArrayList<CheckersMove>();
        // for every jump move, check if the moved piece can jump further
        for (int i = 0; i < jumpMoves.size(); i++) {
            Board tempBoard = new Board(state);
            int initRow = jumpMoves.get(i).getOldRow();
            int initCol = jumpMoves.get(i).getOldCol();
            Piece tempPiece = tempBoard.getPiece(initRow, initCol);
            boolean turnedKing = tempPiece.moveTo(jumpMoves.get(i));
            ArrayList<int[]> initCapturedPieces = jumpMoves.get(i).getCapturedPieces();
            // get the tempPiece's legal jump moves
            ArrayList<CheckersMove> tempMoves = tempPiece.getLegalPieceMoves(getLegalJumpMoves(tempBoard, currentTurn));
            // if the piece can still jump, call this method again
            // otherwise, add current move as a max jump move
            if (tempMoves.size() != 0 && !turnedKing) {
                for (int j = 0; j < tempMoves.size(); j++)
                    tempMoves.get(j).addToCapture(initCapturedPieces);

                tempMoves = updateJumpMoves(tempBoard, currentTurn, tempMoves);
                for (int j = 0; j < tempMoves.size(); j++) {
                    tempMoves.get(j).changeInit(initRow, initCol);
                    maxJumpMoves.add(tempMoves.get(j));
                }
            } else maxJumpMoves.add(jumpMoves.get(i));
        }
        return maxJumpMoves;
    }

    // get all legal jump moves of current player
    private ArrayList<CheckersMove> getLegalJumpMoves(Board state, boolean currentTurn) {
        Tile[][] stateBoard = state.board;
        ArrayList<CheckersMove> legalJumpMoves = new ArrayList<CheckersMove>();
        // Check every square on the board
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // If the current checked square has a piece of the current player
                if (stateBoard[i][j].isOccupied() && currentTurn == stateBoard[i][j].getPiece().isBlack()) {
                    // check if the piece can jump
                    if (stateBoard[i][j].getPiece().canJump(i - 2, j - 2, i - 1, j - 1, currentTurn))
                        legalJumpMoves.add(new CheckersMove(i, j, i - 2, j - 2, true)); // 1
                    if (stateBoard[i][j].getPiece().canJump(i - 2, j + 2, i - 1, j + 1, currentTurn))
                        legalJumpMoves.add(new CheckersMove(i, j, i - 2, j + 2, true)); // 2
                    if (stateBoard[i][j].getPiece().canJump(i + 2, j - 2, i + 1, j - 1, currentTurn))
                        legalJumpMoves.add(new CheckersMove(i, j, i + 2, j - 2, true)); // 3
                    if (stateBoard[i][j].getPiece().canJump(i + 2, j + 2, i + 1, j + 1, currentTurn))
                        legalJumpMoves.add(new CheckersMove(i, j, i + 2, j + 2, true)); // 4
                }
            }
        }

        // get the coords of each captured piece
        for (int i = 0; i < legalJumpMoves.size(); i++) {
            CheckersMove currMove = legalJumpMoves.get(i);
            int jumpRow = (currMove.getOldRow() + currMove.getNewRow()) / 2;
            int jumpCol = (currMove.getOldCol() + currMove.getNewCol()) / 2;
            currMove.addToCapture(stateBoard[jumpRow][jumpCol].getPiece());
        }

        return legalJumpMoves;
    }

    // Get all legal moves of the current player
    public ArrayList<CheckersMove> getLegalMoves(Board state, boolean currentTurn) {
        Tile[][] stateBoard = state.board;
        ArrayList<CheckersMove> legalMoves = getLegalJumpMoves(state, currentTurn);

        // for every jump, check if there are any consecutive jumps
        // if there are, update all jump moves to max jump moves
        if (legalMoves.size() != 0)
            legalMoves = updateJumpMoves(state, currentTurn, legalMoves);

        // if there are no jumps (i.e. arrayList is empty), check if there are available moves
        else {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (stateBoard[i][j].isOccupied() && currentTurn == stateBoard[i][j].getPiece().isBlack()) {
                        if (stateBoard[i][j].getPiece().canMove(i - 1, j - 1, currentTurn))
                            legalMoves.add(new CheckersMove(i, j, i - 1, j - 1, false)); // 1
                        if (stateBoard[i][j].getPiece().canMove(i - 1, j + 1, currentTurn))
                            legalMoves.add(new CheckersMove(i, j, i - 1, j + 1, false)); // 2
                        if (stateBoard[i][j].getPiece().canMove(i + 1, j - 1, currentTurn))
                            legalMoves.add(new CheckersMove(i, j, i + 1, j - 1, false)); // 3
                        if (stateBoard[i][j].getPiece().canMove(i + 1, j + 1, currentTurn))
                            legalMoves.add(new CheckersMove(i, j, i + 1, j + 1, false)); // 4
                    }
                }
            }
        }
//        sortMoves(legalMoves);
        return legalMoves;
    }

    public void sortMoves(ArrayList<CheckersMove> legalMoves){
        for (int i = 0; i < legalMoves.size() - 1; i++) {
            Board temp = new Board(this);
            temp.getPiece(legalMoves.get(i).getOldRow(), legalMoves.get(i).getOldCol()).moveTo(legalMoves.get(i));
            int lowIndex = i;
            for (int j = i + 1; j < legalMoves.size(); j++) {
                Board jBoard = new Board(this);
                jBoard.getPiece(legalMoves.get(j).getOldRow(), legalMoves.get(j).getOldCol()).moveTo(legalMoves.get(j));
                if (jBoard.getPointValue() < temp.getPointValue()) {
                    temp = jBoard;
                    lowIndex = j;
                }
            }
            CheckersMove tempMove = legalMoves.get(i);
            legalMoves.set(i, legalMoves.get(lowIndex));
            legalMoves.set(lowIndex, tempMove);
        }
    }

//    public void sortMoves (ArrayList<CheckersMove> legalMoves){
//        for (int i = 0; i < legalMoves.size() - 1; i++){
//            int best_index = i;
//            for (int j = i+1; j < legalMoves.size(); j++){
//                if (legalMoves.get(j).getCapturedPieces().size()
//                        > legalMoves.get(best_index).getCapturedPieces().size())
//                    best_index = j;
//            }
//            CheckersMove temp = legalMoves.get(i);
//            legalMoves.set(i, legalMoves.get(best_index));
//            legalMoves.set(best_index, temp);
//
//        }
//    }

    // check if the opponent has any possible moves
    public boolean checkWin(boolean currentTurn) {
        return getLegalMoves(this, !currentTurn).size() == 0;
    }

    public boolean hasWinner() {
        return checkWin(false) || checkWin(true);
    }

    public void displayBoard() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                char p = ' ';
                if (board[i][j].isOccupied()) {
                    if (board[i][j].getPiece().isBlack())
                        p = 'b';
                    else p = 'r';
                    if (board[i][j].getPiece().isKing())
                        Character.toUpperCase(p);
                }
                System.out.print("[" + p + "] ");
            }
            System.out.println();
        }
        System.out.println();
    }
}