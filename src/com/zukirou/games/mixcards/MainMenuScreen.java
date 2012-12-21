package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Screen;

public class MainMenuScreen extends Screen{
	int menunum;	
	public MainMenuScreen(Game game){
		super(game);
	}
	@Override
	public void update(float deltaTime){
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		
		
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			if(event.type == TouchEvent.TOUCH_UP){
/*				
				if(inBounds(event, 0, g.getHeight() - 64, 64, 64)){
					Settings.soundEnabled = !Settings.soundEnabled;
					if(Settings.soundEnabled)
						Assets.click.play(1);
				}
*/				
				if(inBounds(event, 85, 175, 135, 30)){//PlayQuiz
					game.setScreen(new ReadyQuizScreen(game));
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					return;
				}
				if(inBounds(event, 55, 225, 210, 40)){//PlayEndurance
					game.setScreen(new ReadyScreen(game));
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					return;
				}
				if(inBounds(event, 60, 280, 193, 27)){//PlayTimeLimit
					game.setScreen(new ReadyTimeLimitScreen(game));
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					return;
				}
/*				
				if(inBounds(event, 64, 220 + 42, 192, 42)){
					vsFlag = 0;
					game.setScreen(new HighscoreScreen(game));
					if(Settings.soundEnabled)
						Assets.click.play(1);
					return;
				}
				if(inBounds(event, 64, 220 + 84, 192, 42)){
					vsFlag = 0;
					game.setScreen(new HelpScreen(game));
					if(Settings.soundEnabled)
						Assets.click.play(1);
					return;
				}
				if(inBounds(event, 64, 220 + 168, 192, 42)){
					vsFlag = 1;
					game.setScreen(new ReadyScreen(game));
					if(Settings.soundEnabled)
						Assets.click.play(1);
					return;
				}
*/				
			}
			
		}
	}
	
	private boolean inBounds(TouchEvent event, int x, int y, int width, int height){
		if(event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
			return true;
		else
			return false;
	}	
	
	@Override
	public void present(float deltaTime){
		Graphics g = game.getGraphics();
		

		g.drawPixmap(Assets.background, 0, 0);
		g.drawRectLine(90, 206, 130, 0, Color.RED, 10);
		g.drawRectLine(55, 257, 210, 0, Color.GREEN, 10);
		g.drawRectLine(60, 306, 193, 0, Color.BLUE, 10);
		g.drawRectLine(110, 352, 100, 0, Color.YELLOW, 10);
		g.drawPixmap(Assets.moji, 5, 70, 0, 0, 320, 35);//Title
		g.drawPixmap(Assets.moji, 90, 180, 0, 36, 130, 25);//PlayQuiz
		g.drawPixmap(Assets.moji, 55, 230, 0, 61, 210, 26);//PlayEndurance
		g.drawPixmap(Assets.moji, 60, 280, 0, 87, 193, 27);//PlayTimeLimit
		g.drawPixmap(Assets.moji, 110, 325, 0, 116, 100, 22);//Record
/*		
		if(Settings.soundEnabled)
			g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
		else
			g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
*/			
	}
	
	@Override
	public void pause(){
		Settings.save(game.getFileIO());
	}
	
	@Override
	public void resume(){
		
	}
	
	@Override
	public void dispose(){
		
	}
}