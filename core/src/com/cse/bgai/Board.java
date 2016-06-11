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
    public int logicBoard[];

    public final int WHITE_CAPTURED_FIELD = 24;
    public final int BLACK_CAPTURED_FIELD = 25;
    public final int WHITE_BEAROFF_FIELD = 26;
    public final int BLACK_BEAROFF_FIELD = 27;

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
        logicBoard = new int[28];
        graphicBoard = new Sprite[28][15];

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
        for(int i = 0; i < 28; i++) { logicBoard[i] = 0; }

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

        for(int i = 0; i < 28; i++) {
            for(int k = 5; k < 15; k++) {
                Sprite empty = new Sprite(emptyCheckerTexture, 0, 0, 200, 200);
                empty.setBounds(coordX, coordY, 50, 50);
                empty.setPosition(coordX, coordY);
                graphicBoard[i][k] = empty;
            }
        }

        for(int i = 0; i < 28; i++) {

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

        // special cases
        // captured
        Sprite whiteCaptureSprite = new Sprite(whiteCheckerTexture, 0, 0, 200, 200);
        whiteCaptureSprite.setSize(40, 40);
        whiteCaptureSprite.setBounds(335, 330, 40, 40);
        whiteCaptureSprite.setPosition(335, 330);
        graphicBoard[24][0] = whiteCaptureSprite;

        Sprite blackCaptureSprite = new Sprite(blackCheckerTexture, 0, 0, 200, 200);
        blackCaptureSprite.setSize(40, 40);
        blackCaptureSprite.setBounds(335, 255, 40, 40);
        blackCaptureSprite.setPosition(335, 255);
        graphicBoard[25][0] = blackCaptureSprite;

        // bearoff
        Sprite whiteBearoffSprite = new Sprite(whiteCheckerTexture, 0, 0, 200, 200);
        whiteBearoffSprite.setSize(40, 40);
        whiteBearoffSprite.setBounds(710, 465, 40, 40);
        whiteBearoffSprite.setPosition(710, 465);
        graphicBoard[26][0] = whiteBearoffSprite;

        Sprite blackBearoffSprite = new Sprite(blackCheckerTexture, 0, 0, 200, 200);
        blackBearoffSprite.setSize(40, 40);
        blackBearoffSprite.setBounds(710, 120, 40, 40);
        blackBearoffSprite.setPosition(710, 120);
        graphicBoard[27][0] = blackBearoffSprite;
    }

    public boolean reenter(int from, int to, int dice) {

        int fromColor = 0;

        if(from == WHITE_CAPTURED_FIELD) {
            fromColor = 1;
        } else if(from == BLACK_CAPTURED_FIELD){
            fromColor = -1;
        } else {
            return false;
        }

        int toColor = getColor(to); // 0

        if(fromColor == 1) { dice = -dice; };

        if(logicBoard[from] != 0) {
            reenterMove(fromColor, dice);
        } else {
            return false;
        }

        return true;
    }

    public boolean move(int from, int to, int dice) {
        int fromColor = getColor(from);
        int toColor = getColor(to);

        if(fromColor == 1) { dice = -dice; }

        int fieldSelector = 0;
        if(fromColor == 1) { fieldSelector = WHITE_CAPTURED_FIELD; }
        else { fieldSelector = BLACK_CAPTURED_FIELD; }

        if(from - to != dice) { return false; }

        // if selected color not have a captured piece
        if(logicBoard[fieldSelector] == 0) {
            // its a normal move
            if(fromColor == toColor || toColor == 0) {
                normalMove(fromColor, from, to);
            }
            // its a capture move
            else if(fromColor != toColor && Math.abs(logicBoard[to]) == 1) {
                captureMove(fromColor, from, to);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean bearoff(int from, int to) { // 0, 27 +
        int fromColor = getColor(from); // -1 +

        int fieldSelector = 0;
        if(fromColor == 1) { fieldSelector = WHITE_BEAROFF_FIELD; }
        else { fieldSelector = BLACK_BEAROFF_FIELD; } // fieldSelector = BLACK_BEAROFF_FIELD +

        // 1st cond +,
        if(to == fieldSelector && canBearoff(fromColor)) {
            bearoffMove(fromColor, from);

            return true;
        } else {
            return false;
        }
    }

    public void normalMove(int color, int from, int to) {
        if(color == 1) { // white
            logicBoard[from]--;
            logicBoard[to]++;
        } else {        // black
            logicBoard[from]++;
            logicBoard[to]--;
        }
    }

    public void captureMove(int color, int from, int to) {
        if(color == 1) {
            logicBoard[from]--;
            logicBoard[to] += 2;
            logicBoard[BLACK_CAPTURED_FIELD]++;
        } else {
            logicBoard[from]++;
            logicBoard[to] -= 2;
            logicBoard[WHITE_CAPTURED_FIELD]++;
        }
    }

    public void bearoffMove(int color, int from) {
        if(color == 1) { // white
            logicBoard[WHITE_BEAROFF_FIELD]++;
            logicBoard[from]--;

            System.out.println("BEAROFFMOVE FOR WHITE");

        } else {        // black
            logicBoard[BLACK_BEAROFF_FIELD]++;
            logicBoard[from]++;

            System.out.println("BEAROFFMOVE FOR BLACK");
        }
    }

    // changed: to -> dice
    public void reenterMove(int color, int dice) {
        if(color == 1) {
            logicBoard[WHITE_CAPTURED_FIELD]--;

            if(logicBoard[-dice - 1] == -1) {
                logicBoard[BLACK_CAPTURED_FIELD]++;
                logicBoard[-dice - 1]++;
            }

            logicBoard[-dice - 1]++;
        } else { // -1
            logicBoard[BLACK_CAPTURED_FIELD]--;

            if(logicBoard[24 - dice] == 1) {
                logicBoard[WHITE_CAPTURED_FIELD]++;
                logicBoard[24 - dice]--;
            }

            logicBoard[24 - dice]--;
        }
    }

    public boolean canReEnter(int color, ArrayList<Integer> dices) {

        int selected = 0;

        if(color == 1) { // white
            for(int i = 0; i < dices.size(); i++) {
                selected = dices.get(i);

                if(getColor(selected - 1) == color || logicBoard[selected - 1] == 0 || logicBoard[selected - 1] == -color) {
                    return true;
                }
            }
        } else {
            for(int i = 0; i < dices.size(); i++) {
                selected = dices.get(i);

                if(getColor(24 - selected) == color || logicBoard[24 - selected] == 0 || logicBoard[24 - selected] == -color) {
                    return true;
                }
            }
        }

        return false;
    }

    // AI FUNCTION : returns an integer (dice) to reenter with.
    //             : if not, returns -1
    public int canReEnterAI(ArrayList<Integer> dices) {

        int selected = 0;

        for(int i = 0; i < dices.size(); i++) {
            selected = dices.get(i);

            if(getColor(24 - selected) == -1 || logicBoard[24 - selected] == 0 || logicBoard[24 - selected] == 1) {
                return selected;
            }
        }

        return -1;
    }

    public boolean mustReEnter(int color) {
        int field = 0;

        if(color == 1) { field = WHITE_CAPTURED_FIELD; }
        else { field = BLACK_CAPTURED_FIELD; }

        if(logicBoard[field] != 0) {
            return true;
        }

        return false;
    }

    public boolean hasPossibleMoves(int color, ArrayList<Integer> dices) {

        int selected = 0;

        for(int i = 0; i < dices.size(); i++) {     // black 23->0
            selected = dices.get(i);                // white 0->23

            if(color == 1) {                        // beyaz taþlar için
                for(int j = 0; j < 24; j++) {       // tahtanýn tümünü kontrol et

                    if(j + selected < 24) {
                        if (getColor(j) == 1) {      // eðer üzerinde olduðum taþ beyaz ise

                            if (canBearoff(1)) {
                                return true;
                            }

                            // eðer zar ile ilerlediðimde gelen taþ
                            // 1. siyah ve 1 tane ise
                            // 2. boþ ise
                            // 3. beyaz renkte ise
                            // hamle yapmak mümkündür
                            else if (logicBoard[j + selected] == -1
                                    || logicBoard[j + selected] == 0
                                    || getColor(j + selected) == 1) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                for(int j = 23; j >= 0; j--) {       // tahtanýn tümünü kontrol et

                    if(j - selected > 0) {
                        if(getColor(j) == -1) {      // eðer üzerinde olduðum taþ beyaz ise

                            if(canBearoff(-1)) {
                                return true;
                            }
                            // eðer zar ile ilerlediðimde gelen taþ
                            // 1. siyah ve 1 tane ise
                            // 2. boþ ise
                            // 3. beyaz renkte ise
                            // hamle yapmak mümkündür
                            else if(logicBoard[j - selected] == 1
                                    || logicBoard[j - selected] == 0
                                    || getColor(j - selected) == -1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean canBearoff(int color) {

        int counter = 0;

        if(color == 1 && logicBoard[WHITE_CAPTURED_FIELD] == 0) {
            for(int i = 18; i < 24; i++) {
                if(getColor(i) == color) {
                    counter += logicBoard[i];
                }
            }
        } else if(color == -1 && logicBoard[BLACK_CAPTURED_FIELD] == 0){
            for(int i = 0; i < 6; i++) {
                if(getColor(i) == color) {
                    counter += logicBoard[i];
                }
            }
        }

        if(color == 1)
        {
            if ((Math.abs(counter) + logicBoard[WHITE_BEAROFF_FIELD]) == 15) {
                return true;
            }
        } else if(color == -1) {
            if ((Math.abs(counter) + logicBoard[BLACK_BEAROFF_FIELD]) == 15) {
                return true;
            }
        }

        return false;
    }

    public int checkWinner() {
        if(logicBoard[WHITE_BEAROFF_FIELD] == 15) {
            return 1;
        } else if(logicBoard[BLACK_BEAROFF_FIELD] == 15) {
            return -1;
        } else {
            return 0;
        }
    }

    public void generatePossibleMoves(int dice[]) {

        for(int i = 0; i < possibleMoves.size(); i++) { possibleMoves.remove(i); }

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

    public int getColor(int index) {

        int checker = logicBoard[index];

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

        for (int i = 0; i < 28; i++) {
            for(int j = 0; j < 15; j++) {
            if(graphX == 0 && graphY == 0) {
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
            } else {
                break;
            }

                // }
            }
        }

        /*
        // if index equals to zero,
        // check for special sprite indexes
        // whiteCaptureSprite.setBounds(335, 330, 40, 40);
        if(index == 0) {
            if(x > 335 && x < 375) {
                if(y > )
            }
        }
        */

        int values[] = new int[3];
        values[0] = index; // index found
        values[1] = graphX; // selected checkers x
        values[2] = graphY; // selected checkers y

        return values;
    }

    // batch drawer
    public void draw(SpriteBatch batch) {
        boardSprite.draw(batch);

        for(int i = 0; i < 28; i++) {
            for(int j = 0; j < 15; j++) {
                /*if(graphicBoard[i][j] != null)*/
                if(i > 23 && j > 0) { break; }
                else { graphicBoard[i][j].draw(batch); }
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
