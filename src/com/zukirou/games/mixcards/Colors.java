package com.zukirou.games.mixcards;

public class Colors {
	public static final int Blanc = 0;
	public static final int Red = 1;
	public static final int Green = 2;
	public static final int Blue = 3;
	public static final int Yellow = 4;
	public int x, y;
	public int color;
	public static final int LEFT_TURN = 10;
	public static final int RIGHT_TURN = -10;
	public int direction = 0;

	
	public Colors(int x, int y, int color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public void turnLeft(){
		direction = LEFT_TURN;

	}
	
	public void turnRight(){
		direction = RIGHT_TURN;
	}

}