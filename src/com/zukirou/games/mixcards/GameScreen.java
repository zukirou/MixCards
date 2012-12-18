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
	
	static int pre_linexy_num;						//カードに番号与えて座標を算出する
	static int no_linexy_num[] = new int [100];		//一度ラインを引いたカードに戻れないようにするチェック用のカード番号
	static int touch = 0;							//タッチしたかどうかのフラグ
	static int rotate = 0;							//カードが選択状態か否かのフラグ
	static int line = 0;							//ラインを引いているか否かのフラグ
	static int line_x;								//タッチしたときにラインを引くためのｘ座標
	static int line_y;								//タッチしたときにラインを引くためのY座標
	static int line_x_dragged;						//ラインを引いているときのX座標
	static int line_y_dragged;						//ラインを引いているときのY座標
	static int line_x_end;							//ラインを引き終わった（スクリーンから指が離れた）時のX座標
	static int line_y_end;							//ラインを引き終わった（スクリーンから指が離れた）時のY座標
	static int line_direction;						//ラインを引く方向を示す変数。上が１。右が２、下３左４
	static int line_direction_lock = 0;				//ラインを一方向にのみ引けるようにするためのフラグ。0にならないと別方向に引けない
	static int select_card_x, select_card_y;		//選択したカードのX座標、Y座標
	static int color_place_x,color_place_y;			//色の場所を変えるときの色のX座標、Y座標
	
	enum GameState{
		Ready,
		Running,
		Paused,
		GameOver
	}

	
	GameState state = GameState.Running;
	World world;
	int oldScore = 0;
	String score = "0";

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
				pre_linexy_num = lineXY(event.x, event.y);
				if(world.card_fields[pre_linexy_num / 7][pre_linexy_num % 7] == false){
					touch = 1;
					line_x = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y = 100 + (40 * (lineXY(event.x, event.y) % 7));
					no_linexy_num[pre_linexy_num] = 1;
					g.drawFingerLineStartInt(line_x, line_y);								
				}
			}
			if(event.type == TouchEvent.TOUCH_DRAGGED){
				int line_x1 = 0;
				int line_y1 = 0;
				
				int check_drag_linexy_num = lineXY(event.x, event.y);
				if(line_direction_lock == 0 && pre_linexy_num - 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 1;//上
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 7 == check_drag_linexy_num){
					line_direction = 2;//右
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 3;//下
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num - 7 == check_drag_linexy_num){
					line_direction = 4;//左
					line_direction_lock = 1;
				}
				if(rotate == 0 && touch == 1 && linexy_num_Check(pre_linexy_num, check_drag_linexy_num)){			
					if(check_line_direction(pre_linexy_num, check_drag_linexy_num,line_direction)){
						line = 1;
						touch = 0;
						no_linexy_num[pre_linexy_num] = 1;
						line_x_dragged = 40 + (40 * (lineXY(event.x, event.y) / 7));
						line_y_dragged = 100 + (40 * (lineXY(event.x, event.y) % 7));
						pre_linexy_num = lineXY(event.x, event.y);
						g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x, line_y);						
					}
				}
			}
			if(event.type == TouchEvent.TOUCH_UP){
				//ライン引いている時
				if(line == 1){
//					g.drawFingerLineEndInt(line_x_dragged, line_y_dragged);
					g.deleteFingerLine();
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
					line = 0;
					line_direction_lock = 0;
					//色の合成を行う
					mix(line_direction, ((line_x - 40) / 40) * 2, ((line_y - 100) / 40) * 2, ((line_x_dragged - 40) / 40) * 2, ((line_y_dragged - 100) / 40) * 2);
				//カードを選択状態にする
				}else if(touch == 1 && rotate == 0 && world.card_fields[lineXY(event.x, event.y) / 7][lineXY(event.x, event.y) % 7] == false){
					rotate = 1;
					touch = 0;
					color_place_x = event.x;
					color_place_y = event.y;
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
				//選択状態のカードを非選択状態にする
				}else if(rotate == 1 && event.x > line_x_end - 20 && event.x < line_x_end + 20 && event.y > line_y_end - 20 && event.y < line_y_end + 20){
					rotate = 0;
					touch = 0;
					line_direction_lock = 0;			
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
				}
				//色の入れ替えを行う				
				if(rotate == 1){
					move_color_place(lineXY(color_place_x, color_place_y) / 7, lineXY(color_place_x, color_place_y) % 7);
				}
			}
		}
		
		world.update(deltaTime);
		
		if(world.gameOver){
//			if(Settings.soundEnabled)  ゲームオーバー時に音出すときはここでやる。
//				Assets.bitten.play(1);
			state = GameState.GameOver;
		}
		
		if(oldScore != world.score){
			oldScore = world.score;
			score = "" + oldScore;
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
		if(state == GameState.Running)
			drawRunningUI();
//		if(state == GameState.Paused)
//			drawPausedUI();
//		if(state == GameState.GameOver)
//			drawGameOverUI();
		drawText(g, score, 105 + score.length(), 16);
	}

	private void drawWorld(World world){
		Graphics g = game.getGraphics();
		Cards card= world.cards;

		Pixmap colorPixmap = null;
		Pixmap cardPixmap = null;

		//カードを表示する
		int i = 0, j = 0, card_count = 0, card_remain = 49;
//		world.card_fields[2][6] = true;//消去テスト
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

		if(rotate == 1){//選択中のカードを表示
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
	
	private void drawRunningUI(){
		Graphics g = game.getGraphics();		
//		g.drawPixmap(Assets.moji, 0, 20, 0, 137, 100, 17);//TimeLimit
		g.drawPixmap(Assets.moji, 0, 20, 0, 154, 97, 14);//Score

	}


	//どの場所（土台）かを返す
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
	
	//斜規制上下左右のみラインが引けるようにする。
	public boolean linexy_num_Check(int present_num, int update_num){
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
	
	//ラインの方向チェック
	public boolean check_line_direction(int present_num, int update_num, int nowdirection){

		if(nowdirection == 1 && present_num - 1 == update_num && (present_num + update_num + 1) % 14 != 0 && world.card_fields[update_num / 7][update_num % 7] == false){
			return true;
		}else if(nowdirection == 2 && present_num + 7 == update_num && world.card_fields[update_num / 7][update_num % 7] == false){
			return true;
		}else if(nowdirection == 3 && present_num + 1 == update_num && (present_num + update_num + 1) % 14 != 0 && world.card_fields[update_num / 7][update_num % 7] == false){
			return true;
		}else if(nowdirection == 4 && present_num - 7 == update_num && world.card_fields[update_num / 7][update_num % 7] == false){
			return true;
		}
		return false;
	}
	
	
	//選択中のカードに配置されている色の場所をいれ変える
	public void move_color_place(int x, int y){
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

	//ラインを引いた方向に応じた色の合成を行う
	public void mix(int direction, int startx, int starty, int endx, int endy){
		switch(direction){
		case 0:
			break;
		case 1://上へドラッグ
			int remain_color_dx = startx;
			int remain_color_dy = starty + 1;
			//ラインが引かれた所の色とカードを消すす
			for (int i = starty - 1; i > endy; i--){
				world.color_fields[startx][i] = 100;
				world.color_fields[startx + 1][i] = 100;
				world.card_fields[startx / 2][i / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx][endy + 1] = world.color_fields[remain_color_dx][remain_color_dy];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_dx + 1][remain_color_dy];
			world.card_fields[endx / 2][endy / 2] = false;
			same_color_check(endx, endy);
			break;
		case 2://右へドラッグ
			int remain_color_lx = startx;
			int remain_color_ly = starty;
			//ラインが引かれた所の色とカードを消す
			for (int i = startx + 1; i < endx; i++){
				world.color_fields[i][starty] = 100;
				world.color_fields[i][starty + 1] = 100;
				world.card_fields[i / 2][starty / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx][endy] = world.color_fields[remain_color_lx][remain_color_ly];
			world.color_fields[endx][endy + 1] = world.color_fields[remain_color_lx][remain_color_ly + 1];
			world.card_fields[endx / 2][endy / 2] = false;
			same_color_check(endx , endy);
			break;
		case 3://下へドラッグ
			int remain_color_ux = startx;
			int remain_color_uy = starty;
			//ラインが引かれた所の色とカードを消す
			for (int i = starty + 1; i < endy; i++){
				world.color_fields[startx][i] = 100;
				world.color_fields[startx + 1][i] = 100;
				world.card_fields[startx / 2][i / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx][endy] = world.color_fields[remain_color_ux][remain_color_uy];
			world.color_fields[endx + 1][endy] = world.color_fields[remain_color_ux + 1][remain_color_uy];
			world.card_fields[endx / 2][endy / 2] = false;
			same_color_check(endx, endy);
			break;
		case 4://左へドラッグ
			int remain_color_rx = startx + 1;
			int remain_color_ry = starty;
			//ラインが引かれた所の色とカードを消す
			for (int i = startx - 1; i > endx; i--){
				world.color_fields[i][starty] = 100;
				world.color_fields[i][starty + 1] = 100;
				world.card_fields[i / 2][starty / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx + 1][endy] = world.color_fields[remain_color_rx][remain_color_ry];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_rx][remain_color_ry + 1];
			world.card_fields[endx / 2][endy / 2] = false;
			same_color_check(endx , endy);
			break;
		default:
			break;
		}
		//スタート地点の色とカードを消す
		world.color_fields[startx][starty] = 100;
		world.color_fields[startx + 1][starty] = 100;
		world.color_fields[startx][starty + 1] = 100;
		world.color_fields[startx + 1][starty + 1] = 100;
		world.card_fields[startx / 2][starty / 2] = true;
	}
	
	//同色かをチェック。同色ならば得点
	public void same_color_check(int x, int y){
		if(world.color_fields[x][y] == world.color_fields[x + 1][y] && world.color_fields[x][y] == world.color_fields[x][y + 1] && world.color_fields[x][y] == world.color_fields[x + 1][y + 1]){
			world.score += 10;
			world.renzoku += 1;
			if(world.renzoku > 2){
				world.score += world.renzoku * 10;
			}else{
				world.renzoku = 0;
			}
		}
	}
	
	//数字を素材で表示する
	public void drawText(Graphics g, String line, int x, int y){
		int len = line.length();
		for(int i = 0; i < len; i++){
			char character = line.charAt(i);
			
			if(character == ' '){
				x += 16;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			/*
			if(character == '.'){
				srcX = 200;
				srcWidth = 20;
			}else{
			*/
			srcX = 3 + (character - '0') * 16;			
			srcWidth = 16;
//			}			
			g.drawPixmap(Assets.moji, x, y, srcX, 185, srcWidth, 19);
//			g.drawPixmap(Assets.mainmenu, 105, 415, 86, 45, 196, 45);			
			x += srcWidth;
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
