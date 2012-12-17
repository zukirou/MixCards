package com.zukirou.games.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Matrix;



import android.view.MotionEvent;
import android.view.View;


import android.graphics.Typeface;

import com.zukirou.gameFrameWork.Graphics;
import com.zukirou.gameFrameWork.Pixmap;
import com.zukirou.gameFrameWork.Input;

public class AndroidGraphics implements Graphics{
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	Matrix matrix = new Matrix();
	
	Typeface font = Typeface.DEFAULT_BOLD;
	String text;
	Path path;
	View view;
	
	float posx = 0f;
	float posy = 0f;
	int posxi;
	int posyi;
	private Bitmap bitmap = null;
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer){
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	
	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format){
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if(format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream in = null;
		Bitmap bitmap = null;
		try{
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if(bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '" + fileName +"'");
		}catch (IOException e){
			throw new RuntimeException("Coukdn't load bitmap from asset '"+ fileName +"'");
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException e){
				}
			}
		}
		if(bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB8888;
		
		return new AndroidPixmap(bitmap, format);
	}
	
	@Override
	public void clear(int color){
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
	}
	
	@Override
	public void drawPixel(int x, int y, int color){
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	
	@Override
	public void drawLine(int x, int y, int x2, int y2, int color){
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height, int color){
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}
	
	@Override
	public void drawRectLine(int x, int y, int width, int height, int color, int strokewidth){
		paint.setColor(color);
		paint.setStrokeWidth(strokewidth);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}
	
	@Override
	public void drawRectLineRotate(int x, int y, int width, int height, int color, int rotate, int strokewidth){
		paint.setColor(color);
		paint.setStrokeWidth(strokewidth);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
//		matrix.postRotate(rotate, x + (width / 2), y + (height / 2));
//		canvas.concat(matrix);
		canvas.rotate(rotate, x + (width / 2) , y + (height / 2));
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}
	
	@Override
	public void drawCircle(int x, int y,int r, int color){
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(x, y, r, paint);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight){
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		
		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;
		
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y){
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}
	
	@Override
	public void drawPixmapRotate(Pixmap pixmap, int d, int x, int y, int image_width, int image_height){		
/*		
		Bitmap image1 = Bitmap.createBitmap(((AndroidPixmap)pixmap).bitmap, x, y, image_width, image_height);
		matrix.postRotate(d, image_width / 2, image_height / 2);
		Bitmap image2 = Bitmap.createBitmap(image1, x, y, image_width, image_height, matrix, true);
		canvas.drawBitmap(image2, matrix, null);
*/

		canvas.save();
		int rx = x + (image_width / 2);
		int ry = y + (image_height / 2);
		canvas.rotate(d, rx, ry);
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
		canvas.restore();
		
/*		
		double rad = deg / 180 * Math.PI;
		double rx = (float)(x * Math.cos(rad) - y * Math.sin(rad));
		double ry = (float)(x * Math.sin(rad) + y * Math.cos(rad));
		double dx= rx - x;
		double dy= ry - y;
		Matrix m = new Matrix();
		m.postRotate((float)rad);
		m.postTranslate((float)dx * 100, (float)dy * 100);		
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, m, null);
*/
	}
	
	@Override
	public void drawText(String text, int x, int y, int textsize, int color){
		paint.setColor(color);
		paint.setTypeface(font);
		paint.setTextSize(textsize);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		canvas.drawText(text, x, y, paint);		
	}

	@Override
	public void drawTextLeft(String text, int x, int y, int textsize, int color){
		paint.setColor(color);
		paint.setTypeface(font);
		paint.setTextSize(textsize);
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setAntiAlias(true);
		canvas.drawText(text, x, y, paint);		
	}
	
	@Override
	public void drawTextRight(String text, int x, int y, int textsize, int color){
		paint.setColor(color);
		paint.setTypeface(font);
		paint.setTextSize(textsize);
		paint.setTextAlign(Paint.Align.RIGHT);
		paint.setAntiAlias(true);
		canvas.drawText(text, x, y, paint);		
	}
	
	@Override
	public void drawFingerLineStart(float x, float y){		
		path = new Path();
		path.moveTo(x, y);
	}
	
	@Override
	public void drawFingerLineMove(float x, float y, float posx, float posy){
		posx += (x - posx) / 1.4;
		posy += (y - posy) / 1.4;
		path.lineTo(posx, posy);
	}
	
	@Override
	public void drawFingerLineEnd(float x, float y){
		path.lineTo(x, y);
	}
	
	@Override
	public void makePath(){
		path = new Path();		
	}
	
	@Override
	public void drawFingerLineStartInt(int x, int y){		
		path = new Path();
		path.moveTo(x, y);
	}
	
	@Override
	public void drawFingerLineMoveInt(int x, int y, int posxi, int posyi){
		posxi += (x - posxi);
		posyi += (y - posyi);
		path.lineTo(posxi, posyi);
	}
	
	@Override
	public void drawFingerLineEndInt(int x, int y){
		path.lineTo(x, y);
	}
	
	@Override
	public void drawFingerLine(){
		/*óéèëÇ´í†Ç∆Ç©viewÇ…èëÇ≠ÇæÇØÇÃÇ∆Ç´ÇÕÅAÇ±ÇÍégÇ¡ÇƒÇ›ÇÈÅB
		view.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		
		if(bitmap != null){
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
*/
		paint.setAntiAlias(true);
		paint.setColor(Color.MAGENTA);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		if(path != null){
			canvas.drawPath(path, paint);
		}
	}
	
	@Override
	public void deleteFingerLine(){
		path = null;
	}
	
	
	@Override
	public int getWidth(){
		return frameBuffer.getWidth();
	}
	
	@Override
	public int getHeight(){
		return frameBuffer.getHeight();
	}
}