package BoardGames;

import BoardGames.Board.*;

public class Main {
    public static void main (String[] args) {
        Board b = new Board();
        MainGUI g = new MainGUI();
        Controller c = new Controller(g, b);
    }
}
