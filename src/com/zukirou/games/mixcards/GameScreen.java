package com.zukirou.games.mixcards;

import java.util.List;
import java.lang.Math;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Pixmap;
import com.zukirou.gameFrameWork.Screen;
import com.zukirou.games.mixcards.World;
import com.zukirou.games.mixcards.Assets;
import com.zukirou.games.mixcards.MainMenuScreen;
import com.zukirou.games.mixcards.Settings;
import com.zukirou.games.mixcards.GameScreen.GameState;

public class GameScreen extends Screen{
	
	static int pre_linexy_num;
	static int no_linexy_num[] = new int [100];
	static int rotate = 0;
	static int line = 0;
	static int touch = 0;
	static int line_x;
	static int line_y;
	static int line_x_dragged;
	static int line_y_dragged;
	static int line_x_end;
	static int line_y_end;
	static int select_card_x, select_card_y;
	static int color_place_x,color_place_y;
	
	enum GameState{
		Ready,
		Running,
		Paused,
		GameOver
	}

	
	GameState state = GameState.Running;
	World world;

	public GameScreen(Game game){
		super(game);
		world = new World();
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
		Graphics g = game.getGraphics();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_DOWN){
				touch = 1;
				int check_touchdown_linexy_num = lineXY(event.x, event.y);
				pre_linexy_num = lineXY(event.x, event.y);
				line_x = 40 + (40 * (lineXY(event.x, event.y) / 7));
				line_y = 100 + (40 * (lineXY(event.x, event.y) % 7));
				no_linexy_num[pre_linexy_num] = 1;
				g.drawFingerLineStartInt(line_x, line_y);				
			}
			
			switch(event.type){
			
			case TouchEvent.TOUCH_DRAGGED:
				touch = 0;
				line = 1;
				int line_x1 = 0;
				int line_y1 = 0;
				int check_drag_linexy_num = lineXY(event.x, event.y);
				if(rotate == 0 && linexy_num_Check(pre_linexy_num, check_drag_linexy_num)){
//				if(linexy_num_Check(pre_linexy_num, check_drag_linexy_num)){
					no_linexy_num[pre_linexy_num] = 1;
					line_x_dragged = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_dragged = 100 + (40 * (lineXY(event.x, event.y) % 7));
					pre_linexy_num = lineXY(event.x, event.y);
//					g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x1, line_y1);
					g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x, line_y);
				}
/*				
				if(event.type == TouchEvent.TOUCH_UP){
//					int check_touchup_linexy_num = lineXY(event.x, event.y);
					if(line == 1){
						g.deleteFingerLine();
						for(int j = 0; j < 99; j++){
							no_linexy_num[j] = 0;
						}
						line = 0;
					}
				}
*/

				break;
				
			case TouchEvent.TOUCH_UP:
				if(line == 1){
					g.deleteFingerLine();
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
					line = 0;
					rotate = 0;
				}

				if(line == 0 && touch == 1){
					rotate = 1;
					color_place_x = event.x;
					color_place_y = event.y;
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
				}
				if(rotate == 1 && event.x > line_x_end - 20 && event.x < line_x_end + 20 && event.y > line_y_end - 20 && event.y < line_y_end + 20){
					rotate = 0;
					touch = 0;
				}
				if(line == 0 && rotate == 1 && event.x < line_x_end - 20 || event.x > line_x_end + 20 || event.y < line_y_end - 20 || event.y > line_y_end + 20){
					move_color_place(lineXY(color_place_x, color_place_y) / 7, lineXY(color_place_x, color_place_y) % 7);	
				}
/*
				if(rotate == 0 && line == 1){
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
					g.drawFingerLineEndInt(line_x_end, line_y_end);
					pre_linexy_num = lineXY(event.x, event.y);
				}
*/				
				break;
				
			default:
				break;
				
			}
						
			
