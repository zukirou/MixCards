package com.zukirou.games.mixcards;

public class SelectedColors {
	public int x, y;
	public int color;
	public static final int LEFT_TURN = 0;
	public static final int RIGHT_TURN = 1;
	public int direction;
	
	public SelectedColors(int x, int y, int color){
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