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
import com.zukirou.games.mixcards.GameTimeLimitScreen.GameState;

public class GameTimeLimitScreen extends Screen{
	
	static int pre_linexy_num;						//�J�[�h�ɔԍ��^���č��W���Z�o����
	static int no_linexy_num[] = new int [100];		//��x���C�����������J�[�h�ɖ߂�Ȃ��悤�ɂ���`�F�b�N�p�̃J�[�h�ԍ�
	static int touch = 0;							//�^�b�`�������ǂ����̃t���O
														//0�@��{���
														//1	down���ꂽ
														//2	�h���b�O���ꂽ
														//3�@�F�z�u�ς��\
	static int rotate = 0;							//�J�[�h���I����Ԃ��ۂ��̃t���O
														//0 ��I�����
														//1 �I������Ă����ԁB�F�z�u�ς��\
	static int line = 0;							//���C���������Ă��邩�ۂ��̃t���O
														//0�@���C���������Ă��Ȃ��i�h���b�O���Ă��Ȃ��j
														//1�@���C���������Ă���i�h���b�O���삪�������j
	static int line_x;								//�^�b�`�����Ƃ��Ƀ��C�����������߂̂����W
	static int line_y;								//�^�b�`�����Ƃ��Ƀ��C�����������߂�Y���W
	static int line_x_dragged;						//���C���������Ă���Ƃ���X���W
	static int line_y_dragged;						//���C���������Ă���Ƃ���Y���W
	static int line_x_end;							//���C���������I������i�X�N���[������w�����ꂽ�j����X���W
	static int line_y_end;							//���C���������I������i�X�N���[������w�����ꂽ�j����Y���W
	static int line_direction;						//���C�������������������ϐ��B�オ�P�B�E���Q�A���R���S
	static int line_direction_lock = 0;				//���C����������ɂ݈̂�����悤�ɂ��邽�߂̃t���O�B0�ɂȂ�Ȃ��ƕʕ����Ɉ����Ȃ�
	static int select_card_x, select_card_y;		//�I�������J�[�h��X���W�AY���W
	static int color_place_x,color_place_y;			//�F�̏ꏊ��ς���Ƃ��̐F��X���W�AY���W
	public int color1, color2, color3, color4;

	enum GameState{
		Ready,
		Running,
		Paused,
		GameOver,
		ResetField
	}

	
	GameState state = GameState.Running;
	WorldTimeLimit world;
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

	public GameTimeLimitScreen(Game game){
		super(game);
		world = new WorldTimeLimit();
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
		if(state == GameState.ResetField)
			updateResetField(deltaTime);

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

			if(touch == 0 && line == 0 && event.type == TouchEvent.TOUCH_DOWN){
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
				int check_drag_linexy_num = lineXY(event.x, event.y);
				if(line_direction_lock == 0 && pre_linexy_num - 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 1;//��
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 7 == check_drag_linexy_num){
					line_direction = 2;//�E
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num + 1 == check_drag_linexy_num && (pre_linexy_num + check_drag_linexy_num + 1) % 14 != 0){
					line_direction = 3;//��
					line_direction_lock = 1;
				}else if(line_direction_lock == 0 && pre_linexy_num - 7 == check_drag_linexy_num){
					line_direction = 4;//��
					line_direction_lock = 1;
				}
				if(rotate == 0 && touch == 1 && linexy_num_Check(pre_linexy_num, check_drag_linexy_num)){			
					if(check_line_direction(pre_linexy_num, check_drag_linexy_num,line_direction)){
						touch = 2;
						no_linexy_num[pre_linexy_num] = 1;
						line_x_dragged = 40 + (40 * (lineXY(event.x, event.y) / 7));
						line_y_dragged = 100 + (40 * (lineXY(event.x, event.y) % 7));
						pre_linexy_num = lineXY(event.x, event.y);
						g.drawFingerLineMoveInt(line_x_dragged, line_y_dragged, line_x, line_y);						
						line = 1;
					}
				}
			}
			if(event.type == TouchEvent.TOUCH_UP){
				//���C�������Ă��鎞
				if(touch == 2 && line == 1){
					//�F�̍������s��
					mix(line_direction, ((line_x - 40) / 40) * 2, ((line_y - 100) / 40) * 2, ((line_x_dragged - 40) / 40) * 2, ((line_y_dragged - 100) / 40) * 2);
					g.deleteFingerLine();
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
					line = 0;
					line_direction_lock = 0;
					touch = 0;
				//�J�[�h��I����Ԃɂ���
				}else if(line == 0 && touch == 1 && rotate == 0 && world.card_fields[lineXY(event.x, event.y) / 7][lineXY(event.x, event.y) % 7] == false){
					rotate = 1;
					touch = 0;
					color_place_x = event.x;
					color_place_y = event.y;
					line_x_end = 40 + (40 * (lineXY(event.x, event.y) / 7));
					line_y_end = 100 + (40 * (lineXY(event.x, event.y) % 7));
				//�I����Ԃ̃J�[�h���I����Ԃɂ���
				}else if(rotate == 1 && event.x > line_x_end - 20 && event.x < line_x_end + 20 && event.y > line_y_end - 20 && event.y < line_y_end + 20){
					rotate = 0;
					touch = 0;
					line_direction_lock = 0;			
					for(int j = 0; j < 99; j++){
						no_linexy_num[j] = 0;
					}
				}else{
					touch = 3;
				}
				//�F�̓���ւ����s��				
				if(touch == 3 && rotate == 1){
					move_color_place(lineXY(color_place_x, color_place_y) / 7, lineXY(color_place_x, color_place_y) % 7);
				}else{
					touch = 0;
				}
			}
		}
		
