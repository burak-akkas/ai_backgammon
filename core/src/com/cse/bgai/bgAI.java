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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import javax.swing.*;
import java.util.ArrayList;

public class bgAI extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	Board board;
	private Rectangle rect;

	Dice dice;
	boolean isWhite = true;
	boolean isDiceRolled = false;
	ArrayList<Integer> dices;
	int currentlyPlayedDice = 0;

	boolean isPlaying = true;

	boolean fromSelected = false;
	boolean toSelected = false;

	int fromIndex = 0;
	int toIndex = 0;

	ShapeRenderer sr;
	Rectangle selector;

	Texture arrowTexture;
	Sprite arrowSprite;

	BitmapFont font;

	// AI TEST
	AI ai;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		board = new Board();
		dices = new ArrayList<Integer>();

		dice = new Dice();

		rect = new Rectangle(0, 0, 1, 1);

		sr = new ShapeRenderer();
		selector = new Rectangle(0, 0, 50, 50);

		font =  new BitmapFont();

		arrowTexture = new Texture(Gdx.files.local("core/assets/arrow.png"));
		arrowSprite = new Sprite(arrowTexture, 0, 0, 300, 213);
		arrowSprite.setSize(50, 35);
		arrowSprite.setBounds(300, 375, 50, 35);
		arrowSprite.setPosition(300, 375);

		// AI
		ai = new AI(board);

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if (button == Input.Buttons.LEFT && isWhite) {

					possibilityCheck();

					if(!isDiceRolled) {

						for(int i = 0; i < dices.size(); i++) { dices.remove(i); }

						dice.throwDices();
						board.generatePossibleMoves(dice.getDices());
						dices = board.possibleMoves;

						System.out.println("Dices: " + dices.get(0) + ", " + dices.get(1));

						isDiceRolled = true;

						possibilityCheck();

						//if(!isWhite) { ai.listPossibleMoves(-1, dices); }
					}

					if(!toSelected && isDiceRolled && toIndex == 0) {
						// CHANGED: 160
						rect.setPosition(Gdx.input.getX(), Gdx.graphics.getWidth() - 132 - Gdx.input.getY());
						camera.unproject(new Vector3(rect.getX(), rect.getY(), 0));

						int values[] = board.convertClickToIndex((int)rect.getX(), (int)rect.getY());

						selector.setX(values[1]);
						selector.setY(values[2]);

						fromIndex = values[0];

						fromSelected = true;
					}
				}

				if(button == Input.Buttons.RIGHT && isWhite) {

					if(fromSelected && !toSelected && isDiceRolled && toIndex == 0 ) {
						rect.setPosition(Gdx.input.getX(), Gdx.graphics.getWidth() - 132 - Gdx.input.getY());
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

		update();

		batch.begin();
			drawDice();
			drawText();

			if(board.mustReEnter(1) && isWhite) {
				arrowSprite.draw(batch);
			}

		batch.end();

		if(selector.getY() != 0 && selector.getX() != 0) {
			sr.begin(ShapeRenderer.ShapeType.Line);
			sr.setColor(Color.RED);
			if(fromIndex == 24 || fromIndex == 25 || fromIndex == 26 || fromIndex == 27) { selector.setHeight(40); selector.setWidth(40); }
			else { selector.setHeight(50); selector.setWidth(50); }
			sr.rect(selector.getX(), selector.getY(), selector.getWidth(), selector.getHeight());
			sr.end();
		}
	}

	public void update() {
		if(board.checkWinner() == 0) {

			if(fromSelected && toSelected && isWhite) {

				if(fromIndex != 25 && fromIndex != 26 && fromIndex != 27
				&& fromIndex != 24 && dices.contains(Math.abs(fromIndex - toIndex))) {
					int current = dices.indexOf(Math.abs(fromIndex - toIndex));
					int currentDice = dices.get(current);

					if((board.getLogicBoard()[fromIndex] > 0 && board.move(fromIndex, toIndex, currentDice))) {

						currentlyPlayedDice = dices.get(current);

						dices.remove(current);
					}
				} else if((fromIndex == 24 || fromIndex == 25) && ((dices.contains(Math.abs(toIndex + 1))))) {

					int current = 0;

					if(isWhite) { current = dices.indexOf(Math.abs(toIndex + 1)); }

					int currentDice = dices.get(current);

					if((isWhite && board.canReEnter(1, dices))) {

						System.out.println("board.reenter(" + fromIndex + ", "+ toIndex + ", " + currentDice + ")");
						if(board.reenter(fromIndex, toIndex, currentDice)) {
							currentlyPlayedDice = dices.get(current);

							dices.remove(current);

						} else {
							System.out.println("YOU CANT REENTER TO HERE");
						}

					} else {
						System.out.println("YOU CANT REENTER WITH THIS DICES. SWITCHING PLAYER");

						for(int i = 0; i < dices.size(); i++) { dices.remove(i); }

						isWhite = !isWhite;
						isDiceRolled = false;
						currentlyPlayedDice = 0;
					}

				} else if((toIndex == 26 && board.canBearoff(1) && isWhite)) { // bear-off move

					if(toIndex == 26 && fromIndex >= 18 && isWhite /*&& dices.contains(24 - fromIndex)*/) {

						if(board.bearoff(fromIndex, toIndex)) {
							//currentlyPlayedDice = dices.get(dices.indexOf(24 - fromIndex));
							currentlyPlayedDice = dices.get(0);

							//dices.remove(dices.indexOf(24 - fromIndex));
							dices.remove(0);
						} else {
							System.out.println("CANT BEAROFF (WHITE)");
						}
					} else {
						System.out.println("NOT YOUR TURN.");
					}
				} else {
					System.out.println("YOU CANT PLAY LIKE THAT!");
				}

				if(dices.size() == 0) {
					isWhite = !isWhite;
					isDiceRolled = false;
					currentlyPlayedDice = 0;
				}

				reset();
				board.placeCheckers();
			} else if(!isWhite) { // BLACK = AI

				ai = new AI(board);

				// roll dice
				if(!isDiceRolled) {
					for(int i = 0; i < dices.size(); i++) { dices.remove(i); }

					dice.throwDices();
					board.generatePossibleMoves(dice.getDices());
					dices = board.possibleMoves;

					System.out.println("Dices: " + dices.get(0) + ", " + dices.get(1));

					isDiceRolled = true;

					try { Thread.sleep(1500); } catch (Exception e) { }
				}

				// make all moves
				System.out.println("++ bgAI : DICE SIZE = " + dices.size());

				int playedDice = ai.thinkPlay(dices);

				try { Thread.sleep(1500); } catch (Exception e) { }

				if(playedDice == -1) {
					System.out.println("++ bgAI : AI CANNOT MOVE");

					// clear when AI move ended
					for(int j = 0; j < dices.size(); j++) { dices.remove(j); }

					isWhite = !isWhite;
					isDiceRolled = false;
					currentlyPlayedDice = 0;
					ai.dispose();



				} else {
					System.out.println("++ bgAI : AI PLAYED DICE " + playedDice);

					currentlyPlayedDice = playedDice;
					// remove played die

					System.out.println("++ bgAI : CURRENT/REMOVED DICE INDEX " + dices.indexOf(playedDice));

					dices.remove(dices.indexOf(playedDice));
				}

				board.placeCheckers();

				// clear when AI move ended
				if(dices.size() == 0) {
					isWhite = !isWhite;
					isDiceRolled = false;
					currentlyPlayedDice = 0;
					ai.dispose();
				}
			}
		} else if(isPlaying) {
			if(board.checkWinner() == 1) {
				System.out.println("WINNER: WHITE!");
				JOptionPane.showMessageDialog(new JFrame(), "YOU WON!");
			} else {
				System.out.println("WINNER: BLACK!");
				JOptionPane.showMessageDialog(new JFrame(), "YOU LOSE.");
			}

			isPlaying = false;
		}
	}

	public void drawText() {
		// text
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, isWhite ? "WHITE'S TURN" : "BG-AI'S TURN", 270, 635);
		if(!isDiceRolled && isWhite) { font.draw(batch, "(ROLL THE DICE!)", 410, 635); }
		if(!isDiceRolled && !isWhite) { font.draw(batch, "(AI IS PLAYING.)", 410, 635); }
		if(isDiceRolled) { font.draw(batch, "(LAST PLAYED: " + (currentlyPlayedDice == 0 ? "NOTHING" : currentlyPlayedDice) + ")", 410, 635); }

		// captured
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		//font.draw(batch, "WHITE", 330, 450);
		font.draw(batch, ""+board.getLogicBoard()[board.WHITE_CAPTURED_FIELD], 351, 355);
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		//font.draw(batch, "BLACK ", 330, 250);
		font.draw(batch, "" + board.getLogicBoard()[board.BLACK_CAPTURED_FIELD], 351, 280);

		// bear-off
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(batch, ""+board.getLogicBoard()[board.WHITE_BEAROFF_FIELD], 726, 490);
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, "" + board.getLogicBoard()[board.BLACK_BEAROFF_FIELD], 726, 145);
	}

	public void drawDice() {
		board.draw(batch);
		dice.getFirstSprite().draw(batch);
		dice.getSecondSprite().draw(batch);
	}

	public void possibilityCheck() {

		int color = 0;
		if(isWhite) { color = 1; } else { color = -1; }

		if(isDiceRolled) {
			if(board.canBearoff(1)) {
				System.out.println("WHITE PLAYER CAN BEAROFF");
			} else if (board.mustReEnter(color)) {
				if(!board.canReEnter(color, dices)) {
					System.out.println("REENTER NOT POSSIBLE. SWITCHING PLAYER.");

					for(int i = 0; i < dices.size(); i++) { dices.remove(i); }
					reset();
					currentlyPlayedDice = 0;
					isDiceRolled = false;
					isWhite = !isWhite;
				}
			} else if(!board.hasPossibleMoves(color, dices)) {
				System.out.println("NO POSSIBLE MOVES. SWITCHING PLAYER.");

				for(int i = 0; i < dices.size(); i++) { dices.remove(i); }
				reset();
				currentlyPlayedDice = 0;
				isDiceRolled = false;
				isWhite = !isWhite;
			} else {
				System.out.println("Playing normally.");
			}
		}
	}

	public void reset() {
		toSelected = false;
		fromSelected = false;
		fromIndex = 0;
		toIndex = 0;

		//currentlyPlayedDice = 0;

		selector.setX(0);
		selector.setY(0);
	}
}
