package com.zukirou.games.mixcards;

import java.util.Random;

public class World{
	static final int WORLD_WIDTH = 14;
	static final int WORLD_HEIGHT = 14;
	static final float TICK_INITIAL = 0.5f;
	static final float TICK_DECREMENT = 0.05f;
	
	public int score = 0;
	public int renzoku = 0;
	public int samecolor_count = 0;
	public int reset_count = 15;
	public int time_limit = 60;
	public int time_extend = 10;

	public Cards cards;
	public Cards card[] = new Cards[49];
	public int card_x[] = new int[7];
	public int card_y[] = new int[7];
	public int card_count = 49;

	public Colors colors;
	public Colors color[] = new Colors[WORLD_WIDTH * WORLD_HEIGHT];
	public int color_x[] = new int[WORLD_WIDTH];	
	public int color_y[] = new int[WORLD_HEIGHT];
	public int red_count = 0;
	public int green_count = 0;
	public int blue_count = 0;
	public int yellow_count = 0;

	public SelectedColors selectedcolors;	
	public boolean gameOver = false;
	int color_fields[][] = new int[WORLD_WIDTH][WORLD_HEIGHT];
	boolean card_fields[][] = new boolean[WORLD_WIDTH / 2][WORLD_WIDTH / 2];
	Random random = new Random();
	
	float tickTime = 0;
	public float tick = TICK_INITIAL;
	
	public World(){
		placeCards();
		placeColor();
	}
	
	//カードを配置する
	private void placeCards(){
		for(int i = 0; i < WORLD_WIDTH / 2; i++){
			for(int j = 0; j < WORLD_HEIGHT / 2; j++){
				for(int k = 0; k < card_count ; k++){
					card_x[i] = i;
					card_y[j] = j;
					card[k] = new Cards(card_x[i], card_y[j]);										
				}
			}
		}
		card_fields[3][3] = true;//消去テスト
	}
	
	//色を配置する
	private void placeColor(){
		//中央はあけておく
		color_fields[6][6] = 100;
		color_fields[7][6] = 100;
		color_fields[6][7] = 100;		
		color_fields[7][7] = 100;
		while(true){//赤の配置場所

			if (red_count  > 47){
				break;
			}				
			
			int color_i = random.nextInt(WORLD_WIDTH);
			int color_j = random.nextInt(WORLD_HEIGHT);

			
			if(color_fields[color_i][color_j] == 0){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上				
					color_fields[color_i + 1][color_j] = 10;					
					color_fields[color_i][color_j + 1] = 10;					
				}else if(color_i %2 == 0 && color_j % 2 != 0){//左下				
					color_fields[color_i][color_j - 1] = 10;					
					color_fields[color_i + 1][color_j] = 10;					
				}else if(color_i % 2 != 0 && color_j % 2 == 0){//右上				
					color_fields[color_i - 1][color_j] = 10;					
					color_fields[color_i][color_j + 1] = 10;					
				}else if(color_i % 2 != 0 && color_j % 2 != 0){//右下				
					color_fields[color_i][color_j -1] = 10;					
					color_fields[color_i - 1 ][color_j] = 10;	
				}			
				color_x[color_i] = color_i;
				color_y[color_j] = color_j;
				color_fields[color_x[color_i]][color_y[color_j]] = 1;			
				color[red_count] = new Colors(color_x[color_i], color_y[color_j], 1);
				red_count ++;
			}			
		}
		
