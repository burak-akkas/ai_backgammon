package com.cse.bgai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Burak on 8.06.2016.
 */
enum MoveType {
    BEAROFF, CAPTURE, NORMAL, ENTER_HOME, REENTER, FRIEND, CANNOT;
}

class Move implements Comparable<Move> {
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

    @Override
    public int compareTo(Move o1) {
        return this.score - o1.score;
    }
}

public class AI {

    private Board board;

    public static final int REENTER_WEIGHT = 500;
    public static final int BEAROFF_WEIGHT = 125;
    public static final int CAPTURE_WEIGHT = 24;
    //public static final int SINGLE_CHECKER_WEIGHT = -8;
    public static final int ENTER_HOME_WEIGHT = 15;
    public static final int FRIEND_WEIGHT = 14;
    public static final int NORMAL_WEIGHT = 16;

    private ArrayList<Move> possibleMoves;
    // bearoff -> fromIndex, toIndex
    // normal, capture -> from, to, dice

    public AI(Board b) {
        this.board = b;
        possibleMoves = new ArrayList<Move>();
    }

    // returns played dice, if cannot move returns -1
    public int thinkPlay(ArrayList<Integer> dices) {
        generatePossibleMoves(dices);
        calculateScores();
        Move m = getBestScoreMove();

        System.out.println("+ AI : SELECTED MOVE = (FROM " + m.from + ", TO " + m.to + ", TYPE: " + m.moveType + ")");

        // BEAROFF, CAPTURE, NORMAL, ENTER_HOME, REENTER, FRIEND, CANNOT;
        switch (m.moveType) {
            case BEAROFF:
                board.bearoff(findCheckerForBearOff(), m.to);
                break;
            case CAPTURE:
            case NORMAL:
            case ENTER_HOME:
            case FRIEND:
                board.move(m.from, m.to, m.dice);
                break;
            case REENTER:
                board.reenter(m.from, m.to, m.dice);
                break;
            case CANNOT:
                return -1;
            default:
                return -1;
        }

        return m.dice;
    }