		world.update(deltaTime);
		
		if(world.gameOver){
//			if(Settings.soundEnabled)  �Q�[���I�[�o�[���ɉ��o���Ƃ��͂����ł��B
//				Assets.bitten.play(1);
			state = GameState.GameOver;
		}
		if(world.resetField){
			state = GameState.ResetField;
		}
		
		//�X�R�A�X�V
		if(oldScore != world.score){
			oldScore = world.score;
			score = "" + oldScore;
		}
		
		//�^�C�����~�b�g
		if(old_time_limit != world.time_limit){
			old_time_limit = world.time_limit;
			time_limit = "" + old_time_limit;
		}
		
		//���Z�b�g�J�E���g�X�V
		if(old_samecolor_count != world.samecolor_count){
			old_samecolor_count = world.samecolor_count;
			samecolor_count = "" + old_samecolor_count;
		}
		if(old_reset_count != world.reset_count){
			old_reset_count = world.reset_count;
			reset_count = "" + old_reset_count;
		}
		
		//�c��ԃJ�E���g�X�V
		if(oldr_count != world.red_count){
			oldr_count = world.red_count;
			r_count = "" + oldr_count;
		}
		//�c�艩�J�E���g�X�V
		if(oldg_count != world.green_count){
			oldg_count = world.green_count;
			g_count = "" + oldg_count;
		}
		//�c�艩�J�E���g�X�V
		if(oldb_count != world.blue_count){
			oldb_count = world.blue_count;
			b_count = "" + oldb_count;
		}
		//�c�艩�J�E���g�X�V
		if(oldy_count != world.yellow_count){
			oldy_count = world.yellow_count;
			y_count = "" + oldy_count;
		}
		
