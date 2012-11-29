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
	public int color_count = WORLD_WIDTH * WORLD_HEIGHT / 4;
	public int color_num = 1;

	public int color_i;
	public int color_j;


	
	public SelectedColors selectedcolors;	
	public boolean gameOver = false;
	int color_fields[][] = new int[WORLD_WIDTH][WORLD_HEIGHT];// = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
//	boolean color_fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	boolean card_fields[][] = new boolean[WORLD_WIDTH / 2][WORLD_WIDTH / 2];
	Random random = new Random();
	
	float tickTime = 0;
	public float tick = TICK_INITIAL;
	
	public World_mixcards(){
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
		card_fields[3][3] = true;//消去テスト
	}
	
	private void placeColor(){
		for(int i = 0; i < WORLD_WIDTH; i++){
			for(int j = 0; j < WORLD_HEIGHT; j++){
				color_fields[i][j] = 0;
			}
		}

		while(true){
			if (color_count  < 0){
				break;
			}
			while(true){
				color_i = random.nextInt(WORLD_WIDTH);
				color_j = random.nextInt(WORLD_HEIGHT);
				
				//カード単位で初期配置場所チェック（ななめ配置のみOK。上下左右に並ばないようにする）
				if(color_i % 2 == 0 && color_j % 2 == 0){//左上
					color_fields[color_i + 1][color_j] = 1;
					color_fields[color_i][color_j + 1] = 1;
				}
				if(color_i %2 == 0 && color_j % 2 != 0){//左下
					color_fields[color_i][color_j - 1] = 1;
					color_fields[color_i + 1][color_j] = 1;
				}
				if(color_i % 2 != 0 && color_j % 2 == 0){//右上
					color_fields[color_i - 1][color_j] = 1;
					color_fields[color_i][color_j + 1] = 1;
				}
				if(color_i % 2 != 0 && color_j % 2 != 0){//右下
					color_fields[color_i][color_j -1] = 1;
					color_fields[color_i - 1 ][color_j] = 1;
				}
				
				if(color_fields[color_i][color_j] == 0)
					break;
			}
			color_x[color_i] = color_i;
			color_y[color_j] = color_j;
			color_fields[color_x[color_i]][color_y[color_j]] = 1;			
			color[color_count] = new Colors(color_x[color_i], color_y[color_j], color_num);			
			color_count --;
		}
		
		

	}

}