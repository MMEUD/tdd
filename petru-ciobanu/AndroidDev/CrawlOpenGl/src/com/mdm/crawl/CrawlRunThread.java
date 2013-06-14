package com.mdm.crawl;

import android.graphics.Canvas;

public class CrawlRunThread extends Thread {
    private CrawlSurface panel;
    private boolean run = false;

    public CrawlRunThread(CrawlSurface pan) {
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