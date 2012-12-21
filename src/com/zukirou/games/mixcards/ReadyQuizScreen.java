package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Screen;

public class ReadyQuizScreen extends Screen{
		
	public ReadyQuizScreen(Game game){
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

		g.drawRectLine(86, 175, 136, 30, Color.RED, 5);
		g.drawPixmap(Assets.moji, 5, 70, 0, 0, 320, 35);//Title
		g.drawPixmap(Assets.moji, 90, 180, 0, 36, 130, 25);//PlayQuiz
		g.drawPixmap(Assets.moji, 35, 290, 0, 387, 277, 17);//画面にタッチすると開始します

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