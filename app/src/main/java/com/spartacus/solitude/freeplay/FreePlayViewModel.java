package com.spartacus.solitude.freeplay;

import android.content.Context;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.databinding.ViewModel;

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
        Log.e("WHAT", "COOL: " + getPlayerOne() + " v " + getPlayerTwo());
    }
}
