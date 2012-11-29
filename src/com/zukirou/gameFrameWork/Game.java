package com.zukirou.gameFrameWork;

public interface Game{
	public Input getInput();
	
	public FileIO getFileIO();
	
	public Graphics getGraphics();
	
	public Audio getAudio();
	
	public void setScreen(Screen screen);
	
	public Screen getCurrentScreen();
	
	public Screen getStartScreen();
	
}
/*
public class ZukirouGame extends AndroidGame{
	public Screen getStartScreen(){
		return new ZukirouGameStartScreen(this);
	}
}
*/