		while(true){//緑の配置場所	
			if (green_count  > 47){
				break;
			}				
			
			int color_i = random.nextInt(WORLD_WIDTH);
			int color_j = random.nextInt(WORLD_HEIGHT);

			
			if(color_fields[color_i][color_j] == 0 || color_fields[color_i][color_j] == 10){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					if(color_fields[color_i + 1][color_j] != 1){
						color_fields[color_i + 1][color_j] = 20;											
					}
					if(color_fields[color_i][color_j + 1] != 1){
						color_fields[color_i][color_j + 1] = 20;											
					}
				}else if(color_i %2 == 0 && color_j % 2 != 0){//左下
					if(color_fields[color_i][color_j - 1] != 1){
						color_fields[color_i][color_j - 1] = 20;											
					}
					if(color_fields[color_i + 1][color_j] != 1){
						color_fields[color_i + 1][color_j] = 20;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					if(color_fields[color_i - 1][color_j] != 1){
						color_fields[color_i - 1][color_j] = 20;											
					}
					if(color_fields[color_i][color_j + 1] != 1){
						color_fields[color_i][color_j + 1] = 20;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					if(color_fields[color_i][color_j -1] != 1){
						color_fields[color_i][color_j -1] = 20;											
					}
					if(color_fields[color_i - 1 ][color_j] != 1){
						color_fields[color_i - 1 ][color_j] = 20;							
					}
				}			
				color_x[color_i] = color_i;
				color_y[color_j] = color_j;
				color_fields[color_x[color_i]][color_y[color_j]] = 2;			
				color[red_count + green_count] = new Colors(color_x[color_i], color_y[color_j], 2);
				green_count ++;
			}			
		}		

		while(true){//青の配置場所	
			if (blue_count  > 47){
				break;
			}				
			
			int color_i = random.nextInt(WORLD_WIDTH);
			int color_j = random.nextInt(WORLD_HEIGHT);

			
			if(color_fields[color_i][color_j] == 0 ||
					color_fields[color_i][color_j] == 10 ||
					color_fields[color_i][color_j] == 20){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					if(color_fields[color_i + 1][color_j] != 1 && color_fields[color_i + 1][color_j] != 2){
						color_fields[color_i + 1][color_j] = 30;											
					}
					if(color_fields[color_i][color_j + 1] != 1 && color_fields[color_i][color_j + 1] != 2){
						color_fields[color_i][color_j + 1] = 30;											
					}
				}else if(color_i %2 == 0 && color_j % 2 != 0){//左下
					if(color_fields[color_i][color_j - 1] != 1 && color_fields[color_i][color_j - 1] != 2){
						color_fields[color_i][color_j - 1] = 30;											
					}
					if(color_fields[color_i + 1][color_j] != 1 && color_fields[color_i + 1][color_j] != 2){
						color_fields[color_i + 1][color_j] = 30;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					if(color_fields[color_i - 1][color_j] != 1 && color_fields[color_i - 1][color_j] != 2){
						color_fields[color_i - 1][color_j] = 30;											
					}
					if(color_fields[color_i][color_j + 1] != 1&& color_fields[color_i][color_j + 1] != 2){
						color_fields[color_i][color_j + 1] = 30;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					if(color_fields[color_i][color_j -1] != 1 && color_fields[color_i][color_j -1] != 2){
						color_fields[color_i][color_j -1] = 30;											
					}
					if(color_fields[color_i - 1 ][color_j] != 1 && color_fields[color_i - 1 ][color_j] != 2){
						color_fields[color_i - 1 ][color_j] = 30;							
					}
				}			
				color_x[color_i] = color_i;
				color_y[color_j] = color_j;
				color_fields[color_x[color_i]][color_y[color_j]] = 3;			
				color[red_count + green_count + blue_count] = new Colors(color_x[color_i], color_y[color_j], 3);
				blue_count ++;
			}			
		}		

		while(true){//黄色の配置場所。yellowは残りの空間に配置上下左右にならぶことアリ	
			if (yellow_count  > 47){
				break;
			}				
			
			int color_i = random.nextInt(WORLD_WIDTH);
			int color_j = random.nextInt(WORLD_HEIGHT);

			
			if(color_fields[color_i][color_j] == 0 ||
					color_fields[color_i][color_j] == 10 ||
					color_fields[color_i][color_j] == 20 ||
					color_fields[color_i][color_j] == 30){
				color_x[color_i] = color_i;
				color_y[color_j] = color_j;
				color_fields[color_x[color_i]][color_y[color_j]] = 4;			
				color[red_count + green_count + blue_count + yellow_count] = new Colors(color_x[color_i], color_y[color_j], 4);
				yellow_count ++;
			}			
		}		
	}
	
	public void update(float deltaTime){
		if(gameOver)
			return;
		
//		tickTime += deltaTime;
		
		
//		while(tickTime > tick){
			
			if(remain_card_check()){
				gameOver = true;
				return;
				
			}
//		}
		
	}
	
	public boolean remain_card_check(){
		int remain = 0;
		int k = 0;
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				if(card_fields[i][j] == false){
					if(card_fields[i + 1][j] == false){
						remain ++;
					}else if(card_fields[i][j + 1] == false){
						remain ++;
					}
				}				
			}
		}
		if(remain == 0)
			return true;
		return false;
	}
}