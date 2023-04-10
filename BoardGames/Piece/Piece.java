package BoardGames.Piece;
import BoardGames.Board.*;

import java.util.ArrayList;

public class Piece {
    protected int row;
    protected int col;
    private boolean isKing;
    protected final boolean IS_BLACK;
    protected String file_path;
    public Board board;
    protected ArrayList<CheckersMove> legalPieceMoves;

    public Piece(Board b, int r, int c, boolean ib, String fp) {
        board = b;
        row = r;
        col = c;
        isKing = false;
        IS_BLACK = ib;
        file_path = fp;
        legalPieceMoves = new ArrayList<CheckersMove>();
    }

    public Piece (Board b, Piece p) {
        this(b, p.row, p.col, p.IS_BLACK, p.file_path);
        isKing = p.isKing;
    }

    public int getRow () {
        return row;
    }

    public int getCol () {
        return col;
    }

    public boolean isBlack() {
        return IS_BLACK;
    }

    public boolean isKing() {
        return isKing;
    }

    public String getFilePath () { return file_path; }

    // move this piece, then remove every piece it captured
    // return true if a non-king piece has reached last row, otherwise return false
    public boolean moveTo (CheckersMove move){
        board.getTile(row, col).removePiece();
        row = move.getNewRow();
        col = move.getNewCol();
        board.getTile(row, col).setPiece(this);
        boolean turnedKing = false;

        // If this is non-king piece reached the last row, make it a king and return false
        if (!isKing && reachedLastRow()){
            this.setKing();
            turnedKing = true;
        }
        // If the move is a jump, remove jumped pieces
        if (move.isJump()){
            ArrayList<int[]> removedPieces = move.getCapturedPieces();
            for (int i = 0; i < removedPieces.size(); i++){
                int capturedRow = removedPieces.get(i)[0];
                int capturedCol = removedPieces.get(i)[1];
                board.removePiece(board.getPiece(capturedRow, capturedCol));
            }
        }
        return turnedKing;
    }

    public boolean reachedLastRow(){
        if (IS_BLACK)
            return row == 0;
        else return row == 7;
    }

    // Make this piece a king
    public void setKing(){
        isKing = true;
        if (IS_BLACK)
            file_path = "images/king_black.png";
        else file_path = "images/king_red.png";
    }

    // Get all Legal Moves that belong to this piece
    public ArrayList<CheckersMove> getLegalPieceMoves (ArrayList<CheckersMove> legalMoves) {
        legalPieceMoves.clear();
        for ( int i = 0; i<legalMoves.size(); i++){
            CheckersMove checkedMove = legalMoves.get(i);
            if (checkedMove.getOldRow() == this.row && checkedMove.getOldCol() == this.col)
                legalPieceMoves.add(legalMoves.get(i));
        }
        return legalPieceMoves;
    }

    // Check if the chosen move is legal
    // returns the chosen move if it is legal, otherwise return null
    public CheckersMove isLegalMove (int newRow, int newCol) {
        for (int i = 0; i< legalPieceMoves.size(); i++){
            if (newRow == legalPieceMoves.get(i).getNewRow() && newCol == legalPieceMoves.get(i).getNewCol())
                return legalPieceMoves.get(i);
        }
        return null;
    }

    // Rule 1: Diagonal in the forward direction (towards the opponent) to the next dark square.
    // Assumption: newRow and newCol is always one diagonal from the piece
    public boolean canMove(int newRow, int newCol, boolean currentTurn){
        // If newRow and/or newCol is out of bounds, return false
        if (newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7)
            return false;

        Tile checkedTile = board.getTile(newRow,newCol);
        // If the tile is white or already occupied, return false
        if (!checkedTile.isDark() || checkedTile.isOccupied())
            return false;
        // If the piece is a regular piece, check if it is moving forward
        else return isMovingForward(newRow, currentTurn);
    }

    // Rule 2: If there is one of the opponent's pieces next to a piece and an empty space on the other side,
    // you jump your opponent and remove their piece. You can do multiple jumps if they are lined up in the
    // forward direction. *** note: if you have a jump, you have no choice but to take it.
    // Assumption: newRow and newCol is always 2 spaces diagonal from the piece
    public boolean canJump (int newRow, int newCol, int jumpedRow, int jumpedCol, boolean currentTurn) {
        // Check the criteria for canMove first
        if (!this.canMove(newRow, newCol, currentTurn))
            return false;
        // If canMove returns true, then check there is an opponent's piece on the tile to
        // be jumped over
        else if (board.getTile(jumpedRow, jumpedCol).isOccupied())
            return board.getTile(jumpedRow, jumpedCol).getPiece().isBlack() != currentTurn;
            // If it is not occupied, the return false
        else return false;
    }

    // Checks if the selected piece is moving towards the opponent
    private boolean isMovingForward(int newRow, boolean currentTurn){
        if (isKing)
            return true;
        // If currentTurn is Black
        if (currentTurn)
            return row>newRow;
            // If currentTurn is Red
        else return row<newRow;
    }

    public boolean equals (Object o) {
        if (o == null)
            return false;
        else {
            Piece op = (Piece) o;
            return op.getRow() == this.row
                    && op.getCol() == this.col
                    && op.isBlack() == this.IS_BLACK;
        }
    }
}
