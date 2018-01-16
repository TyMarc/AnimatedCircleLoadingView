package com.github.jlmd.animatedcircleloadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.github.jlmd.animatedcircleloadingview.animator.ViewAnimator;
import com.github.jlmd.animatedcircleloadingview.component.SideArcsView;
import com.github.jlmd.animatedcircleloadingview.component.finish.FinishedFailureView;
import com.github.jlmd.animatedcircleloadingview.component.finish.FinishedOkView;

/**
 * @author jlmd
 */
public class AnimatedCircleLoadingView extends FrameLayout {

  private static final String DEFAULT_HEX_MAIN_COLOR = "#FF9A00";
  private static final String DEFAULT_HEX_SECONDARY_COLOR = "#BDBDBD";
  private static final String DEFAULT_HEX_TINT_COLOR = "#FFFFFF";
  private static final String DEFAULT_HEX_TEXT_COLOR = "#FFFFFF";
  private final Context context;
  private SideArcsView sideArcsView;
  private FinishedOkView finishedOkView;
  private FinishedFailureView finishedFailureView;
  private ViewAnimator viewAnimator;
  private AnimationListener animationListener;
  private boolean startAnimationIndeterminate;
  private boolean stopAnimationOk;
  private boolean stopAnimationFailure;
  private int mainColor;
  private int secondaryColor;
  private int checkMarkTintColor;
  private int failureMarkTintColor;
  private int textColor;

  public AnimatedCircleLoadingView(Context context) {
    super(context);
    this.context = context;
  }

  public AnimatedCircleLoadingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    initAttributes(attrs);
  }

  public AnimatedCircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    initAttributes(attrs);
  }

  private void initAttributes(AttributeSet attrs) {
    TypedArray attributes =
        getContext().obtainStyledAttributes(attrs, R.styleable.AnimatedCircleLoadingView);
    mainColor = attributes.getColor(R.styleable.AnimatedCircleLoadingView_animCircleLoadingView_mainColor,
        Color.parseColor(DEFAULT_HEX_MAIN_COLOR));
    secondaryColor = attributes.getColor(R.styleable.AnimatedCircleLoadingView_animCircleLoadingView_secondaryColor,
        Color.parseColor(DEFAULT_HEX_SECONDARY_COLOR));
    checkMarkTintColor =
        attributes.getColor(R.styleable.AnimatedCircleLoadingView_animCircleLoadingView_checkMarkTintColor,
            Color.parseColor(DEFAULT_HEX_TINT_COLOR));
    failureMarkTintColor =
        attributes.getColor(R.styleable.AnimatedCircleLoadingView_animCircleLoadingView_failureMarkTintColor,
            Color.parseColor(DEFAULT_HEX_TINT_COLOR));
    textColor = attributes.getColor(R.styleable.AnimatedCircleLoadingView_animCircleLoadingView_textColor,
        Color.parseColor(DEFAULT_HEX_TEXT_COLOR));
    attributes.recycle();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    init();
    startAnimation();
  }

  private void startAnimation() {
    if (getWidth() != 0 && getHeight() != 0) {
      if (startAnimationIndeterminate) {
        viewAnimator.startAnimator();
        startAnimationIndeterminate = false;
      }
      if (stopAnimationOk) {
        stopOk();
      }
      if (stopAnimationFailure) {
        stopFailure();
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // Force view to be a square
    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
  }

  private void init() {
    initComponents();
    addComponentsViews();
    initAnimatorHelper();
  }

  private void initComponents() {
    int width = getWidth();
    sideArcsView = new SideArcsView(context, width, mainColor, secondaryColor);
    finishedOkView =
        new FinishedOkView(context, width, mainColor, secondaryColor, checkMarkTintColor);
    finishedFailureView =
        new FinishedFailureView(context, width, mainColor, secondaryColor, failureMarkTintColor);
  }

  private void addComponentsViews() {
    addView(sideArcsView);
    addView(finishedOkView);
    addView(finishedFailureView);
  }

  private void initAnimatorHelper() {
    viewAnimator = new ViewAnimator();
    viewAnimator.setAnimationListener(animationListener);
    viewAnimator.setComponentViewAnimations(
            sideArcsView, finishedOkView, finishedFailureView);
  }

  public void startIndeterminate() {
    startAnimationIndeterminate = true;
    startAnimation();
  }

  public void stopOk() {
    if (viewAnimator == null) {
      stopAnimationOk = true;
    } else {
      viewAnimator.finishOk();
    }
  }

  public void stopFailure() {
    if (viewAnimator == null) {
      stopAnimationFailure = true;
    } else {
      viewAnimator.finishFailure();
    }
  }

  public void resetLoading() {
    if (viewAnimator != null) {
      viewAnimator.resetAnimator();
    }
  }

  public void stopAnimation() {
    if (viewAnimator != null) {
      viewAnimator.stopAnimator();
    }
  }

  public void setAnimationListener(AnimationListener animationListener) {
    this.animationListener = animationListener;
  }

  public interface AnimationListener {

    void onAnimationEnd(boolean success);
  }
}
