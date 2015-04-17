package software.spartacus.com.shotclockscorekeeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PlayMatchActivity extends Activity {
    private static final int SHOT_CLOCK_SECONDS = 40;
    private static final int SHOT_CLOCK_AFTER_BREAK_SECONDS = 60;

    private static CountDownTimer countDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_match);

        final TextView textViewTimer = (TextView)findViewById(R.id.textViewTimer);
        final ScoreTextView scoreTextViewPlayer1 = (ScoreTextView)findViewById(R.id.scoreTextViewPlayer1);
        final ScoreTextView scoreTextViewPlayer2 = (ScoreTextView)findViewById(R.id.scoreTextViewPlayer2);
        final TextView textViewPlayer1Name = (TextView)findViewById(R.id.textViewPlayer1Name);
        final TextView textViewPlayer2Name = (TextView)findViewById(R.id.textViewPlayer2Name);

        getActionBar().hide();

        Intent intent = getIntent();
        textViewPlayer1Name.setText(intent.getStringExtra("player1Name"));
        textViewPlayer2Name.setText(intent.getStringExtra("player2Name"));
        scoreTextViewPlayer1.setText(intent.getStringExtra("player1GamesOnTheWire"));
        scoreTextViewPlayer2.setText(intent.getStringExtra("player2GamesOnTheWire"));

        startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);

        textViewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Timer", "clicked");
                if (Integer.parseInt(textViewTimer.getText().toString()) == SHOT_CLOCK_AFTER_BREAK_SECONDS) {
                    countDownTimer.start();
                } else {
                    cancelAndCreateCountDownTimer(SHOT_CLOCK_SECONDS * 1000 + 500, 500, textViewTimer);
                }
            }
        });

        scoreTextViewPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);
            }
        });

        scoreTextViewPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);
            }
        });

        textViewPlayer1Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerExtension(view, textViewTimer);
            }
        });

        textViewPlayer2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerExtension(view, textViewTimer);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void playerExtension(View view, TextView textViewTimer) {
        TextView textViewPlayerName = (TextView)view;

        int usedButtonBackgroundColor = Color.parseColor("#e50000");
        textViewPlayerName.setBackgroundColor(usedButtonBackgroundColor);
        textViewPlayerName.setEnabled(false);

        long secondsRemaining = Integer.parseInt(textViewTimer.getText().toString());
        cancelAndCreateCountDownTimer(SHOT_CLOCK_SECONDS * 1000 + secondsRemaining * 1000, 500, textViewTimer);
    }

    private void cancelAndCreateCountDownTimer(long millisInFuture, long countDownInterval, TextView textView) {
        cancelAndCreateCountDownTimer(millisInFuture, countDownInterval, textView, true);
    }

    private void cancelAndCreateCountDownTimer(long millisInFuture, long countDownInterval, TextView textView, boolean start) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = createCountDownTimer(millisInFuture, countDownInterval, textView);

        if (start) {
            countDownTimer.start();
        } else {
            textView.setText(String.valueOf(SHOT_CLOCK_AFTER_BREAK_SECONDS));
        }
    }

    private CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval, TextView timer) {
        final TextView textViewTimer = timer;

        return new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {

                int secondsRemaining = (int)Math.floor(millisUntilFinished / 1000);
                if (secondsRemaining <= 10) {
                    int warningTimerColor = Color.parseColor("#e50000");
                    textViewTimer.setBackgroundColor(warningTimerColor);
                } else {
                    int timerColor = Color.parseColor("#00e900");
                    textViewTimer.setBackgroundColor(timerColor);
                }
                textViewTimer.setText(String.valueOf(secondsRemaining));
            }

            public void onFinish() { }
        };
    }

    protected void startNewGame(TextView textViewTimer, TextView textViewPlayer1Name, TextView textViewPlayer2Name) {
        cancelAndCreateCountDownTimer(SHOT_CLOCK_AFTER_BREAK_SECONDS * 1000, 500, textViewTimer, false);
        int unusedButtonBackgroundColor = Color.parseColor("#005b8d");
        textViewPlayer1Name.setBackgroundColor(unusedButtonBackgroundColor);
        textViewPlayer2Name.setBackgroundColor(unusedButtonBackgroundColor);
        textViewPlayer1Name.setEnabled(true);
        textViewPlayer2Name.setEnabled(true);
    }
}
