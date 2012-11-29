package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Screen;

public class ReadyScreen extends Screen{
	static int[] card_idx = new int[49];
	static int[] card_idy = new int[49];
	
	public ReadyScreen(Game game){
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
					game.setScreen(new GameScreen(game));;
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
		g.drawText("Ready", 100, 100, 20, Color.MAGENTA);
		
//		for(int i = 20; i < 300; i += 40){
//			for(int j = 80; j < 360; j += 40){
//						g.drawPixmap(Assets.card, i, j);												
//			}
//		}
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