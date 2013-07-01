package com.mdm.scroll;

import com.mdm.tween.ViewContainer;
import com.mdm.tween.ViewContainerAccessor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;

public class MainActivity extends Activity {

  private TweenManager tweenManager;
  private boolean isAnimationRunning = true;

  private LinearLayout genueHamster;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Setup it
    genueHamster = (LinearLayout) findViewById(R.id.main_cont);
    setTweenEngine();
  }

  /**
   * Initiate the Tween Engine
   */
  private void setTweenEngine() {
    tweenManager = new TweenManager();
    //start animation theread
    setAnimationThread();

    //**Register Accessor, this is very important to do!
    //You need register actually each Accessor, but right now we have global one, which actually suitable for everything.
    Tween.registerAccessor(ViewContainer.class, new ViewContainerAccessor());

  }

  /**
   * Timeout 1 sec after press
   * @param v
   */
  public void startAnimation(View v) {

    //Create object which we will animate
    ViewContainer cont = new ViewContainer();
    //pass our real container
    cont.view = genueHamster;

    ///start animations
    Tween.to(cont, ViewContainerAccessor.POSITION_XY, 0.5f).target(100, 200).ease(Bounce.OUT).delay(1.0f).start(tweenManager);

  }

  /***
   * Thread that should run for update UI via Tween engine
   */
  private void setAnimationThread() {

    new Thread(new Runnable() {
      private long lastMillis = -1;

      public void run() {
        while (isAnimationRunning) {
          if (lastMillis > 0) {
            long currentMillis = System.currentTimeMillis();
            final float delta = (currentMillis - lastMillis) / 1000f;

            /*
            view.post(new Runnable(){
              @Override public void run() {
                  
              }
            });
            */
            /**
             * We run all animation in UI thread instead of using post for each elements.
             */
            runOnUiThread(new Runnable() {

              public void run() {
                tweenManager.update(delta);

              }
            });

            lastMillis = currentMillis;
          } else {
            lastMillis = System.currentTimeMillis();
          }

          try {
            Thread.sleep(1000 / 60);
          } catch (InterruptedException ex) {
          }
        }
      }
    }).start();

  }

  /**
   * Stop animation thread
   */
  private void setAnimationFalse() {
    isAnimationRunning = false;
  }

  /**
   * Make animation thread alive
   */
  private void setAnimationTrue() {
    isAnimationRunning = true;
  }

}
