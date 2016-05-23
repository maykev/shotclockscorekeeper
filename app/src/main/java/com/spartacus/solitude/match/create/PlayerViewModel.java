package com.spartacus.solitude.match.create;

import android.databinding.BaseObservable;

import com.spartacus.solitude.model.Player;

public class PlayerViewModel extends BaseObservable {

    public final Player player;

    public PlayerViewModel(Player player) {
        this.player = player;
    }

    public String getName() {
        return player.getName();
    }
}
