package com.zukirou.games.mixcards;

import java.util.Random;

public class World_mixcards{
	static final int WORLD_WIDTH = 14;
	static final int WORLD_HEIGHT = 14;
	static final float TICK_INITIAL = 0.5f;
	static final float TICK_DECREMENT = 0.05f;

	public Cards cards;
	public Cards card[] = new Cards[49];
	public int card_x[] = new int[7];
	public int card_y[] = new int[7];
	public int card_count = 49;

	public Colors colors;
	public Colors color[] = new Colors[WORLD_WIDTH * WORLD_HEIGHT];
	public int color_x[] = new int[WORLD_WIDTH];	
	public int color_y[] = new int[WORLD_HEIGHT];
//	public int color_count;
	public int red_count = 49;
	public int green_count = 98;
	public int blue_count = 147;
	public int yellow_count = 195;
//	public int color_num = 1;


	
	public SelectedColors selectedcolors;	
	public boolean gameOver = false;
	int color_fields[][] = new int[WORLD_WIDTH][WORLD_HEIGHT];// = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
//	boolean color_fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	boolean card_fields[][] = new boolean[WORLD_WIDTH / 2][WORLD_WIDTH / 2];
	Random random = new Random();
	
	float tickTime = 0;
	public float tick = TICK_INITIAL;
	
	public World_mixcards(){
		/*
		for(int i = 0; i < WORLD_WIDTH; i++){
			for(int j = 0; j < WORLD_HEIGHT; j++){
				color_fields[i][j] = 0;
			}
		}
		*/
		placeCards();
		placeColor();
//		placeGreen();
//		placeBlue();
	}
	
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
	
	private void placeColor(){
		while(true){	
			if (red_count  < 0){
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
				red_count --;
			}			
		}
		
		while(true){	
			if (green_count  < 50){
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
				color[green_count] = new Colors(color_x[color_i], color_y[color_j], 2);
				green_count --;
			}			
		}		

		while(true){	
			if (blue_count  < 99){
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
				color[blue_count] = new Colors(color_x[color_i], color_y[color_j], 3);
				blue_count --;
			}			
		}		

		while(true){	
			if (yellow_count  < 148){
				break;
			}				
			
			int color_i = random.nextInt(WORLD_WIDTH);
			int color_j = random.nextInt(WORLD_HEIGHT);

			
			if(color_fields[color_i][color_j] == 0 ||
					color_fields[color_i][color_j] == 10 ||
					color_fields[color_i][color_j] == 20 ||
					color_fields[color_i][color_j] == 30){
				//ななめ配置のみOK。上下左右に並ばないようにする				
/*				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					if(color_fields[color_i + 1][color_j] != 1 && color_fields[color_i + 1][color_j] != 2 && color_fields[color_i + 1][color_j] != 3){
						color_fields[color_i + 1][color_j] = 40;											
					}
					if(color_fields[color_i][color_j + 1] != 1 && color_fields[color_i][color_j + 1] != 2 && color_fields[color_i][color_j + 1] != 3){
						color_fields[color_i][color_j + 1] = 40;											
					}
				}else if(color_i %2 == 0 && color_j % 2 != 0){//左下
					if(color_fields[color_i][color_j - 1] != 1 && color_fields[color_i][color_j - 1] != 2 && color_fields[color_i][color_j - 1] != 3){
						color_fields[color_i][color_j - 1] = 40;											
					}
					if(color_fields[color_i + 1][color_j] != 1 && color_fields[color_i + 1][color_j] != 2 && color_fields[color_i + 1][color_j] != 3){
						color_fields[color_i + 1][color_j] = 40;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					if(color_fields[color_i - 1][color_j] != 1 && color_fields[color_i - 1][color_j] != 2 && color_fields[color_i - 1][color_j] != 3){
						color_fields[color_i - 1][color_j] = 40;											
					}
					if(color_fields[color_i][color_j + 1] != 1 && color_fields[color_i][color_j + 1] != 2 && color_fields[color_i][color_j + 1] != 3){
						color_fields[color_i][color_j + 1] = 40;											
					}
				}else if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					if(color_fields[color_i][color_j -1] != 1 && color_fields[color_i][color_j -1] != 2 && color_fields[color_i][color_j -1] != 3){
						color_fields[color_i][color_j -1] = 40;											
					}
					if(color_fields[color_i - 1 ][color_j] != 1 && color_fields[color_i - 1 ][color_j] != 2 && color_fields[color_i - 1 ][color_j] != 3){
						color_fields[color_i - 1 ][color_j] = 40;							
					}
				}			
*/
				color_x[color_i] = color_i;
				color_y[color_j] = color_j;
				color_fields[color_x[color_i]][color_y[color_j]] = 4;			
				color[yellow_count] = new Colors(color_x[color_i], color_y[color_j], 4);
				yellow_count --;
			}			
		}		

	}
	
