package com.zukirou.games.mixcards;

public class Cards{
	public int x, y;
	public static final int LEFT_TURN = 10;
	public static final int RIGHT_TURN = -10;
	public int direction = 0;

	public Cards(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void turnLeft(){
		direction = LEFT_TURN;

	}
	
	public void turnRight(){
		direction = RIGHT_TURN;
	}
	
	
}