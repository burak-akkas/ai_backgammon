package com.cse.bgai;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.util.ArrayList;

public class bgAI extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
	Board board;
	private Rectangle rect;

	Dice dice;
	boolean isWhite = true;
	boolean isDiceRolled = false;
	ArrayList<Integer> dices;
	int currentlyPlayedDice = 0;

	boolean fromSelected = false;
	boolean toSelected = false;

	int fromIndex = 0;
	int toIndex = 0;

	ShapeRenderer sr;
	Rectangle selector;

	BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//img = new Texture(Gdx.files.local("core/assets/badlogic.jpg"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		board = new Board();
		dices = new ArrayList<Integer>();

		dice = new Dice();

		rect = new Rectangle(0, 0, 1, 1);

		sr = new ShapeRenderer();
		selector = new Rectangle(0, 0, 50, 50);

		font =  new BitmapFont();

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if (button == Input.Buttons.LEFT) {

					if(!isDiceRolled) {
						dice.throwDices();
						board.generatePossibleMoves(dice.getDices());
						dices = board.possibleMoves;

						System.out.println("Dices: " + dices.get(0) + ", " + dices.get(1));

						isDiceRolled = true;
					}

					if(!toSelected && isDiceRolled && toIndex == 0) {
							// CHANGED: 160
							rect.setPosition(Gdx.input.getX(), Gdx.graphics.getWidth() - 132 - Gdx.input.getY());
							camera.unproject(new Vector3(rect.getX(), rect.getY(), 0));

							//board.move(5, 2, 3);
							//board.move(11, 13, 2);
							int values[] = board.convertClickToIndex((int)rect.getX(), (int)rect.getY());

							selector.setX(values[1]);
							selector.setY(values[2]);

							fromIndex = values[0];

							fromSelected = true;
							//board.placeCheckers();
					}
				}

				if(button == Input.Buttons.RIGHT) {

					if(fromSelected && !toSelected && isDiceRolled && toIndex == 0 ) {
						rect.setPosition(Gdx.input.getX(), Gdx.graphics.getWidth() - 160 - Gdx.input.getY());
						camera.unproject(new Vector3(rect.getX(), rect.getY(), 0));

						int values[] = board.convertClickToIndex((int)rect.getX(), (int)rect.getY());

						toIndex = values[0];

						toSelected = true;
					}
				}

				return true;
			}
		});

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(board.checkWinner() == 0) {
			if(fromSelected && toSelected) {
				// means MOVE!
				if(board.getLogicBoard()[fromIndex] > 0 && isWhite) { // white playing
					if(dices.contains(Math.abs(fromIndex - toIndex))) {
						int current = dices.indexOf(Math.abs(fromIndex - toIndex));

						System.out.println("Current dice index: " + current + ", value: " + dices.get(current));
						/*
						if(board.capturedWhitePieces > 0) {
							if(board.move(board.capturedWhitePieces, toIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						}*/
						if(board.canBearOff(1)) {
							if(board.bearOff(fromIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						} else {
							if(board.move(fromIndex, toIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						}
					} else {
						System.out.println("YOU CANT PLAY LIKE THAT!");
					}

					if(dices.size() == 0) {
						isWhite = false;
						isDiceRolled = false;
						currentlyPlayedDice = 0;
					}

				} else if(board.getLogicBoard()[fromIndex] < 0 && !isWhite) { // black playing
					if(dices.contains(Math.abs(fromIndex - toIndex))) {
						int current = dices.indexOf(Math.abs(fromIndex - toIndex));

						/*
						if(board.capturedBlackPieces > 0) {
							if(board.move(-board.capturedBlackPieces, toIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						}
						*/
						if(board.canBearOff(-1)) {
							if(board.bearOff(fromIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						} else {
							if(board.move(fromIndex, toIndex, dices.get(current))) {

								currentlyPlayedDice = dices.get(current);

								dices.remove(current);
							}
						}

					} else {
						System.out.println("YOU CANT PLAY LIKE THAT!");
					}

					if(dices.size() == 0) {
						isWhite = true;
						isDiceRolled = false;
						currentlyPlayedDice = 0;
					}
				} else {
					System.out.println("NOT YOUR TURN!");
				}

				//board.move(fromIndex, toIndex, Math.abs(fromIndex - toIndex));

				System.out.println("MOVE! FROM = " + fromIndex + "TO = " + toIndex);

				toSelected = false;
				fromSelected = false;
				fromIndex = 0;
				toIndex = 0;

				selector.setX(0);
				selector.setY(0);

				board.placeCheckers();
			}
		}

		batch.begin();
		board.draw(batch);
		dice.getFirstSprite().draw(batch);
		dice.getSecondSprite().draw(batch);

		// text
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, isWhite ? "WHITE'S TURN" : "BLACK'S TURN", 270, 635);
		if(!isDiceRolled) { font.draw(batch, "(ROLL THE DICE!)", 410, 635); }
		if(isDiceRolled) { font.draw(batch, "(LAST PLAYED: " + (currentlyPlayedDice == 0 ? "NOTHING" : currentlyPlayedDice) + ")", 410, 635); }
		font.draw(batch, "WHITE", 330, 450);
		font.draw(batch, "C: " + board.capturedWhitePieces, 340, 420);
		font.draw(batch, "BLACK ", 330, 250);
		font.draw(batch, "C: " + board.capturedBlackPieces, 340, 220);

		batch.end();

		if(selector.getY() != 0 && selector.getX() != 0) {
			sr.begin(ShapeRenderer.ShapeType.Line);
			sr.setColor(Color.RED);
			sr.rect(selector.getX(), selector.getY(), selector.getWidth(), selector.getHeight());
			sr.end();
		}
	}
}
