package com.cse.bgai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 *
 * @author 130017964
 * @version 4.20(release)
 *
 *          Used to generate pair of random values in range (1-6) for backgammon
 *          dice throw
 */
public class Dice {
    private Random rn;
    private int diceX;
    private int diceY;

    private Texture t1 = new Texture(Gdx.files.local("core/assets/dice/dice1.png"));
    private Texture t2 = new Texture(Gdx.files.local("core/assets/dice/dice2.png"));
    private Texture t3 = new Texture(Gdx.files.local("core/assets/dice/dice3.png"));
    private Texture t4 = new Texture(Gdx.files.local("core/assets/dice/dice4.png"));
    private Texture t5 = new Texture(Gdx.files.local("core/assets/dice/dice5.png"));
    private Texture t6 = new Texture(Gdx.files.local("core/assets/dice/dice6.png"));

    private Texture q = new Texture(Gdx.files.local("core/assets/dice/question.png"));

    private Sprite s1 = new Sprite(t1, 200, 200);
    private Sprite s2 = new Sprite(t2, 200, 200);
    private Sprite s3 = new Sprite(t3, 200, 200);
    private Sprite s4 = new Sprite(t4, 200, 200);
    private Sprite s5 = new Sprite(t5, 200, 200);
    private Sprite s6 = new Sprite(t6, 200, 200);

    private Sprite sprite1 = new Sprite(q, 200, 200);
    private Sprite sprite2 = new Sprite(q, 200, 200);

    public Dice() {
        rn = new Random();
    }

    public void throwDices() {
        diceX = 1 + rn.nextInt(5);
        diceY = 1 + rn.nextInt(5);

        switch (diceX) {
            case 1:
                sprite1 = s1;
                break;
            case 2:
                sprite1 = s2;
                break;
            case 3:
                sprite1 = s3;
                break;
            case 4:
                sprite1 = s4;
                break;
            case 5:
                sprite1 = s5;
                break;
            case 6:
                sprite1 = s6;
                break;
            default:
                break;
        }
        switch (diceY) {
            case 1:
                sprite2 = s1;
                break;
            case 2:
                sprite2 = s2;
                break;
            case 3:
                sprite2 = s3;
                break;
            case 4:
                sprite2 = s4;
                break;
            case 5:
                sprite2 = s5;
                break;
            case 6:
                sprite2 = s6;
                break;
            default:
                break;
        }
    }

    public int[] getDices() {
        // return new int[]{diceX,diceY};
        return new int[] { diceX, diceY};
    }

    public Sprite getFirstSprite() {
        sprite1.setSize(50, 50);
        sprite1.setPosition(705, 255);
        return sprite1;
    }

    public Sprite getSecondSprite() {
        sprite2.setSize(50, 50);
        sprite2.setPosition(705, 300);
        return sprite2;
    }
}
