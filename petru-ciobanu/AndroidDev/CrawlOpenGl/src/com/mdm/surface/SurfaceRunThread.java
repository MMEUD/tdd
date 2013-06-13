package com.mdm.surface;

import android.graphics.Canvas;

import com.mdm.crawl.CrawlSurface;

public class SurfaceRunThread  extends Thread{
	 private SurfaceContainer panel;
	 private boolean run = false;
	 
	 public SurfaceRunThread(SurfaceContainer pan) {
	        panel = pan;
	        
	    }
	 
	  public void setRunning(boolean Run) {
	        run = Run;
	    }

	    public boolean isRunning() {
	        return run;
	    }
	    
	    
	    @Override
	    public void run() {
	        Canvas c;
	        while (run) {
	            c = null;
	            try {
	                c = panel.getHolder().lockCanvas(null);
	                  synchronized (panel.getHolder()) {
	                	panel.updatePhysics(50);
	                	panel.onDraw(c);
	                	
	                }
	            } finally {
	                if (c != null) {
	                    panel.getHolder().unlockCanvasAndPost(c);
	                }
	            }
	        }
	    }    
	}
	 

