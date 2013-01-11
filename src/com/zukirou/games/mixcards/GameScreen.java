package com.zukirou.games.mixcards;

import java.util.List;

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

public class GameScreen extends Screen{
	
	static int pre_linexy_num;						//カードに番号与えて座標を算出する
	static int no_linexy_num[] = new int [100];		//一度ラインを引いたカードに戻れないようにするチェック用のカード番号
	static int touch = 0;							//タッチしたかどうかのフラグ
														//0　基本状態
														//1	downされた
														//2	ドラッグされた
														//3　色配置変え可能
	static int rotate = 0;							//カードが選択状態か否かのフラグ
														//0 非選択状態
														//1 選択されている状態。色配置変え可能
	static int line = 0;							//ラインを引いているか否かのフラグ
														//0　ラインを引いていない（ドラッグしていない）
														//1　ラインを引いている（ドラッグ操作が入った）
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
	public int color1, color2, color3, color4;

	enum GameState{
		Ready,
		Running,
		Paused,
		ReachCount,
		GameOver
	}

	
	GameState state = GameState.Running;
	World world;
	int oldScore = 0;
	String score = "0";
	int oldr_count = 0;
	String r_count = "0";
	int oldg_count = 0;
	String g_count = "0";
	int oldb_count = 0;
	String b_count = "0";
	int oldy_count = 0;
	String y_count = "0";
	int old_samecolor_count = 0;
	int samecolortimes = 0;
	String samecolor_count = "0";
	int old_reset_count = 10;
	int temp_reset_count = 10;
	String reset_count = "10";
	int old_time_limit = 60;
	String time_limit = "60";

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
		if(state == GameState.ReachCount)
			updateReachCount(touchEvents);
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime){
		int len = touchEvents.size();
		Graphics g = game.getGraphics();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_DOWN && event.x > 270 && event.x < 320 && event.y > 0 && event.y < 50) {
				state = GameState.Paused;
				return;
			}

			if(line == 0 && event.type == TouchEvent.TOUCH_DOWN){
				pre_linexy_num = lineXY(event.x, event.y);
				
				if(rotate == 1 && touch_out_of_selectedcard(event.x , event.y, line_x_end, line_y_end)){
					rotate = 0;
					touch = 0;
					line_direction_lock = 0;			
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;	
					}
				
				}
				
				if(world.card_fields[pre_linexy_num / 7][pre_linexy_num % 7] == false){
					touch = 1;
					line_x = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y = 100 + (40 * (lineXY(event.x, event.y) % 7));
					g.drawFingerLineStartInt(line_x, line_y);
					line_direction_lock = 0;
				}					
			}
			if(event.type == TouchEvent.TOUCH_DRAGGED){				
				int check_drag_linexy_num = lineXY(event.x, event.y);
				if(line_direction_lock == 0 && pre_linexy_num - 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 1;//上
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 7 == check_drag_linexy_num && check_drag_linexy_num > 6){
					line_direction = 2;//右
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 3;//下
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num - 7 == check_drag_linexy_num && check_drag_linexy_num < 42){
					line_direction = 4;//左
					line_direction_lock = 1;
				}
				if(touch == 1 && linexy_num_Check(pre_linexy_num, check_drag_linexy_num) &&
						world.card_fields[pre_linexy_num / 7][pre_linexy_num % 7] == false &&
						world.card_fields[check_drag_linexy_num / 7][check_drag_linexy_num % 7] == false){			
					if(check_line_direction(pre_linexy_num, check_drag_linexy_num,line_direction)){
						touch = 2;
						no_linexy_num[pre_linexy_num] = 1;
						line_x_dragged = 40 + (40 * (lineXY(event.x, event.y) / 7));
						line_y_dragged = 100 + (40 * (lineXY(event.x, event.y) % 7));
						g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x, line_y);						
						line = 1;
						rotate = 0;
					}
				}
			}
			if(event.type == TouchEvent.TOUCH_UP){
				int check_touchup_linexy_num = lineXY(event.x, event.y);
				//ライン引いている時
				if(touch == 2 && line == 1){
					//合成
					mix(line_direction, ((line_x - 40) / 40) * 2, ((line_y - 100) / 40) * 2, ((line_x_dragged - 40) / 40) * 2, ((line_y_dragged - 100) / 40) * 2);
					g.deleteFingerLine();
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
					line = 0;
					line_direction_lock = 0;
					touch = 0;
					rotate = 0;
				//カードを選択状態にする
				}else if(world.card_fields[check_touchup_linexy_num / 7][check_touchup_linexy_num % 7] == false && line == 0 && touch == 1 && rotate == 0 && event.x > line_x - 20 && event.x < line_x + 20 && event.y > line_y - 20 && event.y < line_y + 20){
					rotate = 1;
					touch = 0;
					color_place_x = event.x;
					color_place_y = event.y;
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
				//選択状態のカードを非選択状態にする
				}else if(rotate == 1 && touch_out_of_selectedcard(event.x , event.y, line_x_end, line_y_end)){
					rotate = 0;
					touch = 0;
					line_direction_lock = 0;			
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
				}else if(rotate == 1 && world.card_fields[check_touchup_linexy_num / 7][check_touchup_linexy_num % 7] == false){
					touch = 3;
				}
				//色の入れ替えを行う				
				if(touch == 3 && rotate == 1){
					move_color_place(lineXY(color_place_x, color_place_y) / 7, lineXY(color_place_x, color_place_y) % 7);
					touch = 0;
				}else{
					touch = 0;
				}
			}
		}
		
		world.update(deltaTime);
		
		if(world.gameOver){
//			if(Settings.soundEnabled)  ゲームオーバー時に音出すときはここでやる。
//				Assets.bitten.play(1);
			state = GameState.GameOver;
		}
		
		//スコア更新
		if(oldScore != world.score){
			oldScore = world.score;
			score = "" + oldScore;
		}
		
		//リセットカウント更新
		if(old_samecolor_count != world.samecolor_count){
			old_samecolor_count = world.samecolor_count;
			samecolor_count = "" + old_samecolor_count;
		}
		if(old_reset_count != world.reset_count){
			old_reset_count = world.reset_count;
			reset_count = "" + old_reset_count;
		}
		
		//タイムリミット更新
		if(old_time_limit != world.time_limit){
			old_time_limit = world.time_limit;
			time_limit = "" + old_time_limit;
		}

		
		//残り赤カウント更新
		if(oldr_count != world.red_count){
			oldr_count = world.red_count;
			r_count = "" + oldr_count;
		}
		//残り黄カウント更新
		if(oldg_count != world.green_count){
			oldg_count = world.green_count;
			g_count = "" + oldg_count;
		}
		//残り黄カウント更新
		if(oldb_count != world.blue_count){
			oldb_count = world.blue_count;
			b_count = "" + oldb_count;
		}
		//残り黄カウント更新
		if(oldy_count != world.yellow_count){
			oldy_count = world.yellow_count;
			y_count = "" + oldy_count;
		}
		
		//同色カウント＝リセットカウントで配置リセット
		if(world.samecolor_count == old_reset_count){
			state = GameState.ReachCount;			
		}
		
	}
	
	private void updatePaused(List<TouchEvent> touchEvents){
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 174 && event.x <= 280){
					if(event.y > 210 && event.y <= 290){
//						if(Settings.soundEnabled)
//							Assets.click.play(1);
						state = GameState.Running;
						return;
					}
				}
				if(event.x > 80 && event.x <= 140){
					if(event.y > 210 && event.y <= 290){
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
			if(event.type == TouchEvent.TOUCH_DOWN){
				if(	event.x >= 0 && event.x <= 320 && 
					event.y >= 0 && event.y <= 480){
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}
	
	private void updateReachCount(List<TouchEvent> touchEvents){
		int tempscore = world.score;
		int tempresetcount = old_reset_count + 1;
		if(tempresetcount > 18){
			tempresetcount = 18;
		}
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_DOWN){
				if(	event.x >= 0 && event.x <= 320 && 
						event.y >= 0 && event.y <= 480){
					//if(Settings.soundEnabled)
					//Assets.click.play(1);
 					world = new World();
					world.score = tempscore;
					world.reset_count = tempresetcount;					
					state = GameState.Running;
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
		if(state == GameState.Paused)
			drawPausedUI();
		if(state == GameState.GameOver)
			drawGameOverUI();
		if(state == GameState.ReachCount)
			drawReachCountUI();
		
		//スコア表示
		g.drawPixmap(Assets.moji, 0, 20, 0, 154, 97, 14);//Score
		drawLargeNum(g, score, 105 + score.length(), 16);
		
		//リセットカウント表示（フィールド中央）
		drawMiddleNum(g, samecolor_count, 140 + samecolor_count.length(), 200);
		drawMiddleNum(g, reset_count, 153 - reset_count.length(), 223);
		
		//TimeLimit表示
		g.drawPixmap(Assets.moji, 20, 62, 3, 138, 93, 16);//TimeLimit
		drawMiddleCyanNum(g, time_limit, 113 + time_limit.length(), 64);

		//残赤のカウント表示
		g.drawPixmap(Assets.red, 20, 38);
		drawSmallNum(g, r_count, 43 + r_count.length(), 43);

		//残緑のカウント表示
		g.drawPixmap(Assets.green, 83, 38);
		drawSmallNum(g, g_count, 106 + g_count.length(), 43);

		//残青のカウント表示
		g.drawPixmap(Assets.blue, 146, 38);
		drawSmallNum(g, b_count, 169 + b_count.length(), 43);

		//残黄のカウント表示
		g.drawPixmap(Assets.yellow, 209, 38);
		drawSmallNum(g, y_count, 232 + y_count.length(), 43);

	}

	private void drawWorld(World world){
		Graphics g = game.getGraphics();

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
//		world.color_fields[6][6] = 100;//消去テスト
//		world.color_fields[7][6] = 100;
//		world.color_fields[6][7] = 100;		
//		world.color_fields[7][7] = 100;
		
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
		g.drawLine(143, 223, 177, 218, 5, Color.GREEN);

	}
	
	//ゲーム中常時表示UI
	private void drawRunningUI(){
		Graphics g = game.getGraphics();
		
		//QUITボタン
		g.drawRect(280, 5, 20, 20, Color.DKGRAY);
		g.drawRectLine(280, 5, 20, 20, Color.BLACK, 3);

	}
	
	//ポーズ中のUI表示
	private void drawPausedUI(){
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.moji, 115, 173, 0, 338, 102, 26);//QUIT??
		g.drawPixmap(Assets.moji, 90, 240, 0, 365, 52, 23);//Yes
		g.drawPixmap(Assets.moji, 190, 240, 54, 365, 44, 23);//No
		
	}

	//ゲームオーバー
	public void drawGameOverUI(){
		Graphics g = game.getGraphics();
		if(world.time_limit == 0){
			g.drawPixmap(Assets.moji, 47, 120, 0, 428, 226, 32);//TimeOver
			g.drawPixmap(Assets.moji, 52, 280, 0, 304, 216, 14);//Touchscreen
			g.drawPixmap(Assets.moji, 17, 300, 0, 285, 287, 19);//画面にタッチでタイトル戻る
		}else{
			g.drawPixmap(Assets.moji, 47, 120, 0, 217, 228, 32);//GameOver
			g.drawPixmap(Assets.moji, 52, 160, 0, 248, 222, 21);//Youcan't
			g.drawPixmap(Assets.moji, 62, 180, 0, 267, 194, 18);//もう得点とれない
			g.drawPixmap(Assets.moji, 52, 280, 0, 304, 216, 14);//Touchscreen
			g.drawPixmap(Assets.moji, 17, 300, 0, 285, 287, 19);//画面にタッチでタイトル戻る			
		}
	}

	//「お見事」の表示。同色カウントに到達
	public void drawReachCountUI(){
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.moji01, 83, 155, 0, 376, 155, 46);//お見事
		g.drawPixmap(Assets.moji01, 34, 245, 0, 44, 302, 17);//画面にタッチして次に進みます
	}

	//どのカードかを返す
	public int lineXY(int x, int y){
		if(x > 279)
			x = 278;
		if(x < 20)
			x = 20;
		if(y > 358)
			y = 357;
		if(y < 80)
			y = 80;
		
		int line_xy_num = 0;
		int line_x = 0;
		int line_y = 0;

		line_x = (x - 20) / 40;
		line_y = (y - 80) / 40;
		line_xy_num = (line_x * 7) + line_y;//左上のカードを「０番」とし、その下のカードを１、２、３・・・と連続にカード番号をあたえる。２列目の一番上は「7」になる。
		
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
//			return false;			
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
			color1 = world.color_fields[startx][starty];
			color2 = world.color_fields[startx + 1][starty];
			color3 = world.color_fields[endx][endy + 1];
			color4 = world.color_fields[endx + 1][endy + 1];
			//ラインが引かれた所の色とカードを消すす
			for (int i = starty; i > endy; i--){
				world.color_fields[startx][i] = 100;
				world.color_fields[startx + 1][i] = 100;
				world.card_fields[startx / 2][i / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx][endy + 1] = world.color_fields[remain_color_dx][remain_color_dy];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_dx + 1][remain_color_dy];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		case 2://右へドラッグ
			int remain_color_lx = startx;
			int remain_color_ly = starty;
			color1 = world.color_fields[startx + 1][starty];
			color2 = world.color_fields[startx + 1][starty + 1];
			color3 = world.color_fields[endx][endy];
			color4 = world.color_fields[endx][endy + 1];
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
			break;
		case 3://下へドラッグ
			int remain_color_ux = startx;
			int remain_color_uy = starty;
			color1 = world.color_fields[startx][starty + 1];
			color2 = world.color_fields[startx + 1][starty + 1];
			color3 = world.color_fields[endx][endy];
			color4 = world.color_fields[endx + 1][endy];
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
			break;
		case 4://左へドラッグ
			int remain_color_rx = startx + 1;
			int remain_color_ry = starty;
			color1 = world.color_fields[startx][starty];
			color2 = world.color_fields[startx][starty + 1];
			color3 = world.color_fields[endx + 1][endy];
			color4 = world.color_fields[endx + 1][endy + 1];
			//ラインが引かれた所の色とカードを消す
			for (int i = startx; i > endx; i--){
				world.color_fields[i][starty] = 100;
				world.color_fields[i][starty + 1] = 100;
				world.card_fields[i / 2][starty / 2] = true;
			}
			//合成先に色を配置
			world.color_fields[endx + 1][endy] = world.color_fields[remain_color_rx][remain_color_ry];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_rx][remain_color_ry + 1];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		default:
			break;
		}
		
		//消えた色のチェック（残り色の数更新のため）
		vanish_color_check(color1, color2, color3, color4);

		//スタート地点の色とカードを消す
		world.color_fields[startx][starty] = 100;
		world.color_fields[startx + 1][starty] = 100;
		world.color_fields[startx][starty + 1] = 100;
		world.color_fields[startx + 1][starty + 1] = 100;
		world.card_fields[startx / 2][starty / 2] = true;
		
		//消すだけでも１点入る
		world.score += 1;

		//同色チェック（同色でスコア獲得）
		same_color_check(endx, endy);
	}

	//消えた色のチェックをして残り色のカウントを減少させる
	public void vanish_color_check(int num1, int num2, int num3, int num4){
		int num[] = new int[4];
		num[0] = num1;
		num[1] = num2;
		num[2] = num3;
		num[3] = num4;
		for(int i = 0; i < 4; i++){
			switch(num[i]){
			case 0:
				break;
			case 1:
				world.red_count -= 1;
				if(world.red_count < 0)
					world.red_count = 0;
				break;
			case 2:
				world.green_count -= 1;
				if(world.green_count < 0)
					world.green_count = 0;
				break;
			case 3:
				world.blue_count -= 1;
				if(world.blue_count < 0)
					world.blue_count = 0;
				break;
			case 4:
				world.yellow_count -= 1;
				if(world.yellow_count < 0)
					world.yellow_count = 0;
				break;
			default:
				break;
			}			
		}
	}
	
	//同色かをチェック。連続得点権利発生。リセットカウント更新。時間５秒延長（60超えない）
	public void same_color_check(int x, int y){
		if(world.color_fields[x][y] == world.color_fields[x + 1][y] && world.color_fields[x][y] == world.color_fields[x][y + 1] && world.color_fields[x][y] == world.color_fields[x + 1][y + 1]){
			world.score += 10;
			world.renzoku += 1;
			world.samecolor_count += 1;
			world.time_limit += 5;
			if(world.time_limit > 60){
				world.time_limit = 60;
			}

			if(world.renzoku > 2){
				world.score += world.renzoku * 10;
			}
		}else{
			world.renzoku = 0;
		}
	}
	
	//選択中のカードの範囲外がタッチされたかをチェック
	public boolean touch_out_of_selectedcard(int touchx, int touchy, int x, int y){
		if(		touchx < x - 20 && touchy > 0 || 
				touchx > x + 20 && touchy > 0 ||
				touchx > 0 && touchy < y - 20 ||
				touchx > 0 && touchy > y + 20){
			return true;
		}
		return false;
	}
	
	//スコアの数字を素材で表示できるようにする
	public void drawLargeNum(Graphics g, String line, int x, int y){
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

	//色カウントの数字を素材で表示できるようにする
	public void drawSmallNum(Graphics g, String line, int x, int y){
		int len = line.length();
		for(int i = 0; i < len; i++){
			char character = line.charAt(i);
			
			if(character == ' '){
				x += 13;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			srcX =(character - '0') * 13;			
			srcWidth = 13;
			g.drawPixmap(Assets.moji, x, y, srcX, 203, srcWidth, 14);
			x += srcWidth;
		}
	}
	
	//中央のカウントの数字を素材で表示できるようにする
	public void drawMiddleNum(Graphics g, String line, int x, int y){
		int len = line.length();
		for(int i = 0; i < len; i++){
			char character = line.charAt(i);
			
			if(character == ' '){
				x += 15;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			srcX =(character - '0') * 15;			
			srcWidth = 15;
			g.drawPixmap(Assets.moji, x, y, srcX, 318, srcWidth, 17);
			x += srcWidth;
		}		
	}
	
	//タイムリミット用の数字素材を表示できるようにする
	public void drawMiddleCyanNum(Graphics g, String line, int x, int y){
		int len = line.length();
		for(int i = 0; i < len; i++){
			char character = line.charAt(i);
			
			if(character == ' '){
				x += 12;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			srcX =(character - '0') * 12;			
			srcWidth = 12;
			g.drawPixmap(Assets.moji, x, y, srcX, 414, srcWidth, 15);
			x += srcWidth;
		}		
	}

	@Override
	public void pause(){
		if(state == GameState.Running)
			state = GameState.Paused;
		
		if(world.gameOver){
			Settings.addScore(world.score);
			Settings.save(game.getFileIO());
		}
	}
		
	@Override
	public void resume(){
		
	}
	
	@Override
	public void dispose(){
		
	}
}
