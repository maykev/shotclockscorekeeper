package com.spartacus.solitude.match.play;


import android.databinding.Bindable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.SolitudeService;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchPlayer;
import com.spartacus.solitude.model.MatchUpdate;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.utils.SubscriptionUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

public class PlayMatchViewModel extends ViewModel {

    public interface Listener {
        void onCheckMatchWinner(Match match, MatchPlayer player);
        void onFinished();
    }

    private static final int SHOT_CLOCK_SECONDS = 30;
    private static final int SHOT_CLOCK_AFTER_BREAK_SECONDS = 60;

    private final Listener listener;
    private final Match match;
    private final int table;
    private final SolitudeService service;

    // State
    private int playerOneScore;
    private boolean isPlayerOneExtended;
    private int playerTwoScore;
    private boolean isPlayerTwoExtended;
    private int timeRemaining;
    private boolean isPlayerOneTurn;
    private boolean isMatchFinished;
    private boolean isExtensionsAllowed;

    // Subscriptions
    private Subscription timerSubscription;

    // Score Callbacks
    private final SwipeTextView.OnSwipeListener playerOneScoreListener;
    private final SwipeTextView.OnSwipeListener playerTwoScoreListener;

    public PlayMatchViewModel(Listener listener, Match match, int lagWinnerId, int table) {
        this.listener = listener;
        this.match = match;
        this.table = table;
        this.playerOneScore = match.getPlayerOne().getGamesOnTheWire();
        this.playerTwoScore = match.getPlayerTwo().getGamesOnTheWire();
        this.service = SolitudeApp.getInstance().getService();

        this.playerTwoScoreListener = new SwipeTextView.OnSwipeListener() {
            @Override
            public void onSwipeDown() {
                updatePlayerScore(PlayMatchViewModel.this.match.getPlayerTwo(), playerTwoScore - 1);
            }

            @Override
            public void onSwipeUp() {
                updatePlayerScore(PlayMatchViewModel.this.match.getPlayerTwo(), playerTwoScore + 1);
            }

        };

        this.playerOneScoreListener = new SwipeTextView.OnSwipeListener() {
            @Override
            public void onSwipeDown() {
                updatePlayerScore(PlayMatchViewModel.this.match.getPlayerOne(), playerOneScore - 1);
            }

            @Override
            public void onSwipeUp() {
                updatePlayerScore(PlayMatchViewModel.this.match.getPlayerOne(), playerOneScore + 1);
            }
        };

        startNewGame();
        isPlayerOneTurn = lagWinnerId == match.getPlayerOne().getId();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("playerOneScore", playerOneScore);
        outState.putInt("playerTwoScore", playerTwoScore);
        outState.putInt("timeRemaining", timeRemaining);
        outState.putBoolean("isPlayerOneTurn", isPlayerOneTurn);
        outState.putBoolean("isPlayerOneExtended", isPlayerOneExtended);
        outState.putBoolean("isPlayerTwoExtended", isPlayerTwoExtended);
        outState.putBoolean("isMatchFinished", isMatchFinished);
        outState.putBoolean("isExtensionsAllowed", isExtensionsAllowed);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        playerOneScore = savedInstanceState.getInt("playerOneScore");
        playerTwoScore = savedInstanceState.getInt("playerTwoScore");
        timeRemaining = savedInstanceState.getInt("timeRemaining");
        isPlayerOneTurn = savedInstanceState.getBoolean("isPlayerOneTurn");
        isPlayerOneExtended = savedInstanceState.getBoolean("isPlayerOneExtended");
        isPlayerTwoExtended = savedInstanceState.getBoolean("isPlayerTwoExtended");
        isMatchFinished = savedInstanceState.getBoolean("isMatchFinished");
        isExtensionsAllowed = savedInstanceState.getBoolean("isExtensionsAllowed");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (timeRemaining != SHOT_CLOCK_SECONDS && timeRemaining != SHOT_CLOCK_AFTER_BREAK_SECONDS) {
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Bindable
    public String getPlayerOneName() {
        return match.getPlayerOne().getDisplayName();
    }

    @Bindable
    public int getPlayerOneScore() {
        return playerOneScore;
    }

    @Bindable
    public boolean isPlayerOneExtended() {
        return isPlayerOneExtended;
    }

    @Bindable
    public String getPlayerTwoName() {
        return match.getPlayerTwo().getDisplayName();
    }

    @Bindable
    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    @Bindable
    public boolean isPlayerTwoExtended() {
        return isPlayerTwoExtended;
    }

    @Bindable
    public boolean isPlayerOneTurn() {
        return isPlayerOneTurn;
    }

    @Bindable
    public int getTimeRemaining() {
        return timeRemaining;
    }

    private void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = Math.min(timeRemaining, SHOT_CLOCK_AFTER_BREAK_SECONDS + SHOT_CLOCK_SECONDS);
        notifyPropertyChanged(BR.timeRemaining);
    }

    public SwipeTextView.OnSwipeListener getPlayerOneScoreListener() {
        return playerOneScoreListener;
    }

    public SwipeTextView.OnSwipeListener getPlayerTwoScoreListener() {
        return playerTwoScoreListener;
    }


    public void onExtendPlayerOneClicked(View view) {
        if (!isPlayerOneExtended && isExtensionsAllowed) {
            isPlayerOneExtended = true;
            isExtensionsAllowed = false;
            notifyPropertyChanged(BR.playerOneExtended);


            stopTimer();
            setTimeRemaining(getTimeRemaining() + SHOT_CLOCK_SECONDS);
            startTimer();
        }
    }

    public void onExtendPlayerTwoClicked(View view) {
        if (!isPlayerTwoExtended && isExtensionsAllowed) {
            isPlayerTwoExtended = true;
            isExtensionsAllowed = false;
            notifyPropertyChanged(BR.playerTwoExtended);

            stopTimer();
            setTimeRemaining(getTimeRemaining() + SHOT_CLOCK_SECONDS);
            startTimer();
        }
    }

    public void onTimerClicked(View view) {
        stopTimer();

        if (timeRemaining != SHOT_CLOCK_AFTER_BREAK_SECONDS) {
            setTimeRemaining(SHOT_CLOCK_SECONDS);
        }

        isExtensionsAllowed = true;

        startTimer();
    }

    private void sendScoreUpdate() {
        if (match.getMatchType() == Match.FREE_PLAY_MATCH) {
            return;
        }

        final MatchUpdate update = new MatchUpdate.Builder()
                .setPlayerScore(match.getPlayerOne(), playerOneScore)
                .setPlayerScore(match.getPlayerTwo(), playerTwoScore)
                .setTable(table)
                .setStatus(isMatchFinished ? Match.STATUS_FINISHED : Match.STATUS_IN_PROGRESS)
                .build();

        Observable<Void> obserable = service.updateMatch(match.getId(), update)
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler());

        if (isMatchFinished) {
            obserable.retry();
        }

        obserable.subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.d("MatchUpdate", "Update finished: " + update);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("MatchUpdate", "Failed to update: ", e);
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });

    }

    private void startTimer() {
        stopTimer();

        timerSubscription = Observable.interval(1, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        setTimeRemaining(getTimeRemaining() - 1);
                        return getTimeRemaining();
                    }
                })
                .take(timeRemaining)
                .subscribe();
    }

    private void stopTimer() {
        SubscriptionUtils.unsubscribe(timerSubscription);
    }

    private void startNewGame() {
        stopTimer();
        setTimeRemaining(SHOT_CLOCK_AFTER_BREAK_SECONDS);
        isPlayerOneExtended = false;
        isPlayerTwoExtended = false;
        isExtensionsAllowed = false;
        notifyChange();
    }

    public void setMatchFinished(boolean isMatchFinished) {
        this.isMatchFinished = isMatchFinished;
        if (isMatchFinished) {
            listener.onFinished();
        } else {
            playerOneScore = Math.min(playerOneScore, match.getRace() -1);
            playerTwoScore = Math.min(playerTwoScore, match.getRace() -1);

            notifyChange();
        }

        sendScoreUpdate();
    }

    private void updatePlayerScore(final MatchPlayer player, int score) {
        if (player.getGamesOnTheWire() > score) {
            return;
        }

        // Prevents clicking too fast when checking if a player won
        if (playerOneScore == match.getRace() || playerTwoScore == match.getRace()) {
            return;
        }
        score = Math.min(score, match.getRace());

        stopTimer();

        if (player == match.getPlayerOne()) {
            playerOneScore = score;
        } else {
            playerTwoScore = score;
        }

        if (score == match.getRace()) {
            listener.onCheckMatchWinner(match, player);
        } else {
            isPlayerOneTurn = !isPlayerOneTurn;
        }

        startNewGame();
        notifyChange();
        sendScoreUpdate();
    }
}
