package com.mdm.surface;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class Surface  {

private float x, y;
public View view;
public Bitmap bmp;


public Bitmap getBitmap(){
	return bmp;
}

public void setBitmap(Bitmap bm){
	this.bmp=bm;
}


public float getX() {
    return x;
}

public float getY() {
    return y;
}

public void setX(float x) {
    this.x = x;
    //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
    //params.leftMargin= (int)x;
    
    
}

public void setY(float y) {
    this.y = y;
}

public float getScaleX() {
	// TODO Auto-generated method stub
	return this.getScaleX();
}

public int getWidth() {
	// TODO Auto-generated method stub
	return view.getWidth();
}

public int getHeight() {
	return view.getHeight();
}

public float getRotation() {
	// TODO Auto-generated method stub
	return view.getRotation();
}

public Object getColor() {
	// TODO Auto-generated method stub
	return null;
}

public void setPosition(float f, float g) {
	this.x=f;
	this.y=g;	
}


public void setScale(float f, float g) {
	view.setScaleX(f);
	view.setScaleY(g);
}

public void setRotation(float f) {
	// TODO Auto-generated method stub
	view.setRotation(f);
}

public void setColor(Color c) {
	// TODO Auto-generated method stub
	this.setColor(c);
}

public float getScaleY() {
	// TODO Auto-generated method stub
	return 0;
}
}