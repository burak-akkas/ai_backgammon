package com.cse.bgai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by Burak on 6.06.2016.
 */
public class Board {

    // logic
    private int logicBoard[];

    public int capturedWhitePieces = 0;
    public int capturedBlackPieces = 0;

    private int bearoffWhitePieces = 0;
    private int bearoffBlackPieces = 0;

    public ArrayList possibleMoves;

    // graphic
    private Sprite graphicBoard[][];

    // rendering
    private final String path = "core/assets/";

    private Texture boardTexture;
    private Sprite boardSprite;

    private Texture whiteCheckerTexture;
    private Texture blackCheckerTexture;
    private Texture emptyCheckerTexture;
    private Sprite emptyCheckerSprite;

    // constructor
    public Board() {
        logicBoard = new int[24];
        graphicBoard = new Sprite[24][15];

        possibleMoves = new ArrayList<Integer>();

        init();
        initGraphics();
        placeCheckers();
    }

    // tree constructor
    public Board(int board[]) {
        logicBoard = board;
    }

    // initialize
    public void init() {
        for(int i = 0; i < logicBoard.length; i++) { logicBoard[i] = 0; }

        // white = 1
        // black = -1
        // empty = 0
        logicBoard[0] = 2; // 2 white pieces

        logicBoard[5] = -5; // 5 black pieces
        logicBoard[7] = -3;

        logicBoard[11] = 5;

        logicBoard[12] = -5;

        logicBoard[16] = 3;
        logicBoard[18] = 5;

        logicBoard[23] = -2;
    }

    // init graphics
    public void initGraphics() {
        // board
        boardTexture = new Texture(Gdx.files.local(path + "board.png"));
        boardSprite = new Sprite(boardTexture, 0, 0, 775, 622);

        // checkers
        whiteCheckerTexture = new Texture(Gdx.files.local(path + "white.png"));
        blackCheckerTexture = new Texture(Gdx.files.local(path + "black.png"));
        emptyCheckerTexture = new Texture(Gdx.files.local(path + "empty.png"));
        emptyCheckerSprite = new Sprite(emptyCheckerTexture, 0, 0, 200, 200);

        emptyCheckerSprite.setSize(50, 50);

    }

    // draw checkers according to logic board
    // TODO: use after every move
    public void placeCheckers() {
        int coordX = 0, coordY = 0;

        for(int i = 0; i < 24; i++) {
            for(int k = 5; k < 15; k++) {
                Sprite empty = new Sprite(emptyCheckerTexture, 0, 0, 200, 200);
                empty.setBounds(coordX, coordY, 50, 50);
                empty.setPosition(coordX, coordY);
                graphicBoard[i][k] = empty;
            }
        }

        for(int i = 0; i < logicBoard.length; i++) {

            if(i >= 0 && i <= 5) {
                coordX = (i * 50) + 32;
            } else if (i >= 6 && i <= 11) {
                coordX = (i * 50) + 78;
            } else if (i >= 18 && i <= 23) {
                coordX = 1210 - ((i * 50) + 30);
            } else {
                coordX = 1305 - ((i * 50) + 78);
            }

            for(int k = 0; k < 5; k++) {

                if(i >= 0 && i <= 11) {
                    coordY = (k * 50) + 25;
                } else {
                    // CHANGED: 622
                    coordY = 622 - ((k * 50) + 75);
                }

                Sprite empty = new Sprite(emptyCheckerTexture, 0, 0, 200, 200);
                empty.setBounds(coordX, coordY, 50, 50);
                empty.setPosition(coordX, coordY);
                graphicBoard[i][k] = empty;
            }

            //System.out.println("Logic Board Element: " + logicBoard[i]);

            for(int j = 0; j < Math.abs(logicBoard[i]); j++) { // 2

                if(i >= 0 && i <= 11) {
                    coordY = (j * 50) + 25;
                } else {
                    // CHANGED: 622
                    coordY = 622 - ((j * 50) + 75);
                }

                if(logicBoard[i] > 0) { // if white

                    Sprite white = new Sprite(whiteCheckerTexture, 0, 0, 200, 200);

                    white.setBounds(coordX, coordY, 50, 50);
                    white.setPosition(coordX, coordY);
                    graphicBoard[i][j] = white;

                    //System.out.println("White, Pos X=" + coordX + " Pos Y="+ coordY);

                }

                if(logicBoard[i] < 0) { // if black

                    Sprite black = new Sprite(blackCheckerTexture, 0, 0, 200, 200);

                    black.setBounds(coordX, coordY, 50, 50);
                    black.setPosition(coordX, coordY);
                    graphicBoard[i][j] = black;

                    //System.out.println("Black, Pos X=" + coordX + " Pos Y="+ coordY);
                }

                if (logicBoard[i] == 0) {  // if empty
                   // TODO: be careful about here
                    //System.out.println("Empty, Pos X=" + coordX + " Pos Y="+ coordY);
                    Sprite empty = new Sprite(emptyCheckerTexture, 0, 0, 200, 200);
                    empty.setBounds(coordX, coordY, 50, 50);
                    empty.setPosition(coordX, coordY);
                    graphicBoard[i][j] = empty;
                }
            }
        }

    }

