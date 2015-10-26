package software.spartacus.com.shotclockscorekeeper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class ScoreTextView extends TextView implements GestureDetector.OnGestureListener {
    private int score = 0;
    private GestureDetector gestureDetector;
    private Listener listener;

    public interface Listener {
        void onScoreChanged(int score);
    }

    public ScoreTextView(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, this);
    }

    public ScoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gestureDetector = new GestureDetector(context, this);
    }

    public ScoreTextView(Context context, AttributeSet attrs, int defStyle) {
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
        if (event1.getY() > event2.getY()) {
            score++;
        } else {
            score--;
        }

        updateScore();
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
    public boolean onSingleTapUp(MotionEvent event) {
        score++;
        updateScore();
        return true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        updateScore();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void updateScore() {
        setText(String.valueOf(score));

        if (listener != null) {
            listener.onScoreChanged(score);
        }
    }
}
