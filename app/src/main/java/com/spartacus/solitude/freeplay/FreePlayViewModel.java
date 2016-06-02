package com.spartacus.solitude.freeplay;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.match.play.MatchActivity;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchPlayer;

public class FreePlayViewModel extends ViewModel {

    private static final String ARG_PLAYER_ONE = "ARG_PLAYER_ONE";
    private static final String ARG_PLAYER_TWO = "ARG_PLAYER_TWO";

    private String playerOne;
    private String playerTwo;

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        playerOne = savedInstanceState.getString(ARG_PLAYER_ONE);
        playerTwo = savedInstanceState.getString(ARG_PLAYER_TWO);
        notifyChange();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_PLAYER_ONE, playerOne);
        outState.putString(ARG_PLAYER_TWO, playerTwo);
    }

    @Bindable
    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String name) {
        this.playerOne = name;
        notifyPropertyChanged(BR.valid);
        notifyPropertyChanged(BR.playerOne);
    }

    @Bindable
    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String name) {
        this.playerTwo = name;
        notifyPropertyChanged(BR.valid);
        notifyPropertyChanged(BR.playerTwo);
    }

    @Bindable
    public boolean isValid() {
        return !TextUtils.isEmpty(playerOne) && !TextUtils.isEmpty(playerTwo);
    }

    public void onStartGameClicked(Context context) {
        Match match = new Match.Builder()
                .setMatchType(Match.FREE_PLAY_MATCH)
                .addPlayer(new MatchPlayer.Builder()
                        .setDisplayName(playerOne)
                        .setName(playerOne)
                        .setId(1)
                        .build())
                .addPlayer(new MatchPlayer.Builder()
                        .setDisplayName(playerTwo)
                        .setName(playerTwo)
                        .setId(2)
                        .build())
                .setRace(9)
                .build();

        Intent intent = new Intent(context, MatchActivity.class)
                .putExtra(MatchActivity.EXTRA_MATCH, match);

        context.startActivity(intent);
    }
}
