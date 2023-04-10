package BoardGames;

import BoardGames.Board.*;
import BoardGames.Piece.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Agent {
    private final int MAX_DEPTH;
    private int nodesExplored = 0;
    public Agent() {
        MAX_DEPTH = 10;
    } // MAX: 10


    public void miniMaxPlayer(Board currentState) {
        ArrayList<CheckersMove> bestMoves = new ArrayList<>();
        double bestScore = Double.POSITIVE_INFINITY;

        // legalMoves will generate successor states
        ArrayList<CheckersMove> legalMoves = currentState.getLegalMoves(currentState, false);
        // if there's no more moves for the AI, return null
        if (legalMoves.size() == 0)
            System.out.println("No more moves for Player");
        // if there's only one possible move left, return that move
        else if (legalMoves.size() == 1) {
            System.out.println("Only one move for Player:");
            legalMoves.get(0).printMove();
        }
        // otherwise, get the best move by doing miniMax
        else {
            for (int i = 0; i < legalMoves.size(); i++) {
                CheckersMove checkedMove = legalMoves.get(i);
                Board tempBoard = new Board(currentState);
                tempBoard.getPiece(checkedMove.getOldRow(), checkedMove.getOldCol()).moveTo(checkedMove);
                double score = getMaxValue(tempBoard, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                if (score < bestScore) {
                    bestScore = score;
                    bestMoves.clear();
                    bestMoves.add(legalMoves.get(i));
                } else if (score == bestScore)
                    bestMoves.add(legalMoves.get(i));
            }
            System.out.println("COMPUTED MIN VALUE: " + bestScore);
            // if there are more than one possible move with the best score, get a random move
            System.out.println("Suggested move for Player:");
            Random r = new Random();
            bestMoves.get(r.nextInt(bestMoves.size())).printMove();
        }
    }

//    private ArrayList<Board> sortLegalMovesMax (Board currentState, ArrayList<CheckersMove> legalMoves) {
//        ArrayList<Board> sortedStates = new ArrayList<>();
//        int lowestIndex;
//
//        //generate successor states
//        for (int i = 0; i < legalMoves.size(); i++){
//            CheckersMove move = legalMoves.get(i);
//            Board temp = new Board(currentState);
//            temp.getPiece(move.getOldRow(), move.getOldCol()).moveTo(move);
//            sortedStates.add(temp);
//        }
//
//        // sort the successor states in ascending order
//        for (int i = 0; i < sortedStates.size() - 1; i++){
//            lowestIndex = i;
//            for (int j = i+1; j < sortedStates.size(); j++){
//                if (sortedStates.get(j).getPointValue() < sortedStates.get(lowestIndex).getPointValue()){
//                    lowestIndex = j;
//                }
//            }
//            Board temp = sortedStates.get(i);
//            sortedStates.set(i, sortedStates.get(lowestIndex));
//            sortedStates.set(lowestIndex, temp);
//        }
////        System.out.println("Sorted MAX Move Values: ");
////        for (int i = 0; i < sortedStates.size(); i++){
////            System.out.print(" " + sortedStates.get(i).getPointValue());
////        }
////        System.out.println();
//        return sortedStates;
//    }
//
//    private ArrayList<Board> sortLegalMovesMin (Board currentState, ArrayList<CheckersMove> legalMoves) {
//        ArrayList<Board> sortedStates = new ArrayList<>();
//        int highestIndex;
//
//        //generate successor states
//        for (int i = 0; i < legalMoves.size(); i++){
//            CheckersMove move = legalMoves.get(i);
//            Board temp = new Board(currentState);
//            temp.getPiece(move.getOldRow(), move.getOldCol()).moveTo(move);
//            sortedStates.add(temp);
//        }
//
//        // sort the successor states in descending order
//        for (int i = 0; i < sortedStates.size() - 1; i++){
//            highestIndex = i;
//            for (int j = i+1; j < sortedStates.size(); j++){
//                if (sortedStates.get(j).getPointValue() > sortedStates.get(highestIndex).getPointValue()){
//                    highestIndex = j;
//                }
//            }
//            Board temp = sortedStates.get(i);
//            sortedStates.set(i, sortedStates.get(highestIndex));
//            sortedStates.set(highestIndex, temp);
//        }
////        System.out.println("Sorted MIN Move Values: ");
////        for (int i = 0; i < sortedStates.size(); i++){
////            System.out.print(" " + sortedStates.get(i).getPointValue());
////        }
////        System.out.println();
//        return sortedStates;
//    }

    private ArrayList<Integer> branchingFactor = new ArrayList<>();

    /**
     * Returns the best possible move for the AI, under the assumption that the opponent plays to minimize utility
     * Assumption: Agent is always first (Max/true)
     * @param currentState - initial state of the board
     * @return - best possible move
     */
    public CheckersMove miniMax(Board currentState) {
        ArrayList<CheckersMove> bestMoves = new ArrayList<>();
        double bestScore = Double.NEGATIVE_INFINITY;

        nodesExplored = 0;
        branchingFactor.clear();
        long start = System.nanoTime();

        // legalMoves will generate successor states
        ArrayList<CheckersMove> legalMoves = currentState.getLegalMoves(currentState, true);
        branchingFactor.add(legalMoves.size());

        // if there's no more moves for the AI, return null
        if (legalMoves.size() == 0)
            return null;
        // if there's only one possible move left, return that move
        else if (legalMoves.size() == 1)
            return legalMoves.get(0);
        // otherwise, get the best move by doing miniMax
        else {
            for (int i = 0; i < legalMoves.size(); i++) {
                nodesExplored++;
                CheckersMove move = legalMoves.get(i);
                Board temp = new Board(currentState);
                temp.getPiece(move.getOldRow(), move.getOldCol()).moveTo(move);
                double score = getMinValue(temp, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                if (score > bestScore) {
                    bestScore = score;
                    bestMoves.clear();
                    bestMoves.add(legalMoves.get(i));
                } else if (score == bestScore)
                    bestMoves.add(legalMoves.get(i));
            }
        }
        long end = System.nanoTime();
        double b = 0;

        for (int i = 0; i < branchingFactor.size(); i++)
            b+= branchingFactor.get(i);

        System.out.println("COMPUTED MAX VALUE: " + bestScore);
        System.out.println("Computed time in seconds: " + (end - start)*0.000000001);
        System.out.println("Number of nodes: " + b);
        System.out.println("Number of nodes explored: " + nodesExplored);
        b /= branchingFactor.size();
        System.out.println("Average branching factor: " + b);
        // if there are more than one possible move with the best score, get a random move
        Random r = new Random();
        return bestMoves.get(r.nextInt(bestMoves.size()));
    }

    private double getMaxValue(Board currentState, int depth, double alpha, double beta) {
        // check if the algorithm reached the max depth or has a winner
        if (depth == MAX_DEPTH || currentState.hasWinner())
            return currentState.getPointValue();

        double maxEval = Double.NEGATIVE_INFINITY;
        ArrayList<CheckersMove> legalMoves = currentState.getLegalMoves(currentState, true);
        branchingFactor.add(legalMoves.size());
            // for each legal move, simulate that move and get its point value
            for (int i = 0; i < legalMoves.size(); i++) {
                nodesExplored++;
                CheckersMove move = legalMoves.get(i);
                Board temp = new Board(currentState);
                temp.getPiece(move.getOldRow(), move.getOldCol()).moveTo(move);
                maxEval = Math.max(maxEval, getMinValue(temp, depth + 1, alpha, beta));
                // pruning
                if (maxEval >= beta)
                    return maxEval;
                alpha = Math.max(maxEval, alpha);
            }

        return maxEval;
    }


    private double getMinValue(Board currentState, int depth, double alpha, double beta) {
        // check if the algorithm reached the max depth or has a winner
        if (depth == MAX_DEPTH || currentState.hasWinner())
            return currentState.getPointValue();

        double minEval = Double.POSITIVE_INFINITY;
        ArrayList<CheckersMove> legalMoves = currentState.getLegalMoves(currentState, false);
        branchingFactor.add(legalMoves.size());

            // for each legal move, simulate that move and get its point value
            for (int i = 0; i < legalMoves.size(); i++) {
                CheckersMove move = legalMoves.get(i);
                Board temp = new Board(currentState);
                temp.getPiece(move.getOldRow(), move.getOldCol()).moveTo(move);
                nodesExplored++;
                minEval = Math.min(minEval, getMaxValue(temp, depth+1, alpha, beta));
                // pruning
                if (minEval <= alpha)
                    return minEval;
                beta = Math.min(minEval, beta);
            }
        return minEval;
    }
}

// pg 166
// pg 170