		//���F�J�E���g�����Z�b�g�J�E���g�Ŕz�u���Z�b�g
		if(world.samecolor_count == old_reset_count){
			state = GameState.ResetField;
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

	private void updateResetField(float deltaTime){
			int tempscore = world.score;
			int tempresetcount = old_reset_count + 1;
			if(tempresetcount > 20){
				tempresetcount = 20;
			}
			world = new WorldTimeLimit();
			world.score = tempscore;
			world.reset_count = tempresetcount;	
			state = GameState.Running;
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
		if(state == GameState.ResetField)
			drawResetFieldUI(deltaTime);
		
		//�X�R�A�\��
		g.drawPixmap(Assets.moji, 0, 20, 0, 154, 97, 14);//Score
		drawLargeNum(g, score, 105 + score.length(), 16);
		
		//TimeLimit�\��
		g.drawPixmap(Assets.moji, 20, 365, 3, 138, 93, 16);//TimeLimit
		drawMiddleCyanNum(g, time_limit, 113 + time_limit.length(), 365);
		
		//���Z�b�g�J�E���g�\���i�t�B�[���h�����j
		drawMiddleNum(g, samecolor_count, 140 + samecolor_count.length(), 200);
		drawMiddleNum(g, reset_count, 153 - reset_count.length(), 223);
		
		//�c�Ԃ̃J�E���g�\��
		g.drawPixmap(Assets.red, 20, 45);
		drawSmallNum(g, r_count, 43 + r_count.length(), 48);

		//�c�΂̃J�E���g�\��
		g.drawPixmap(Assets.green, 83, 45);
		drawSmallNum(g, g_count, 106 + g_count.length(), 48);

		//�c�̃J�E���g�\��
		g.drawPixmap(Assets.blue, 146, 45);
		drawSmallNum(g, b_count, 169 + b_count.length(), 48);

		//�c���̃J�E���g�\��
		g.drawPixmap(Assets.yellow, 209, 45);
		drawSmallNum(g, y_count, 232 + y_count.length(), 48);

	}

	private void drawWorld(WorldTimeLimit world){
		Graphics g = game.getGraphics();
		Cards card= world.cards;

		Pixmap colorPixmap = null;
		Pixmap cardPixmap = null;

		//�J�[�h��\������
		int i = 0, j = 0, card_count = 0, card_remain = 49;
//		world.card_fields[2][6] = true;//�����e�X�g
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

		if(rotate == 1){//�I�𒆂̃J�[�h��\��
			int card_x = line_x_end - 20;
			int card_y = line_y_end - 20;
			cardPixmap = Assets.selected_card;
			g.drawPixmap(cardPixmap, card_x, card_y);
		}
		
		//�F��\������
//		world.color_fields[6][6] = 100;//�����e�X�g
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
	
	//�Q�[�����펞�\��UI
	private void drawRunningUI(){
		Graphics g = game.getGraphics();
		
		//QUIT�{�^��
		g.drawRect(280, 5, 20, 20, Color.DKGRAY);
		g.drawRectLine(280, 5, 20, 20, Color.BLACK, 3);

	}
	
	private void drawPausedUI(){
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.moji, 115, 173, 0, 338, 102, 26);//QUIT??
		g.drawPixmap(Assets.moji, 90, 240, 0, 365, 52, 23);//Yes
		g.drawPixmap(Assets.moji, 190, 240, 54, 365, 44, 23);//No
		
	}

	//���F�J�E���g�B��
	public void drawResetFieldUI(float deltaTime){
		Graphics g = game.getGraphics();
		float reset_field_waitTime = 10000.0f;
		while(reset_field_waitTime > 0){
			g.drawPixmap(Assets.moji, 80, 200, 4, 170, 81, 13);
			reset_field_waitTime -= deltaTime;						
		}
		return;

	}
	
	//�Q�[���I�[�o�[
	public void drawGameOverUI(){
		Graphics g = game.getGraphics();
		if(world.time_limit == 0){
			g.drawPixmap(Assets.moji, 47, 120, 0, 428, 226, 32);//TimeOver
			g.drawPixmap(Assets.moji, 52, 280, 0, 304, 216, 14);//Touchscreen
			g.drawPixmap(Assets.moji, 17, 300, 0, 285, 287, 19);//��ʂɃ^�b�`�Ń^�C�g���߂�
		}else{
			g.drawPixmap(Assets.moji, 47, 120, 0, 217, 228, 32);//GameOver
			g.drawPixmap(Assets.moji, 52, 160, 0, 248, 222, 21);//Youcan't
			g.drawPixmap(Assets.moji, 62, 180, 0, 267, 194, 18);//�������_�Ƃ�Ȃ�
			g.drawPixmap(Assets.moji, 52, 280, 0, 304, 216, 14);//Touchscreen
			g.drawPixmap(Assets.moji, 17, 300, 0, 285, 287, 19);//��ʂɃ^�b�`�Ń^�C�g���߂�			
		}
	}


	//�ǂ̃J�[�h����Ԃ�
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

		line_x = (x - 40) / 40;//40�͈�ԍ���̃J�[�h�̒��S�����W
		line_y = (y - 100) / 40;//100�͈�ԍ���̃J�[�h�̒��S�����W
		line_xy_num = (line_x * 7) + line_y;//����̃J�[�h���u�O�v�Ƃ��A���̉��̃J�[�h���P�A�Q�A�R�E�E�E�ƘA���ɂ��A�ǂ̃J�[�h�ԍ��ɂȂ邩�����߂�B
		
		return line_xy_num;
	}
	
