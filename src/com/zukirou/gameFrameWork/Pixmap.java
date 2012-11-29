package com.zukirou.gameFrameWork;

import com.zukirou.gameFrameWork.Graphics.PixmapFormat;

public interface Pixmap{
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}