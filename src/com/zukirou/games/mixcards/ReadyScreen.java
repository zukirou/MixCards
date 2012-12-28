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

		g.drawRectLine(50, 65, 215, 35, Color.GREEN, 5);
		g.drawPixmap(Assets.moji, 55, 70, 0, 61, 210, 26);//PlayEndurance
		g.drawPixmap(Assets.moji01, 16, 120, 0, 316, 288, 17);//同色カードをたくさんつくってください
		g.drawPixmap(Assets.moji01, 56, 145, 0, 336, 208, 40);//同色カード図
		g.drawPixmap(Assets.moji01, 25, 195, 0, 132, 249, 15);//カードにタッチして色の位置を変えます
		g.drawPixmap(Assets.moji01, 18, 215, 0, 149, 265, 73);//タッチ図
		g.drawPixmap(Assets.moji01, 25, 300, 0, 224, 248, 15);//カードからカードをなぞって合成します
		g.drawPixmap(Assets.moji01, 54, 315, 0, 239, 192, 74);//合成図

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