	//�΋K���㉺���E�̂݃��C����������悤�ɂ���B
	public boolean linexy_num_Check(int present_num, int update_num){
		if(no_linexy_num[update_num] == 0){//%14�͈�ԏォ�牺�A�܂����̋t�̏ꏊ�̎ΐ��������Ȃ��悤�ɂ��邽�߂̂���
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
	
	//���C���̕����`�F�b�N
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
	
	
	//�I�𒆂̃J�[�h�ɔz�u����Ă���F�̏ꏊ������ς���
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

	//���C���������������ɉ������F�̍������s��
	public void mix(int direction, int startx, int starty, int endx, int endy){
		switch(direction){
		case 0:
			break;
		case 1://��փh���b�O
			int remain_color_dx = startx;
			int remain_color_dy = starty + 1;
			color1 = world.color_fields[startx][starty];
			color2 = world.color_fields[startx + 1][starty];
			color3 = world.color_fields[endx][endy + 1];
			color4 = world.color_fields[endx + 1][endy + 1];
			//���C���������ꂽ���̐F�ƃJ�[�h��������
			for (int i = starty; i > endy; i--){
				world.color_fields[startx][i] = 100;
				world.color_fields[startx + 1][i] = 100;
				world.card_fields[startx / 2][i / 2] = true;
			}
			//������ɐF��z�u
			world.color_fields[endx][endy + 1] = world.color_fields[remain_color_dx][remain_color_dy];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_dx + 1][remain_color_dy];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		case 2://�E�փh���b�O
			int remain_color_lx = startx;
			int remain_color_ly = starty;
			color1 = world.color_fields[startx + 1][starty];
			color2 = world.color_fields[startx + 1][starty + 1];
			color3 = world.color_fields[endx][endy];
			color4 = world.color_fields[endx][endy + 1];
			//���C���������ꂽ���̐F�ƃJ�[�h������
			for (int i = startx + 1; i < endx; i++){
				world.color_fields[i][starty] = 100;
				world.color_fields[i][starty + 1] = 100;
				world.card_fields[i / 2][starty / 2] = true;
			}
			//������ɐF��z�u
			world.color_fields[endx][endy] = world.color_fields[remain_color_lx][remain_color_ly];
			world.color_fields[endx][endy + 1] = world.color_fields[remain_color_lx][remain_color_ly + 1];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		case 3://���փh���b�O
			int remain_color_ux = startx;
			int remain_color_uy = starty;
			color1 = world.color_fields[startx][starty + 1];
			color2 = world.color_fields[startx + 1][starty + 1];
			color3 = world.color_fields[endx][endy];
			color4 = world.color_fields[endx + 1][endy];
			//���C���������ꂽ���̐F�ƃJ�[�h������
			for (int i = starty + 1; i < endy; i++){
				world.color_fields[startx][i] = 100;
				world.color_fields[startx + 1][i] = 100;
				world.card_fields[startx / 2][i / 2] = true;
			}
			//������ɐF��z�u
			world.color_fields[endx][endy] = world.color_fields[remain_color_ux][remain_color_uy];
			world.color_fields[endx + 1][endy] = world.color_fields[remain_color_ux + 1][remain_color_uy];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		case 4://���փh���b�O
			int remain_color_rx = startx + 1;
			int remain_color_ry = starty;
			color1 = world.color_fields[startx][starty];
			color2 = world.color_fields[startx][starty + 1];
			color3 = world.color_fields[endx + 1][endy];
			color4 = world.color_fields[endx + 1][endy + 1];
			//���C���������ꂽ���̐F�ƃJ�[�h������
			for (int i = startx; i > endx; i--){
				world.color_fields[i][starty] = 100;
				world.color_fields[i][starty + 1] = 100;
				world.card_fields[i / 2][starty / 2] = true;
			}
			//������ɐF��z�u
			world.color_fields[endx + 1][endy] = world.color_fields[remain_color_rx][remain_color_ry];
			world.color_fields[endx + 1][endy + 1] = world.color_fields[remain_color_rx][remain_color_ry + 1];
			world.card_fields[endx / 2][endy / 2] = false;
			break;
		default:
			break;
		}
		
		//�������F�̃`�F�b�N�i�c��F�̐��X�V�̂��߁j
		vanish_color_check(color1, color2, color3, color4);

		//�X�^�[�g�n�_�̐F�ƃJ�[�h������
		world.color_fields[startx][starty] = 100;
		world.color_fields[startx + 1][starty] = 100;
		world.color_fields[startx][starty + 1] = 100;
		world.color_fields[startx + 1][starty + 1] = 100;
		world.card_fields[startx / 2][starty / 2] = true;
		
		//���������ł��P�_����
		world.score += 1;

		//���F�`�F�b�N�i���F�ŃX�R�A�l���i�A���ŘA�����_���������j�j
		same_color_check(endx, endy);


	}

	//�������F�̃`�F�b�N�����Ďc��F�̃J�E���g������������
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
	
	//���F�����`�F�b�N�B�A�����_���������B���Z�b�g�J�E���g�X�V�B���F�Ŏ��ԏ�������
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
				world.score = world.score + world.renzoku * 10;
			}
		}else{
			world.renzoku = 0;
		}
	}
	
	//�X�R�A�̐�����f�ނŕ\���ł���悤�ɂ���
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

	//�F�J�E���g�̐�����f�ނŕ\���ł���悤�ɂ���
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
	
	//�����̃J�E���g�̐�����f�ނŕ\���ł���悤�ɂ���
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
	
	//�^�C�����~�b�g�p�̐����f�ނ�\���ł���悤�ɂ���
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