	private void placeGreen(){
		while(true){
			if (green_count  < 51){
				break;
			}				
			
			int green_i = random.nextInt(WORLD_WIDTH);
			int green_j = random.nextInt(WORLD_HEIGHT);
						
			if(color_fields[green_i][green_j] == 0){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(green_i % 2 == 0 && green_j % 2 == 0){//左上				
					color_fields[green_i + 1][green_j] = 20;					
					color_fields[green_i][green_j + 1] = 20;					
				}else if(green_i %2 == 0 && green_j % 2 != 0){//左下				
					color_fields[green_i][green_j - 1] = 20;					
					color_fields[green_i + 1][green_j] = 20;					
				}else if(green_i % 2 != 0 && green_j % 2 == 0){//右上				
					color_fields[green_i - 1][green_j] = 20;					
					color_fields[green_i][green_j + 1] = 20;					
				}else if(green_i % 2 != 0 && green_j % 2 != 0){//右下				
					color_fields[green_i][green_j -1] = 20;					
					color_fields[green_i - 1 ][green_j] = 20;	
				}			
				color_x[green_i] = green_i;
				color_y[green_j] = green_j;
				color_fields[color_x[green_i]][color_y[green_j]] = 2;			
				color[green_count] = new Colors(color_x[green_i], color_y[green_j], 2);
				green_count --;
			}			
		}		
	}

	private void placeBlue(){
		while(true){
			if (blue_count  < 144){
				break;
			}				
			
			int blue_i = random.nextInt(WORLD_WIDTH);
			int blue_j = random.nextInt(WORLD_HEIGHT);
						
			
			if(color_fields[blue_i][blue_j] == 10){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(blue_i % 2 == 0 && blue_j % 2 == 0){//左上				
					color_fields[blue_i + 1][blue_j] = 30;					
					color_fields[blue_i][blue_j + 1] = 30;					
				}else if(blue_i %2 == 0 && blue_j % 2 != 0){//左下				
					color_fields[blue_i][blue_j - 1] = 30;					
					color_fields[blue_i + 1][blue_j] = 30;					
				}else if(blue_i % 2 != 0 && blue_j % 2 == 0){//右上				
					color_fields[blue_i - 1][blue_j] = 30;					
					color_fields[blue_i][blue_j + 1] = 30;					
				}else if(blue_i % 2 != 0 && blue_j % 2 != 0){//右下				
					color_fields[blue_i][blue_j -1] = 30;					
					color_fields[blue_i - 1 ][blue_j] = 30;	
				}			
				color_x[blue_i] = blue_i;
				color_y[blue_j] = blue_j;
				color_fields[color_x[blue_i]][color_y[blue_j]] = 3;			
				color[green_count] = new Colors(color_x[blue_i], color_y[blue_j], 3);
				blue_count --;
			}			
		}		
	}

	private void placeYellow(){
		while(true){
			if (blue_count  < 130){
				break;
			}				
			
			int blue_i = random.nextInt(WORLD_WIDTH);
			int blue_j = random.nextInt(WORLD_HEIGHT);
			
			if(color_fields[blue_i][blue_j] == 0 || 
					color_fields[blue_i][blue_j] == 10 ||
					color_fields[blue_i][blue_j] == 20){
				//ななめ配置のみOK。上下左右に並ばないようにする				
				if(blue_i % 2 == 0 && blue_j % 2 == 0){//左上				
					color_fields[blue_i + 1][blue_j] = 30;					
					color_fields[blue_i][blue_j + 1] = 30;					
				}else if(blue_i %2 == 0 && blue_j % 2 != 0){//左下				
					color_fields[blue_i][blue_j - 1] = 30;					
					color_fields[blue_i + 1][blue_j] = 30;					
				}else if(blue_i % 2 != 0 && blue_j % 2 == 0){//右上				
					color_fields[blue_i - 1][blue_j] = 30;					
					color_fields[blue_i][blue_j + 1] = 30;					
				}else if(blue_i % 2 != 0 && blue_j % 2 != 0){//右下				
					color_fields[blue_i][blue_j -1] = 30;					
					color_fields[blue_i - 1 ][blue_j] = 30;	
				}			
				color_x[blue_i] = blue_i;
				color_y[blue_j] = blue_j;
				color_fields[color_x[blue_i]][color_y[blue_j]] = 3;			
				color[green_count] = new Colors(color_x[blue_i], color_y[blue_j], 3);
				blue_count --;
			}			
		}		
	}

}