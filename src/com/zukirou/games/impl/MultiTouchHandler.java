package com.zukirou.games.impl;

import java.util.ArrayList;
import java.util.List;
import android.view.MotionEvent;
import android.view.View;

import com.zukirou.gameFrameWork.Input.TouchEvent;
import com.zukirou.gameFrameWork.Pool;
import com.zukirou.gameFrameWork.Pool.PoolObjectFactory;


public class MultiTouchHandler implements TouchHandler {
	boolean[] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];
	float[] touchXf = new float[20];
	float[] touchYf = new float[20];
	
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	public MultiTouchHandler(View view, float scaleX, float scaleY){
		PoolObjectFactory<TouchEvent> factory =
				new PoolObjectFactory<TouchEvent>(){
			@Override
			public TouchEvent createObject(){
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event){
		synchronized(this){
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex =
					(event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >>
					MotionEvent.ACTION_POINTER_ID_SHIFT;
					int pointerId = event.getPointerId(pointerIndex);
					TouchEvent touchEvent;
					
					switch(action){
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_POINTER_DOWN:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_DOWN;
						touchEvent.pointer = pointerId;
						touchEvent.x = touchX[pointerId] =
								(int) (event.getX(pointerIndex) * scaleX);
						touchEvent.y = touchY[pointerId] =
								(int) (event.getY(pointerIndex) * scaleY);
						touchEvent.xf = touchXf[pointerId] =
								(float) (event.getX(pointerIndex) * scaleX);
						touchEvent.yf = touchYf[pointerId] =
								(float) (event.getY(pointerIndex) * scaleY);
						isTouched[pointerId] = true;//falseになってた。
						touchEventsBuffer.add(touchEvent);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_POINTER_UP:
					case MotionEvent.ACTION_CANCEL:
						touchEvent = touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_UP;
						touchEvent.pointer = pointerId;
						touchEvent.x = touchX[pointerId] =
								(int)(event.getX(pointerIndex) * scaleX);//getYになってた
						touchEvent.y = touchY[pointerId] =
								(int)(event.getY(pointerIndex) * scaleY);
						touchEvent.xf = touchXf[pointerId] =
								(float)(event.getX(pointerIndex) * scaleX);
						touchEvent.yf = touchYf[pointerId] =
								(float)(event.getY(pointerIndex) * scaleY);
						isTouched[pointerId] = false;
						touchEventsBuffer.add(touchEvent);
						break;
						
					case MotionEvent.ACTION_MOVE:
						int pointerCount = event.getPointerCount();
						for(int i = 0; i < pointerCount; i++){
							pointerIndex = i;
							pointerId = event.getPointerId(pointerIndex);
							
							touchEvent = touchEventPool.newObject();
							touchEvent.type = TouchEvent.TOUCH_DRAGGED;
							touchEvent.pointer = pointerId;
							touchEvent.x = touchX[pointerId] =
									(int) (event.getX(pointerIndex) * scaleX);
							touchEvent.y = touchY[pointerId] =
									(int) (event.getY(pointerIndex) * scaleY);
							touchEvent.xf = touchXf[pointerId] =
									(float) (event.getX(pointerIndex) * scaleX);
							touchEvent.yf = touchYf[pointerId] =
									(float) (event.getY(pointerIndex) * scaleY);
							touchEventsBuffer.add(touchEvent);
						}
						break;
					}
					return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer){
		synchronized(this){
			if(pointer < 0 || pointer >= 20)
				return false;
			else
				return isTouched[pointer];
		}
	}
	
	@Override
	public int getTouchX(int pointer){
		synchronized(this){
			if(pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchX[pointer];
		}
	}
	
	@Override
	public int getTouchY(int pointer){
		synchronized(this){
			if(pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchY[pointer];
		}
	}

	@Override
	public float getTouchXf(int pointer){
		synchronized(this){
			if(pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchXf[pointer];
		}
	}

	@Override
	public float getTouchYf(int pointer){
		synchronized(this){
			if(pointer < 0 || pointer >= 20)
				return 0;
			else
				return touchXf[pointer];
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents(){
		synchronized(this){
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
}