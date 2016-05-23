package com.spartacus.solitude.match.play;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class SwipeTextView extends TextView implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private OnSwipeListener onSwipeListener;

    public interface OnSwipeListener {
        void onSwipeDown();
        void onSwipeUp();
    }

    public SwipeTextView(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, this);
    }

    public SwipeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gestureDetector = new GestureDetector(context, this);
    }

    public SwipeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.gestureDetector = new GestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (onSwipeListener == null) {
            return false;
        }

        if (event1.getY() > event2.getY()) {
            onSwipeListener.onSwipeUp();
        } else {
            onSwipeListener.onSwipeDown();
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {

    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (onSwipeListener != null) {
            onSwipeListener.onSwipeUp();
        }
        return false;
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        this.onSwipeListener = listener;
    }

}
