package com.zukirou.games.mixcards;

import com.zukirou.gameFrameWork.Game;
import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Screen;
import com.zukirou.gameFrameWork.Graphics.PixmapFormat;

public class LoadingScreen extends Screen{
	public LoadingScreen(Game game){
		super(game);
	}
	
	@Override
	public void update(float deltaTime){
		Graphics g = game.getGraphics();
		Assets.moji	=	g.newPixmap("moji.png", PixmapFormat.ARGB4444);
		Assets.background	=	g.newPixmap("background.png", PixmapFormat.ARGB4444);
		Assets.quiz01	=	g.newPixmap("quiz01.png", PixmapFormat.ARGB4444);
		
		Assets.card		=	g.newPixmap("card.png", PixmapFormat.ARGB4444);
		Assets.red		=	g.newPixmap("red.png", PixmapFormat.ARGB4444);
		Assets.blue		=	g.newPixmap("blue.png", PixmapFormat.ARGB4444);
		Assets.green	=	g.newPixmap("green.png", PixmapFormat.ARGB4444);
		Assets.yellow	=	g.newPixmap("yellow.png", PixmapFormat.ARGB4444);
		Assets.selected_card		=	g.newPixmap("selected_card.png", PixmapFormat.ARGB4444);
		Assets.selected_red			=	g.newPixmap("selected_red.png", PixmapFormat.ARGB4444);
		Assets.selected_blue		=	g.newPixmap("selected_blue.png", PixmapFormat.ARGB4444);
		Assets.selected_green		=	g.newPixmap("selected_green.png", PixmapFormat.ARGB4444);
		Assets.selected_yellow		=	g.newPixmap("selected_yellow.png", PixmapFormat.ARGB4444);
		
		Settings.load(game.getFileIO());
		game.setScreen(new MainMenuScreen(game));
	}
	
	@Override
	public void present(float deltaTime){
		
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