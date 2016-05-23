package com.spartacus.solitude.match.play;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchPlayer;
import com.spartacus.solitude.model.MatchUpdate;
import com.spartacus.solitude.databinding.FragmentPlayMatchBinding;
import com.spartacus.solitude.databinding.ViewModelFragment;
import com.spartacus.solitude.MatchUpdateService;


public class PlayMatchFragment extends ViewModelFragment<PlayMatchViewModel,FragmentPlayMatchBinding> implements PlayMatchViewModel.Listener {

    public static PlayMatchFragment newInstance(Match match, int table, int breakPlayerId) {
        Bundle args = new Bundle();
        args.putParcelable("match", match);
        args.putInt("table", table);
        args.putInt("breakPlayerId", breakPlayerId);

        PlayMatchFragment fragment = new PlayMatchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataBinding().scoreTextViewPlayer1.setOnSwipeListener(getViewModel().getPlayerOneScoreListener());
        getDataBinding().scoreTextViewPlayer2.setOnSwipeListener(getViewModel().getPlayerTwoScoreListener());
    }

    @Override
    protected PlayMatchViewModel createViewModel(FragmentPlayMatchBinding dataBinding) {
        Match match = getArguments().getParcelable("match");
        int breakPlayerId = getArguments().getInt("breakPlayerId");
        int table = getArguments().getInt("table");

        return new PlayMatchViewModel(this, match, breakPlayerId, table);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_play_match;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void onCheckMatchWinner(Match match, MatchPlayer player) {
        if (getChildFragmentManager().findFragmentByTag("FinishMatchDialog") != null) {
            return;
        }

        DialogFragment dialogFragment = FinishMatchDialog.newInstance(player);
        dialogFragment.setTargetFragment(PlayMatchFragment.this, 100);
        dialogFragment.show(getChildFragmentManager(), "FinishMatchDialog");
    }

    @Override
    public void onFinished() {
        getActivity().finish();
    }

    @Override
    public void onMatchUpdate(Match match, MatchUpdate update) {
        Intent intent = new Intent(getContext(), MatchUpdateService.class)
                .setAction(MatchUpdateService.ACTION_MATCH_UPDATE)
                .putExtra(MatchUpdateService.EXTRA_MATCH, match)
                .putExtra(MatchUpdateService.EXTRA_MATCH_UPDATE, update);

        getContext().startService(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100) {
            return;
        }

        getViewModel().setMatchFinished(resultCode == Activity.RESULT_OK);
    }
}
