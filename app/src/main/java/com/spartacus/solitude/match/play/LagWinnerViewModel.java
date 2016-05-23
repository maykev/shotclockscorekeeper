package com.spartacus.solitude.match.play;

import android.databinding.Bindable;
import android.view.View;
import android.widget.RadioGroup;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.databinding.ViewModel;

public class LagWinnerViewModel extends ViewModel {

    private final Match match;
    private final Listener listener;
    private int breakPlayerId = -1;
    private final RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    public RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public interface Listener {
        void onLagWinnerSelected(Match match, int lagWinnerPlayerId);
    }

    public LagWinnerViewModel(Listener listener, final Match match) {
        this.match = match;
        this.listener = listener;

        this.onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.player_one) {
                    breakPlayerId = match.getPlayerOne().getId();
                } else {
                    breakPlayerId = match.getPlayerTwo().getId();
                }

                notifyPropertyChanged(BR.valid);
            }
        };
    }

    @Bindable
    public String getPlayerOneName() {
        return match.getPlayerOne().getName();
    }

    @Bindable
    public String getPlayerTwoName() {
        return match.getPlayerTwo().getName();
    }


    public void onStartGame(View view) {
        listener.onLagWinnerSelected(match, breakPlayerId);
    }

    @Bindable
    public boolean isValid() {
        return breakPlayerId != -1;
    }
}