    public void generatePossibleMoves(ArrayList<Integer> dices) {

        int selected = 0;

        // ONE STEP
        System.out.println("+ AI : RESULTS FOR 1-STEP MOVEMENT");
        for(int i = 0; i < dices.size(); i++) {
            selected = dices.get(i);

            for (int j = 23; j >= 0; j--) {

                // changed (j - selected > 0)
                if (j - selected >= 0) {

                    if (board.getColor(j) == -1) {

                        if (board.mustReEnter(-1)) {

                            int dice = 0;
                            dice = board.canReEnterAI(dices);

                            if (dice != -1) {
                                System.out.println("+ AI : MUST REENTER AND CAN REENTER TO " + (24 - dice));
                                possibleMoves.add(new Move(MoveType.REENTER, 25, (24 - dice), dice));
                            } else {
                                System.out.println("+ AI : CANNOT REENTER. (SWITCH TO WHITE PLAYER)");
                                possibleMoves.add(new Move(MoveType.CANNOT, 0, 0, 0));
                            }
                        } else if (board.canBearoff(-1)) {
                            System.out.println("+ AI : BEAROFF MOVE FOUND FOR BLACK.");
                            // changed: possibleMoves.add(new Move(MoveType.BEAROFF, selected, 27, selected));
                            possibleMoves.add(new Move(MoveType.BEAROFF, 0, 27, selected));
                        } else if ((board.logicBoard[j - selected] == 1 || board.logicBoard[j - selected] == 0 || board.getColor(j - selected) == -1)
                                && (j - selected) < 6 && (j > 6)) {
                            System.out.println("+ AI : ENTER HOME MOVE FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            possibleMoves.add(new Move(MoveType.ENTER_HOME, j, (j - selected), selected));
                        } else if (board.logicBoard[j - selected] == 1) {
                            System.out.println("+ AI : CAPTURABLE WHITE PIECE FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            possibleMoves.add(new Move(MoveType.CAPTURE, j, (j - selected), selected));
                        } else if (board.logicBoard[j - selected] == 0) {
                            System.out.println("+ AI : MOVE TO EMPTY FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            possibleMoves.add(new Move(MoveType.NORMAL, j, (j - selected), selected));
                        } else if (board.getColor(j - selected) == -1) {
                            System.out.println("+ AI : MOVE TO FRIEND FOUND = (BLACK, " + j + " -> " + (j - selected) + ")");
                            possibleMoves.add(new Move(MoveType.FRIEND, j, (j - selected), selected));
                        } else {
                            System.out.println("+ AI : CANNOT MOVE.");
                            possibleMoves.add(new Move(MoveType.CANNOT, 0, 0, 0));
                        }
                    }
                } else if (j - selected <= 0 && board.canBearoff(-1)) {
                    {
                        System.out.println("+ AI : BEAROFF MOVE FOUND FOR BLACK.");
                        // changed: possibleMoves.add(new Move(MoveType.BEAROFF, selected, 27, selected));
                        possibleMoves.add(new Move(MoveType.BEAROFF, 0, 27, selected));
                    }
                }
            }
        }
    }

    public void calculateScores() {
        // process on possibleMoves array list
        Move selected;

        for(int i = 0; i < possibleMoves.size(); i++) {
            selected = possibleMoves.get(i);

            switch (selected.moveType) {
                case BEAROFF:
                    selected.setScore(BEAROFF_WEIGHT);
                    break;
                case CAPTURE:
                    if(selected.to < 6) {
                        selected.setScore(CAPTURE_WEIGHT - 10);
                    } else {
                        selected.setScore(CAPTURE_WEIGHT);
                    }
                    break;
                case NORMAL:
                    if(board.logicBoard[selected.to] < -3) {
                        selected.setScore(NORMAL_WEIGHT - 5);
                    } else if(selected.from > 18) {
                        selected.setScore(NORMAL_WEIGHT + 5);
                    } else if(selected.from < 6) {
                        selected.setScore(NORMAL_WEIGHT - 3);
                    } else {
                        selected.setScore(NORMAL_WEIGHT);
                    }
                    break;
                case FRIEND:
                    if(board.getLogicBoard()[selected.to] == -1) {
                        selected.setScore(FRIEND_WEIGHT + 10);
                    } else if(selected.from > 5 && board.logicBoard[selected.to] > -3) {
                        selected.setScore(FRIEND_WEIGHT + 5);
                    } else {
                        selected.setScore(FRIEND_WEIGHT);
                    }
                    break;
                case REENTER:
                    selected.setScore(REENTER_WEIGHT);
                    break;
                case ENTER_HOME:
                    if(selected.from >= 6 && board.logicBoard[selected.to] > -3) {
                        selected.setScore(ENTER_HOME_WEIGHT + 5);
                    } else if(selected.from > 7 && board.logicBoard[selected.to] < -2) {
                        selected.setScore(ENTER_HOME_WEIGHT - 4);
                    } else if(selected.from <= 5 && board.logicBoard[selected.to] == 0) {
                        selected.setScore(ENTER_HOME_WEIGHT - 8);
                    } else {
                        selected.setScore(ENTER_HOME_WEIGHT);
                    }
                    break;
                case CANNOT:
                    selected.setScore(0);
                    break;
                default:
                    System.out.println("+ AI : ERROR. CANNOT CALCULATE SCORES!");
                    break;
            }
        }
    }

    public Move getBestScoreMove() {

        Collections.sort(possibleMoves);

        return possibleMoves.get(possibleMoves.size() - 1);
    }

    public void dispose() {
        possibleMoves = null;
        board = null;
    }

    public int findCheckerForBearOff() {
        for(int i = 0; i < 6; i++) {
            if(board.getColor(i) == -1) {
                return i;
            }
        }

        return -1;
    }
}
