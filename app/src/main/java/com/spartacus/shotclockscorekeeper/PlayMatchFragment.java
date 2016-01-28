package com.spartacus.shotclockscorekeeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import software.spartacus.com.shotclockscorekeeper.R;

public class PlayMatchFragment extends Fragment {

    private static final int SHOT_CLOCK_SECONDS = 30;
    private static final int SHOT_CLOCK_AFTER_BREAK_SECONDS = 60;

    private static CountDownTimer countDownTimer = null;

    private Match match;
    private String breakPlayerId;

    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    private TextView textViewPlayer1Name;
    private TextView textViewPlayer2Name;
    private MatchClient client;

    public static PlayMatchFragment newInstance(Match match) {
        Bundle args = new Bundle();
        args.putParcelable("match", match);

        PlayMatchFragment fragment = new PlayMatchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        client = new MatchClient(getContext(), this);

        match = getArguments().getParcelable("match");
        playerOneScore = match.getPlayerOne().getGamesOnTheWire();
        playerTwoScore = match.getPlayerTwo().getGamesOnTheWire();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_play_match, container, false);

        textViewPlayer1Name = (TextView) view.findViewById(R.id.textViewPlayer1Name);
        textViewPlayer2Name = (TextView) view.findViewById(R.id.textViewPlayer2Name);

        final TextView textViewTimer = (TextView) view.findViewById(R.id.textViewTimer);
        ScoreTextView scoreTextViewPlayer1 = (ScoreTextView) view.findViewById(R.id.scoreTextViewPlayer1);
        ScoreTextView scoreTextViewPlayer2 = (ScoreTextView) view.findViewById(R.id.scoreTextViewPlayer2);

        textViewPlayer1Name.setText(match.getPlayerOne().getDisplayName());
        textViewPlayer2Name.setText(match.getPlayerTwo().getDisplayName());

        scoreTextViewPlayer1.setScore(playerOneScore);
        scoreTextViewPlayer1.setListener(new ScoreTextView.Listener() {
            @Override
            public void onScoreChanged(int score) {
                playerOneScore = score;
                startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);
                updateScore();

            }
        });

        scoreTextViewPlayer2.setScore(playerTwoScore);
        scoreTextViewPlayer2.setListener(new ScoreTextView.Listener() {
            @Override
            public void onScoreChanged(int score) {
                playerTwoScore = score;
                startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);
                updateScore();
            }
        });

        updateScore();
        startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);

        if (textViewTimer != null) {
            textViewTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(textViewTimer.getText().toString()) == SHOT_CLOCK_AFTER_BREAK_SECONDS) {
                        countDownTimer.start();
                    } else {
                        cancelAndCreateCountDownTimer(SHOT_CLOCK_SECONDS * 1000 + 500, 500, textViewTimer);
                    }
                }
            });
        }

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (breakPlayerId == null && getChildFragmentManager().findFragmentByTag("TAG") == null) {
            DialogFragment dialogFragment = CoinFlipDialogFragment.newInstance(match);
            dialogFragment.setTargetFragment(this, 100);
            dialogFragment.show(getChildFragmentManager(), "TAG");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            breakPlayerId = data.getStringExtra(CoinFlipDialogFragment.COIN_FLIP_WINNER_PLAYER_ID);
            updateBreakIndicator();
        }
    }

    private void playerExtension(View view, TextView textViewTimer) {
        if (textViewTimer != null) {
            TextView textViewPlayerName = (TextView) view;

            int usedButtonBackgroundColor = Color.parseColor("#e50000");
            textViewPlayerName.setBackgroundColor(usedButtonBackgroundColor);
            textViewPlayerName.setEnabled(false);

            long secondsRemaining = Integer.parseInt(textViewTimer.getText().toString());
            cancelAndCreateCountDownTimer(SHOT_CLOCK_SECONDS * 1000 + secondsRemaining * 1000, 500, textViewTimer);
        }
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

                int secondsRemaining = (int) Math.floor(millisUntilFinished / 1000);
                if (secondsRemaining == 0) {
                    int warningTimerColor = Color.parseColor("#e50000");
                    textViewTimer.setBackgroundColor(warningTimerColor);
                }
                else if (secondsRemaining <= 10) {
                    int warningTimerColor = Color.parseColor("#F2EA00");
                    textViewTimer.setBackgroundColor(warningTimerColor);
                } else {
                    int timerColor = Color.parseColor("#00e900");
                    textViewTimer.setBackgroundColor(timerColor);
                }
                textViewTimer.setText(String.valueOf(secondsRemaining));
            }

            public void onFinish() {
            }
        };
    }

    public void updateScore() {
        client.updateScore(match, playerOneScore, playerTwoScore);
    }

    protected void startNewGame(TextView textViewTimer, TextView textViewPlayer1Name, TextView textViewPlayer2Name) {
        if (textViewTimer != null) {
            cancelAndCreateCountDownTimer(SHOT_CLOCK_AFTER_BREAK_SECONDS * 1000, 500, textViewTimer, false);
            int unusedButtonBackgroundColor = Color.parseColor("#005b8d");
            int timerColor = Color.parseColor("#00e900");
            textViewTimer.setBackgroundColor(timerColor);
            textViewPlayer1Name.setBackgroundColor(unusedButtonBackgroundColor);
            textViewPlayer2Name.setBackgroundColor(unusedButtonBackgroundColor);
            textViewPlayer1Name.setEnabled(true);
            textViewPlayer2Name.setEnabled(true);
        }

        if (breakPlayerId != null) {
            if (match.getPlayerOne().getId().equals(breakPlayerId)) {
                breakPlayerId = match.getPlayerTwo().getId();
            } else {
                breakPlayerId = match.getPlayerOne().getId();
            }

            updateBreakIndicator();
        }
    }

    private void updateBreakIndicator() {
        if (breakPlayerId == null || textViewPlayer1Name == null || textViewPlayer2Name == null) {
            return;
        }

        if (match.getPlayerOne().getId().equals(breakPlayerId)) {
            textViewPlayer1Name.setAllCaps(true);
            textViewPlayer1Name.setTypeface(Typeface.DEFAULT_BOLD);
            textViewPlayer1Name.setTextColor(Color.WHITE);
            textViewPlayer2Name.setAllCaps(false);
            textViewPlayer2Name.setTypeface(Typeface.DEFAULT);
            textViewPlayer2Name.setTextColor(Color.LTGRAY);
        } else {
            textViewPlayer1Name.setAllCaps(false);
            textViewPlayer1Name.setTypeface(Typeface.DEFAULT);
            textViewPlayer1Name.setTextColor(Color.LTGRAY);
            textViewPlayer2Name.setAllCaps(true);
            textViewPlayer2Name.setTypeface(Typeface.DEFAULT_BOLD);
            textViewPlayer2Name.setTextColor(Color.WHITE);
        }
    }

    public void onDestroy () {
        super.onDestroy();
        client.cancelRequests();
    }
}
