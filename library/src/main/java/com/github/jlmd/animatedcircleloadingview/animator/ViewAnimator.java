package com.github.jlmd.animatedcircleloadingview.animator;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.github.jlmd.animatedcircleloadingview.component.ComponentViewAnimation;
import com.github.jlmd.animatedcircleloadingview.component.SideArcsView;
import com.github.jlmd.animatedcircleloadingview.component.finish.FinishedFailureView;
import com.github.jlmd.animatedcircleloadingview.component.finish.FinishedOkView;

/**
 * @author jlmd
 */
public class ViewAnimator implements ComponentViewAnimation.StateListener {

  private SideArcsView sideArcsView;
  private FinishedOkView finishedOkView;
  private FinishedFailureView finishedFailureView;
  private AnimationState finishedState;
  private boolean resetAnimator;
  private AnimatedCircleLoadingView.AnimationListener animationListener;

  public void setComponentViewAnimations(SideArcsView sideArcsView,
      FinishedOkView finishedOkCircleView, FinishedFailureView finishedFailureView) {
    this.sideArcsView = sideArcsView;
    this.finishedOkView = finishedOkCircleView;
    this.finishedFailureView = finishedFailureView;
    initListeners();
  }

  private void initListeners() {
    sideArcsView.setStateListener(this);
    finishedOkView.setStateListener(this);
    finishedFailureView.setStateListener(this);
  }

  public void startAnimator() {
    finishedState = null;
    onStartAnimation();
  }

  public void stopAnimator() {
    finishedState = null;
    sideArcsView.hideView();
    finishedOkView.hideView();
    finishedFailureView.hideView();
    resetAnimator = true;
  }

  public void resetAnimator() {
    sideArcsView.hideView();
    finishedOkView.hideView();
    finishedFailureView.hideView();
    resetAnimator = true;
    startAnimator();
  }

  public void finishOk() {
    finishedState = AnimationState.FINISHED_OK;
  }

  public void finishFailure() {
    finishedState = AnimationState.FINISHED_FAILURE;
  }

  public void setAnimationListener(AnimatedCircleLoadingView.AnimationListener animationListener) {
    this.animationListener = animationListener;
  }

  @Override
  public void onStateChanged(AnimationState state) {
    if (resetAnimator) {
      resetAnimator = false;
    } else {
      switch (state) {
        case SIDE_ARCS_RESIZED_TOP:
          onStartAnimation();
          break;
        case FINISHED_OK:
          onFinished(state);
          break;
        case FINISHED_FAILURE:
          onFinished(state);
          break;
        case ANIMATION_END:
          onAnimationEnd();
          break;
        default:
          break;
      }
    }
  }

  private void onStartAnimation() {
    if (finishedState == AnimationState.FINISHED_OK) {
      sideArcsView.hideView();
      finishedOkView.showView();
      finishedOkView.startScaleAnimation();
    } else if (finishedState == AnimationState.FINISHED_FAILURE) {
      sideArcsView.hideView();
      finishedFailureView.showView();
      finishedFailureView.startScaleAnimation();
    } else {
      sideArcsView.hideView();
      sideArcsView.startRotateAnimation();
      sideArcsView.startResizeDownAnimation();
    }
  }

  private boolean isAnimationFinished() {
    return finishedState != null;
  }

  private void onFinished(AnimationState state) {
    finishedState = state;
  }

  private void onAnimationEnd() {
    if (animationListener != null) {
      boolean success = finishedState == AnimationState.FINISHED_OK;
      animationListener.onAnimationEnd(success);
    }
  }
}