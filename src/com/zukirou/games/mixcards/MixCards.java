package com.zukirou.games.mixcards;

import com.zukirou.gameFrameWork.Screen;
import com.zukirou.games.impl.AndroidGame;

public class MixCards extends AndroidGame{
	@Override
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}
}