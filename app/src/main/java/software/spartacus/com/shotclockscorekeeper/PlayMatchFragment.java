package software.spartacus.com.shotclockscorekeeper;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayMatchFragment extends Fragment implements HttpPutRequestCompleted {

    private static final int SHOT_CLOCK_SECONDS = 30;
    private static final int SHOT_CLOCK_AFTER_BREAK_SECONDS = 60;


    private static CountDownTimer countDownTimer = null;

    private ScoreTextView scoreTextViewPlayer1 = null;
    private ScoreTextView scoreTextViewPlayer2 = null;


    private Match match;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_play_match, container, false);

        final TextView textViewTimer = (TextView) view.findViewById(R.id.textViewTimer);
        final TextView textViewPlayer1Name = (TextView) view.findViewById(R.id.textViewPlayer1Name);
        final TextView textViewPlayer2Name = (TextView) view.findViewById(R.id.textViewPlayer2Name);
        scoreTextViewPlayer1 = (ScoreTextView) view.findViewById(R.id.scoreTextViewPlayer1);
        scoreTextViewPlayer2 = (ScoreTextView) view.findViewById(R.id.scoreTextViewPlayer2);

        match = getArguments().getParcelable("match");

        textViewPlayer1Name.setText(match.getPlayerOne().getDisplayName());
        textViewPlayer2Name.setText(match.getPlayerTwo().getDisplayName());

        scoreTextViewPlayer1.setScore(match.getPlayerOne().getGamesOnTheWire());
        scoreTextViewPlayer2.setScore(match.getPlayerTwo().getGamesOnTheWire());

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

        scoreTextViewPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore();
                startNewGame(textViewTimer, textViewPlayer1Name, textViewPlayer2Name);
            }
        });

        scoreTextViewPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore();
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

        return view;
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
        JSONObject scoreUpdate = new JSONObject();

        try {
            JSONObject player1 = new JSONObject();
            JSONObject player2 = new JSONObject();

            player1.put("id", match.getPlayerOne().getId());
            player1.put("score", scoreTextViewPlayer1.getScore());
            player2.put("id", match.getPlayerTwo().getId());
            player2.put("score", scoreTextViewPlayer2.getScore());
            JSONArray players = new JSONArray();
            players.put(player1);
            players.put(player2);
            scoreUpdate.put("players", players);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpPutRequestTask(this).execute(new String[]{"http://assistant-tournament-director.herokuapp.com/match/" + match.getId(), scoreUpdate.toString()});
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
    }

    @Override
    public void onHttpPutRequestCompleted(Boolean success) {

    }
}
