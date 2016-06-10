package com.cse.bgai;

import java.util.ArrayList;

/**
 * Created by Burak on 8.06.2016.
 */
public class AI {

    private Board board;

    public AI(Board b) {
        board = b;
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
}
