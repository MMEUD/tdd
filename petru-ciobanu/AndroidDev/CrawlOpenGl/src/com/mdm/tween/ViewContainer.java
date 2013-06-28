package com.mdm.tween;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

public class ViewContainer {
  private float x, y;
  public View view;
  
  public float getX() {
      return x;
  }
  
  public float getY() {
      return y;
  }

  public void setX(float x) {
      this.x = x;
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
      params.leftMargin= (int)x;
      view.setLayoutParams(params);
  }

  public void setY(float y) {
      this.y = y;
     
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
      params.topMargin = (int)y;
      view.setLayoutParams(params);  
  }

public float getScaleX() {
	// TODO Auto-generated method stub
	return view.getScaleX();
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
	// TODO Auto-generated method stub
	view.setX(f);
	view.setY(g);
}

public void setScale(float f, float g) {
	// TODO Auto-generated method stub
	view.setScaleX(f);
	view.setScaleY(g);
}

public void setRotation(float f) {
	// TODO Auto-generated method stub
	view.setRotation(f);
}

public void setColor(Color c) {
	// TODO Auto-generated method stub
}
}