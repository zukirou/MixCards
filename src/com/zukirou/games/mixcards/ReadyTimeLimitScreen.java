package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Screen;

public class ReadyTimeLimitScreen extends Screen{
	static int[] card_idx = new int[49];
	static int[] card_idy = new int[49];
		
	public ReadyTimeLimitScreen(Game game){
		super(game);
	}
	
	@Override
	public void update(float deltaTime){		
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len ; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 0 && event.x < 320 && event.y < 480 && event.y > 0 ){
					game.setScreen(new GameTimeLimitScreen(game));;
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					return;
				}
			}
		}		
	}
	
	@Override
	public void present(float deltaTime){
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.moji, 5, 70, 0, 0, 320, 35);//Title
		g.drawRectLine(55, 275, 203, 29, Color.BLUE, 5);
		g.drawPixmap(Assets.moji, 60, 280, 0, 87, 193, 27);//PlayTimeLimit

		g.drawPixmap(Assets.moji, 35, 320, 0, 387, 277, 17);//画面にタッチすると開始します

	}
	
	@Override
	public void pause(){
		
	}
	
	@Override
	public void resume(){
		
	}
	
	@Override
	public void dispose(){
		
	}
	
}