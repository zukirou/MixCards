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
	public int color_count = (WORLD_WIDTH * WORLD_HEIGHT) - 1;
//	public int color_count1 = WORLD_WIDTH * WORLD_HEIGHT / 4;
	public int color_num = 1;

	public int red_count = 195;
	public int green_count = 146;
	public int blue_count = 97;
	public int yellow_count = 48;

	public int color_i;
	public int color_j;


	
	public SelectedColors selectedcolors;	
	public boolean gameOver = false;
	public int color_fields[][] = new int[WORLD_WIDTH][WORLD_HEIGHT];// = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
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
//		card_fields[3][3] = true;//消去テスト
	}
	
	private void placeColor(){
//		while(true){
			while(true){//red
				color_i = random.nextInt(WORLD_WIDTH);
				color_j = random.nextInt(WORLD_HEIGHT);
				
				int chk_i = color_i;
				int chk_j = color_j;
				
				//カード単位で初期配置場所チェック（ななめ配置のみOK。同カード内で上下左右に並ばないようにする）
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
					
					color[red_count] = new Colors(color_x[color_i], color_y[color_j], color_num);

					red_count --;

				if(red_count < (WORLD_WIDTH * WORLD_HEIGHT) - 49)
					break;
			}
	
				
			
			

		/*
			while(true){//green
				color_i = random.nextInt(WORLD_WIDTH);
				color_j = random.nextInt(WORLD_HEIGHT);
				
				//カード単位で初期配置場所チェック（ななめ配置のみOK。上下左右に並ばないようにする）
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					color_fields[color_i + 1][color_j] = 2;
					color_fields[color_i][color_j + 1] = 2;
				}
				if(color_i %2 == 0 && color_j % 2 != 0){//左下
					color_fields[color_i][color_j - 1] = 2;
					color_fields[color_i + 1][color_j] = 2;
				}
				if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					color_fields[color_i - 1][color_j] = 2;
					color_fields[color_i][color_j + 1] = 2;
				}
				if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					color_fields[color_i][color_j -1] = 2;
					color_fields[color_i - 1 ][color_j] = 2;
				}
				
				if(color_fields[color_i][color_j] != 1
					|| color_fields[color_i][color_j] != 3
					|| color_fields[color_i][color_j] != 4){
					color_x[color_i] = color_i;
					color_y[color_j] = color_j;
					color_fields[color_x[color_i]][color_y[color_j]] = 2;			
					color[green_count] = new Colors(color_x[color_i], color_y[color_j], color_num + 1);
					green_count --;
					color_count --;
				}
				if(green_count < (WORLD_WIDTH * WORLD_HEIGHT) - 98)
					break;
			}
//			if (color_count < 0 ){
//				break;
//			}
			while(true){//blue
				color_i = random.nextInt(WORLD_WIDTH);
				color_j = random.nextInt(WORLD_HEIGHT);
				
				//カード単位で初期配置場所チェック（ななめ配置のみOK。上下左右に並ばないようにする）
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					color_fields[color_i + 1][color_j] = 30;
					color_fields[color_i][color_j + 1] = 30;
				}
				if(color_i %2 == 0 && color_j % 2 != 0){//左下
					color_fields[color_i][color_j - 1] = 30;
					color_fields[color_i + 1][color_j] = 30;
				}
				if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					color_fields[color_i - 1][color_j] = 30;
					color_fields[color_i][color_j + 1] = 30;
				}
				if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					color_fields[color_i][color_j -1] = 30;
					color_fields[color_i - 1 ][color_j] = 30;
				}
				
				if(color_fields[color_i][color_j] != 1
					|| color_fields[color_i][color_j] != 2
					|| color_fields[color_i][color_j] != 4){
					color_x[color_i] = color_i;
					color_y[color_j] = color_j;
					color_fields[color_x[color_i]][color_y[color_j]] = 3;			
					color[blue_count] = new Colors(color_x[color_i], color_y[color_j], color_num + 2);
					blue_count --;
					color_count --;
				}
				if(blue_count < (WORLD_WIDTH * WORLD_HEIGHT) - 147)
					break;
			}
//			if (color_count < 0 ){
//				break;
//			}
			while(true){
				color_i = random.nextInt(WORLD_WIDTH);
				color_j = random.nextInt(WORLD_HEIGHT);
				
				//カード単位で初期配置場所チェック（ななめ配置のみOK。上下左右に並ばないようにする）
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					color_fields[color_i + 1][color_j] = 40;
					color_fields[color_i][color_j + 1] = 40;
				}
				if(color_i %2 == 0 && color_j % 2 != 0){//左下
					color_fields[color_i][color_j - 1] = 40;
					color_fields[color_i + 1][color_j] = 40;
				}
				if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					color_fields[color_i - 1][color_j] = 40;
					color_fields[color_i][color_j + 1] = 40;
				}
				if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					color_fields[color_i][color_j -1] = 40;
					color_fields[color_i - 1 ][color_j] = 40;
				}
				
				if(color_fields[color_i][color_j] != 1
					|| color_fields[color_i][color_j] != 2
					|| color_fields[color_i][color_j] != 3){
					color_x[color_i] = color_i;
					color_y[color_j] = color_j;
					color_fields[color_x[color_i]][color_y[color_j]] = 4;			
					color[yellow_count] = new Colors(color_x[color_i], color_y[color_j], color_num + 3);
					yellow_count --;
					color_count --;
				}
				if(yellow_count < (WORLD_WIDTH * WORLD_HEIGHT) - 196)
					break;
			}
			*/
//			if (color_count < 0 ){
//				break;
//			}
//		}
	}	
}