/*
			if(event.type == TouchEvent.TOUCH_DRAGGED){
				line = 1;
				int line_x1 = 0;
				int line_y1 = 0;
				int check_drag_linexy_num = lineXY(event.x, event.y);
				if(rotate == 0 && linexy_num_Check(pre_linexy_num, check_drag_linexy_num)){
					no_linexy_num[pre_linexy_num] = 1;
					line_x_dragged = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_dragged = 100 + (40 * (lineXY(event.x, event.y) % 7));
					pre_linexy_num = lineXY(event.x, event.y);
					g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x1, line_y1);
				}
			}
			if(event.type == TouchEvent.TOUCH_UP){
				int check_touchup_linexy_num = lineXY(event.x, event.y);
				if(line == 1){
//					g.deleteFingerLine();
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
					line = 0;
				}
				
				if(rotate == 0){
					rotate = 1;
					color_place_x = event.x;
					color_place_y = event.y;
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
				}else if(rotate == 1 && event.x > line_x_end - 20 && event.x < line_x_end + 20 && event.y > line_y_end - 20 && event.y < line_y_end + 20){
					rotate = 0;
				}else if(rotate == 1 && event.x < line_x_end - 20 || event.x > line_x_end + 20 || event.y < line_y_end - 20 || event.y > line_y_end + 20){
					move_color_place(lineXY(color_place_x, color_place_y) / 7, lineXY(color_place_x, color_place_y) % 7);	
				}
				if(rotate == 0 && line == 1){
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
					g.drawFingerLineEndInt(line_x_end, line_y_end);
					pre_linexy_num = lineXY(event.x, event.y);
				}
			}
*/
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

	private void drawWorld(World world){
		Graphics g = game.getGraphics();
		Cards card= world.cards;

		Pixmap colorPixmap = null;
		Pixmap cardPixmap = null;

		//カードを表示する
		int i = 0, j = 0, card_count = 0, card_remain = 49;
//		world.card_fields[1][6] = true;//消去テスト
		while(true){
			if(card_count > card_remain)
				break;
			
			if(world.card_fields[i][j] == false){
				int card_x = 20 + world.card_x[i] * 40;
				int card_y = 80 + world.card_y[j] * 40;
					
				cardPixmap = Assets.card;

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

		if(rotate == 1){
			int card_x = line_x_end - 20;
			int card_y = line_y_end - 20;
			cardPixmap = Assets.selected_card;
			g.drawPixmap(cardPixmap, card_x, card_y);
		}
		
		//色を表示する
		int l = world.WORLD_WIDTH * world.WORLD_HEIGHT / 2;
		world.color_fields[6][6] = 100;//消去テスト
		world.color_fields[7][6] = 100;
		world.color_fields[6][7] = 100;		
		world.color_fields[7][7] = 100;
		
		for(int cx = 0; cx < 14; cx ++){
			for(int cy = 0; cy < 14; cy ++){
				if(world.color_fields[world.color_x[cx]][world.color_y[cy]] == 1){
					colorPixmap = Assets.red;
					int color_x = 20 + world.color_x[cx] * 20;				
					int color_y = 80 + world.color_y[cy] * 20;
					g.drawPixmap(colorPixmap, color_x, color_y );					
				}
				if(world.color_fields[world.color_x[cx]][world.color_y[cy]] == 2){
					colorPixmap = Assets.green;
					int color_x = 20 + world.color_x[cx] * 20;				
					int color_y = 80 + world.color_y[cy] * 20;
					g.drawPixmap(colorPixmap, color_x, color_y );					
				}
				if(world.color_fields[world.color_x[cx]][world.color_y[cy]] == 3){
					colorPixmap = Assets.blue;
					int color_x = 20 + world.color_x[cx] * 20;				
					int color_y = 80 + world.color_y[cy] * 20;
					g.drawPixmap(colorPixmap, color_x, color_y );					
				}
				if(world.color_fields[world.color_x[cx]][world.color_y[cy]] == 4){
					colorPixmap = Assets.yellow;
					int color_x = 20 + world.color_x[cx] * 20;				
					int color_y = 80 + world.color_y[cy] * 20;
					g.drawPixmap(colorPixmap, color_x, color_y );					
				}
			}
		}
		
		g.drawFingerLine();
	}

	public int lineXY(int x, int y){
		if(x > 280)
			x = 280;
		if(x < 40)
			x = 40;
		if(y > 360)
			y = 360;
		if(y < 100)
			y = 100;
		
		int line_xy_num = 0;
		int line_x = 0;
		int line_y = 0;

		line_x = (x - 40) / 40;//40は一番左上のカードの中心ｘ座標
		line_y = (y - 100) / 40;//100は一番左上のカードの中心ｙ座標
		line_xy_num = (line_x * 7) + line_y;//左上のカードを「０」とし、その下のカードを１、２、３・・・と連続にし、どのカード番号になるかを求める。
		
		return line_xy_num;
	}
	
	public boolean linexy_num_Check(int present_num, int update_num){ //斜規制上下左右のみラインが引けるようにする。
		if(no_linexy_num[update_num] == 0){//%14は一番上から下、またその逆の場所の斜線を引けないようにするためのもの
			if(		present_num + 1 == update_num && (present_num + update_num + 1) % 14 != 0|| 
					present_num - 1 == update_num && (present_num + update_num + 1) % 14 != 0|| 
					present_num + 7 == update_num ||
					present_num - 7 == update_num ){		
				return true;				
			}
			return false;			
		}
		return false;
	}
	
	public void move_color_place(int x, int y){//配置されている色の場所を変える
		int ltx = x * 2;
		int lty = y * 2;
		int left_top_color = world.color_fields[ltx][lty];
		int lbx = x * 2;
		int lby = (y * 2) + 1;
		int left_bottom_color = world.color_fields[lbx][lby];
		int rtx = (x * 2) + 1;
		int rty = y * 2;
		int right_top_color = world.color_fields[rtx][rty];
		int rbx = (x * 2) + 1;
		int rby = (y * 2) + 1;
		int right_bottom_color = world.color_fields[rbx][rby];
		
		world.color_fields[ltx][lty] = left_bottom_color;
		world.color_fields[lbx][lby] = right_bottom_color;
		world.color_fields[rtx][rty] = left_top_color;
		world.color_fields[rbx][rby] = right_top_color;
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
