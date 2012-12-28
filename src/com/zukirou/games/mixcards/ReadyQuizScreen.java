package com.zukirou.games.mixcards;

import java.util.List;

import android.graphics.Color;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Screen;

public class ReadyQuizScreen extends Screen{
	static int[] card_idx = new int[49];
	static int[] card_idy = new int[49];
		
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
					game.setScreen(new GameQuizScreen(game));;
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

		g.drawRectLine(85, 65, 135, 35, Color.RED, 5);
		g.drawPixmap(Assets.moji, 90, 70, 0, 36, 130, 25);//PlayQuiz
		g.drawPixmap(Assets.moji01, 115, 120, 0, 113, 70, 18);//操作方法
		g.drawPixmap(Assets.moji01, 25, 145, 0, 132, 249, 15);//カードにタッチして色の位置を変えます
		g.drawPixmap(Assets.moji01, 18, 165, 0, 149, 265, 73);//タッチ図
		g.drawPixmap(Assets.moji01, 25, 250, 0, 224, 248, 15);//カードからカードをなぞって合成します
		g.drawPixmap(Assets.moji01, 54, 265, 0, 239, 192, 74);//合成図
		g.drawPixmap(Assets.moji, 35, 400, 0, 387, 277, 17);//画面にタッチすると開始します

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