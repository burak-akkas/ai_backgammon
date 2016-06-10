package com.cse.bgai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Burak on 8.06.2016.
 */
enum MoveType {
    BEAROFF, CAPTURE, NORMAL, ENTER_HOME;
}

class Move {
    public MoveType moveType;
    public int from;
    public int to;
    public int dice;
    public int score;

    public Move(MoveType type, int f, int t, int d) {
        this.moveType = type;
        this.from = f;
        this.to = t;
        this.dice = d;
        this.score = 0;
    }

    public void setScore(int sc) {
        this.score = sc;
    }

    public int getScore() {
        return this.score;
    }

    public int getFrom() {
        return this.from;
    }

    public int getTo() {
        return this.to;
    }

    public int getDice() {
        return this.dice;
    }
}

public class AI {

    private Board board;

    public static final int BEAROFF_WEIGHT = 125;
    public static final int CAPTURE_WEIGHT = 5;
    public static final int SINGLE_CHECKER_WEIGHT = -8;
    public static final int ENTER_HOME_WEIGHT = 15;

    private ArrayList<Move> possibleMoves;
    // bearoff -> fromIndex, toIndex
    // normal, capture -> from, to, dice

    public AI(Board b) {
        this.board = b;
        possibleMoves = new ArrayList<Move>();
    }

    public void thinkPlay() {
        possibleMoves = generatePossibleMoves();
        calculateScores();
        Move m = getBestScoreMove();

        switch (m.moveType) {
            case BEAROFF:

                break;
            case CAPTURE:

                break;
            case NORMAL:

                break;
            case ENTER_HOME:

                break;
            default:
                break;
        }
    }

    // ONLY FOR TESTING
    public void listPossibleMoves(int color, ArrayList<Integer> dices) {

        int selected = 0;

        // ONE STEP
        System.out.println("+ AI : RESULTS FOR 1-STEP MOVEMENT");
        for(int i = 0; i < dices.size(); i++) {
            selected = dices.get(i);

                for(int j = 23; j >= 0; j--) {

                    if(j - selected > 0) {
                        if(board.getColor(j) == -1) {

                            if(board.canBearoff(-1)) {
                                System.out.println("+ AI : BEAROFF MOVE FOUND FOR BLACK.");
                            }

                            else if(board.logicBoard[j - selected] == 1) {
                                System.out.println("+ AI : CAPTURABLE WHITE PIECE FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            } else if(board.logicBoard[j - selected] == 0) {
                                System.out.println("+ AI : MOVE TO EMPTY FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            } else if(board.getColor(j - selected) == -1) {
                                System.out.println("+ AI : MOVE TO FRIEND FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            } else {
                                // will continue.
                            }
                        }
                    }
                }
            }

        if(dices.size() == 2) {
            // TWO STEP
            System.out.println("+ AI : RESULTS FOR 2-STEP MOVEMENT");

            selected = dices.get(0) + dices.get(1);

            for(int j = 23; j >= 0; j--) {
                if(j - selected > 0) {
                    if(board.getColor(j) == -1) {

                        if(board.canBearoff(-1)) {
                            System.out.println("+ AI : BEAROFF MOVE FOUND FOR BLACK.");
                        }

                        else if(board.logicBoard[j - selected] == 1) {
                            System.out.println("+ AI : CAPTURABLE WHITE PIECE FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                        } else if(board.logicBoard[j - selected] == 0) {
                            System.out.println("+ AI : MOVE TO EMPTY FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                        } else if(board.getColor(j - selected) == -1) {
                            System.out.println("+ AI : MOVE TO FRIEND FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                        } else {
                            // will continue.
                        }
                    }
                }
            }
        }


    }

    public ArrayList<Move> generatePossibleMoves() {
        return new ArrayList<Move>();
    }

    public void calculateScores() {
        // process on possibleMoves array list
    }

    public Move getBestScoreMove() {
        // process on possibleMoves array list
        return new Move(MoveType.NORMAL, 0, 0, 0);
    }
}
