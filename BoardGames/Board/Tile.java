package BoardGames.Board;

import BoardGames.Piece.*;

public class Tile {
    private final int ROW;
    private final int COL;
    private Piece occupant;

    public Tile(int row, int col, Piece piece) {
        ROW = row;
        COL = col;
        occupant = piece;
    }

    public Tile (Tile t){
        ROW = t.ROW;
        COL = t.COL;
        occupant = null;
    }

    public boolean isOccupied() {
        return occupant!=null;
    }

    public Piece getPiece() {
        return occupant;
    }

    public void setPiece(Piece piece){
        occupant = piece;
    }

    public void removePiece() {
       occupant = null;
    }

    public boolean isDark () {
        return (ROW%2 == 0 && COL%2 != 0) || (ROW%2 != 0 && COL%2 == 0);
    }

}
