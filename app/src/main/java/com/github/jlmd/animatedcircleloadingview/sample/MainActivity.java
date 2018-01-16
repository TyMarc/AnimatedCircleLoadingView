package com.github.jlmd.animatedcircleloadingview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

public class MainActivity extends Activity {

  private AnimatedCircleLoadingView animatedCircleLoadingView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
    startLoading();
    startPercentMockThread();
  }

  private void startLoading() {
    animatedCircleLoadingView.startIndeterminate();
  }

  private void startPercentMockThread() {
    new Handler(getMainLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            animatedCircleLoadingView.stopOk(false);
          }
        });
      }
    }, 10000);
  }
}