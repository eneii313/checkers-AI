package BoardGames.Piece;

import java.util.ArrayList;

public class CheckersMove {
    private int oldRow;
    private int oldCol;
    private int newRow;
    private int newCol;
    private boolean isJump;
    private ArrayList<int[]> capturedPieces;

    public CheckersMove(int or, int oc, int nr, int nc, boolean ij){
        oldRow = or;
        oldCol = oc;
        newRow = nr;
        newCol = nc;
        isJump = ij;
        capturedPieces = new ArrayList<>();
    }

    public int getOldRow() {
        return oldRow;
    }

    public int getOldCol() {
        return oldCol;
    }

    public int getNewRow() {
        return newRow;
    }

    public int getNewCol () {
        return newCol;
    }

    public ArrayList<int[]> getCapturedPieces() {
        return capturedPieces;
    }

    public void addToCapture (Piece piece){
        int[] capturedCoord = {piece.getRow(), piece.getCol()};
        capturedPieces.add(capturedCoord);
    }

    public void addToCapture (ArrayList<int[]> pieces){
        capturedPieces.addAll(pieces);
    }

    public boolean isJump() {
        return isJump;
    }

    public void changeInit (int or, int oc) {
        oldRow = or;
        oldCol = oc;
    }

    public void printMove () {
        System.out.println("From [" + oldRow + ", " + oldCol + "] to [" + newRow + ", " + newCol + "]");

        if (capturedPieces.size() != 0) {
            System.out.println("Captured Pieces:");
            for (int i = 0; i < capturedPieces.size(); i++) {
                System.out.println("  [" + capturedPieces.get(i)[0] + ", " + capturedPieces.get(i)[1] + "]");
            }
        }
        System.out.println();
    }
}
