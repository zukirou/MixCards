package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Screen;
import com.zukirou.gameFrameWork.Input.TouchEvent;

public class HighscoreScreen extends Screen{
	String lines[] = new String[5];
	
	public HighscoreScreen(Game game){
		super(game);
		
		for(int i = 0; i < 5; i++){
			lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
		}
	}
	
	@Override
	public void update(float deltaTime){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 0 && event.y > 0){
//					if(Settings.soundEnabled)
//						Assets.click.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime){
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.moji, 110, 40, 0, 116, 100, 22);//Record
		g.drawRectLine(100, 30, 115, 45, Color.YELLOW, 10);
		g.drawPixmap(Assets.moji, 17, 380, 0, 285, 287, 19);//画面にタッチでタイトル戻る


		int y = 100;
		for(int i = 0; i < 5; i++){
			drawLargeNum(g, lines[i], 20, y);
			y += 50;
			g.drawLine(20, y - 30, 300, y - 30, 5, Color.YELLOW);
		}
	}
	
	//スコアの数字を素材で表示できるようにする
	public void drawLargeNum(Graphics g, String line, int x, int y){
		int len = line.length();
		for(int i = 0; i < len; i++){
			char character = line.charAt(i);
			
			if(character == ' '){
				x += 16;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			/*
			if(character == '.'){
				srcX = 200;
				srcWidth = 20;
			}else{
			*/
			srcX = 3 + (character - '0') * 16;			
			srcWidth = 16;
//			}			
			g.drawPixmap(Assets.moji, x, y, srcX, 185, srcWidth, 19);
//			g.drawPixmap(Assets.mainmenu, 105, 415, 86, 45, 196, 45);			
			x += srcWidth;
		}
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