    public boolean move(int from, int to, int dice) {
        int fromColor = getColor(logicBoard[from]);
        int toColor = getColor(logicBoard[to]);

        if(fromColor == 1) { // white goes left 0 -> 23
            // black goes right 23 -> 0
            dice = -dice;
        }

        System.out.println("Dice: " + dice);

        if(fromColor > 0) {
            if(capturedWhitePieces != 0) {
                System.out.println("YOU CANT MOVE. YOU MUST RETURN YOUR CAPTURED PIECES FIRST.");
                return false;
            }
        } else {
            if(capturedBlackPieces != 0) {
                System.out.println("YOU CANT MOVE. YOU MUST RETURN YOUR CAPTURED PIECES FIRST.");
                return false;
            }
        }

        System.out.println("FROM-TO= " + (from - to));
        if(from - to == dice) {

            if(canCapture(from, to, dice)) {
                System.out.println("CAN CAPTURE!");

                if(toColor > 0) { // white piece
                    logicBoard[from]++;
                    logicBoard[to] -= 2; // 1 for captured piece, 1 for new piece
                    capturedWhitePieces++;

                } else { // black piece
                    logicBoard[from]--;
                    logicBoard[to] += 2;
                    capturedBlackPieces++;
                }

                System.out.println("Captured white Pieces: " + capturedWhitePieces);
                System.out.println("Captured black Pieces: " + capturedBlackPieces);

                return true;
            }

            if(canMove(from, to, dice)) {
                System.out.println("CAN MOVE!");

                if(fromColor > 0) { // white
                    logicBoard[from]--;
                    System.out.println("FROM: " + logicBoard[from]);
                    logicBoard[to]++;
                    System.out.println("TO: " + logicBoard[to]);
                } else {
                    logicBoard[from]++;
                    logicBoard[to]--;
                }

                return true;
            }

            placeCheckers();
        }

        System.out.println("EROL?");

        return false;
    }

    public boolean canCapture(int from, int to, int dice) { // kýrma
        int fromColor = getColor(logicBoard[from]);
        int toColor = getColor(logicBoard[to]);

        // check capture movement
        if(toColor != 0 && toColor != fromColor && Math.abs(logicBoard[to]) == 1) {
            return true;
        }

        return false;
    }

    public boolean canMove(int from, int to, int dice) { // hareket
        int fromColor = getColor(logicBoard[from]);
        int toColor = getColor(logicBoard[to]);

        // check empty space and same color movement
        if(toColor == 0 || toColor == fromColor) {
            return true;
        }

        return false;
    }

    public boolean canBearOff(int color) { // toplama

        int counter = 0;

        if(color > 0) {
            if(capturedWhitePieces != 0) {
                return false;
            }

            for(int i = 0; i < 6; i++) {
                counter += logicBoard[i];
            }
        } else {
            if(capturedBlackPieces != 0) {
                return false;
            }

            for(int i = 18; i < 24; i++) {
                counter += logicBoard[i];
            }
        }

        if(Math.abs(counter) == 15) {
            System.out.println("CAN BEAROFF!");
            return true;
        }

        System.out.println("CANNOT BEAROFF");
        return false;
    }

    public boolean bearOff(int from, int dice) {
        int fromColor = getColor(logicBoard[from]);

        if(fromColor == 1) { // white goes right 0 -> 23
                             // black goes left 23 -> 0
            dice = -dice;
        }

        if(canBearOff(fromColor)) {

            if(fromColor == 1) {
                logicBoard[from]--;
                bearoffWhitePieces++;
            } else {
                logicBoard[from]++;
                bearoffBlackPieces++;
            }

            return true;
        } else {
            return false;
        }
    }

    public int checkWinner() {
        if(bearoffWhitePieces == 15) {
            return 1;
        } else if(bearoffBlackPieces == 15) {
            return -1;
        } else {
            return 0;
        }
    }

    public void generatePossibleMoves(int dice[]) {

        if(dice[0] == dice[1]) { // double
            possibleMoves.add(dice[0]);
            possibleMoves.add(dice[0]);
            possibleMoves.add(dice[0]);
            possibleMoves.add(dice[0]);
        } else { // normal
            possibleMoves.add(dice[0]);
            possibleMoves.add(dice[1]);
        }


    }

    public int getColor(int checker) {
        if(checker > 0) {
            return 1;
        } else if(checker < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public int[] convertClickToIndex(int x, int y) {
        int index = 0;
        int graphX = 0;
        int graphY = 0;

        System.out.println("Find piece index with click X: " + x + ", Y: " + y);

        for (int i = 0; i < 24; i++) {
            for(int j = 0; j < 15; j++) {

                //if(graphicBoard[i][j] != null) {
                    if (x > graphicBoard[i][j].getX() && x < graphicBoard[i][j].getX() + 50) {
                        if (y > graphicBoard[i][j].getY() && y < graphicBoard[i][j].getY() + 50) {

                            index = i;

                            graphX = (int)graphicBoard[i][j].getX();
                            graphY = (int)graphicBoard[i][j].getY();

                            System.out.println("Found, index: " + i);
                            break;
                        }
                    }
               // }
            }
        }

        int values[] = new int[3];
        values[0] = index; // index found
        values[1] = graphX; // selected checkers x
        values[2] = graphY; // selected checkers y

        return values;
    }

    // batch drawer
    public void draw(SpriteBatch batch) {
        boardSprite.draw(batch);

        for(int i = 0; i < 24; i++) {
            for(int j = 0; j < 15; j++) {
                /*if(graphicBoard[i][j] != null)*/ graphicBoard[i][j].draw(batch);
            }
        }
    }

    // clean memory
    public void dispose() {
        for(int i = 0; i < 24; i++) {
            for(int j = 0; j < 15; j++) {
                if(graphicBoard[i][j] != null) graphicBoard[i][j].getTexture().dispose();
            }
        }

        boardTexture.dispose();
    }

    // get board representation
    public int[] getLogicBoard() {
        return logicBoard;
    }

}
