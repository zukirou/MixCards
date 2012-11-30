package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Pixmap;
import com.zukirou.gameFrameWork.Screen;
import com.zukirou.games.mixcards.World_mixcards;
import com.zukirou.games.mixcards.Assets;
import com.zukirou.games.mixcards.MainMenuScreen;
import com.zukirou.games.mixcards.World_mixcards;
import com.zukirou.games.mixcards.Settings;
import com.zukirou.games.mixcards.GameScreen.GameState;

public class GameScreen extends Screen{
	enum GameState{
		Ready,
		Running,
		Paused,
		GameOver
	}
	
	GameState state = GameState.Running;
	World_mixcards world;

	public GameScreen(Game game){
		super(game);
		world = new World_mixcards();
	}
	
	@Override
	public void update(float deltaTime){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		if(state == GameState.Running)
			updateRunning(touchEvents,deltaTime);
		if(state == GameState.Paused)
			updatePaused(touchEvents);
		if(state == GameState.GameOver)
			updateGameOver(touchEvents);

	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime){
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_DOWN){
				if(event.x < 64 && event.y > 416){
					world.cards.turnLeft();
					world.colors.turnLeft();
				}
				if(event.x > 256 && event.y > 416){
					world.cards.turnRight();
					world.colors.turnRight();
				}
			}

		}		
	}
	
	private void updatePaused(List<TouchEvent> touchEvents){
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 80 && event.x <= 240){
					if(event.y > 100 && event.y <= 148){
//						if(Settings.soundEnabled)
//							Assets.click.play(1);
						state = GameState.Running;
						return;
					}
					if(event.y > 148 && event.y < 196){
//						if(Settings.soundEnabled)
//							Assets.click.play(1);
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}
	
	private void updateGameOver(List<TouchEvent> touchEvents){
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(	event.x >= 128 && event.x <= 192 && 
					event.y >= 200 && event.y <= 264){
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime){
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		drawWorld(world);
//		if(state == GameState.Running)
//			drawRunningUI();
//		if(state == GameState.Paused)
//			drawPausedUI();
//		if(state == GameState.GameOver)
//			drawGameOverUI();
//		drawText(g, score, g.getWidth() / 2 - score.length() * 20 / 2, g.getHeight() - 42);
	}

	private void drawWorld(World_mixcards world){
		Graphics g = game.getGraphics();
		Cards card= world.cards;
//		Colors colors = world.colors;
//		Colors color[] = world.color;

		
		Pixmap colorPixmap = null;
		Pixmap cardPixmap = null;
		for(int c = 1; c < 4; c++){
			if(world.color[c].color == 1)
				colorPixmap = Assets.red;
			if(world.color[c].color == 2)
				colorPixmap = Assets.green;
			if(world.color[c].color == 3)
				colorPixmap = Assets.blue;
			if(world.color[c].color == 4)
				colorPixmap = Assets.yellow;
		}

		//タッチされたかどうかのフラグでカードのAssetを変える。とりあえず通常時のカードをセット
		cardPixmap = Assets.card;

		//カードを表示する
		int i = 0, j = 0, card_count = 0, card_remain = 49;
		world.card_fields[1][6] = true;//消去テスト
		while(true){
			if(card_count > card_remain)
				break;
			if(world.card_fields[i][j] == false){
				int card_x = 20 + world.card_x[i] * 40;
				int card_y = 80 + world.card_y[j] * 40;
				g.drawPixmap(cardPixmap, card_x, card_y);
				card_count ++;
			}
			i += 1;
			if(i  >= world.WORLD_WIDTH / 2){
				i = 0;
				j += 1;
				if(j >= world.WORLD_HEIGHT / 2){
					j = 0;
				}
			}			
		}
		
		//色を表示する
		int l = world.WORLD_WIDTH * world.WORLD_HEIGHT / 4;
		world.color_fields[6][6] = 0;//消去テスト
		world.color_fields[7][6] = 0;
		world.color_fields[6][7] = 0;		
		world.color_fields[7][7] = 0;
		
		while(true){
			if(l < 0){
				break;
			}
			if(world.color_fields[world.color[l].x][world.color[l].y] == 1){
				int color_x = 20 + world.color[l].x * 20;				
				int color_y = 80 + world.color[l].y * 20;
				g.drawPixmap(colorPixmap, color_x, color_y );
			}
			l --;						
		}

	}

	
	
	
	
	@Override
	public void pause(){
		if(state == GameState.Running)
			state = GameState.Paused;
		
		if(world.gameOver){
//			Settings.addScore(world.score);
//			Settings.save(game.getFileIO());
		}
	}
	
	@Override
	public void resume(){
		
	}
	
	@Override
	public void dispose(){
		
	}
}
