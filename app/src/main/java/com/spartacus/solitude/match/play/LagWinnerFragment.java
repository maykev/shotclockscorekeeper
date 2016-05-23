package com.spartacus.solitude.match.play;

import android.os.Bundle;
import android.view.View;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.databinding.FragmentLagWinnerBinding;
import com.spartacus.solitude.databinding.ViewModelFragment;

public class LagWinnerFragment extends ViewModelFragment<LagWinnerViewModel, FragmentLagWinnerBinding> implements LagWinnerViewModel.Listener {
    private static final String ARG_MATCH = "ARG_MATCH";
    private static final String ARG_TABLE = "ARG_TABLE";

    public static LagWinnerFragment newInstance(Match match, int table) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MATCH, match);
        args.putInt(ARG_TABLE, table);

        LagWinnerFragment fragment = new LagWinnerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataBinding().lagWinner.setOnCheckedChangeListener(getViewModel().getOnCheckedChangeListener());
    }

    @Override
    protected LagWinnerViewModel createViewModel(FragmentLagWinnerBinding dataBinding) {
        Match match = getArguments().getParcelable(ARG_MATCH);

        return new LagWinnerViewModel(this, match);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lag_winner;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }


    @Override
    public void onLagWinnerSelected(Match match, int lagWinnerPlayerId) {
        int table = getArguments().getInt(ARG_TABLE);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, PlayMatchFragment.newInstance(match, table, lagWinnerPlayerId))
                .commit();
    }
}
