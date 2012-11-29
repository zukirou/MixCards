package com.zukirou.gameFrameWork;

//import com.zukirou.gameFrameWork.Graphics.PixmapFormat;

public abstract class Screen{
	protected final Game game;
	
	public Screen(Game game){
		this.game = game;
	}
	
	public abstract void update(float deltaTime);
	
	public abstract void present(float deltaTime);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();
}
/*
public class ZukirouGameStartScreen extends Screen{
	Pixmap pcChara;
	int x;
	
	public ZukirouGameStartScreen(Game game){
		super(game);
		pcChara = game.getGraphics().newPixmap("assets/pcChara.png", PixmapFormat.RGB565);
	}
	
	@Override
	public void update(float deltaTime){
		x += 1;
		if(x > 100)
			x = 0;
	}
	
	@Override
	public void present(float deltaTime){
		game.getGraphics().clear(0);
		game.getGraphics().drawPixmap(	pcChara, x, 0, 0, 0,
										pcChara.getWidth(), pcChara.getHeight());
	}
	
	@Override
	public void pause(){
		
	}
	
	@Override
	public void resume(){
		
	}
	
	@Override
	public void dispose(){
		pcChara.dispose();
	}
}
*/