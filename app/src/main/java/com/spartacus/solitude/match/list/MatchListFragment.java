package com.spartacus.solitude.match.list;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.databinding.BindingRecyclerViewAdapter;
import com.spartacus.solitude.databinding.FragmentMatchListBinding;
import com.spartacus.solitude.databinding.ViewModelFragment;
import com.spartacus.solitude.match.StartMatchDialog;
import com.spartacus.solitude.model.Match;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class MatchListFragment extends ViewModelFragment<MatchListViewModel, FragmentMatchListBinding> implements MatchListViewModel.View
{

    private static final String ARG_TOURNAMENT_ID = "ARG_TOURNAMENT_ID";

    public static MatchListFragment newInstance(int tournamentId) {
        Bundle args = new Bundle();
        args.putInt(ARG_TOURNAMENT_ID, tournamentId);

        MatchListFragment fragment = new MatchListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataBinding().recyclerView.setHasFixedSize(true);
        getDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HorizontalDividerItemDecoration itemDecoration = new HorizontalDividerItemDecoration.Builder(getContext())
                .build();

        getDataBinding().recyclerView.addItemDecoration(itemDecoration);


        BindingRecyclerViewAdapter adapter = new BindingRecyclerViewAdapter<MatchListViewModel.ItemViewModel>(getViewModel().items) {
            @Override
            public int getLayout(MatchListViewModel.ItemViewModel model) {
                return R.layout.item_match;
            }

            @Override
            public void bind(ViewDataBinding binding, MatchListViewModel.ItemViewModel model) {
                binding.setVariable(BR.viewModel, model);
            }
        };

        getDataBinding().recyclerView.setAdapter(adapter);
    }

    @Override
    protected MatchListViewModel createViewModel(FragmentMatchListBinding dataBinding) {
        int tournamentId = getArguments().getInt(ARG_TOURNAMENT_ID);
        return new MatchListViewModel(this, tournamentId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_match_list;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void showMatchFetchError() {
        Toast.makeText(getContext(), "Failed to fetch matches", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMatchDialog(Match match) {
        StartMatchDialog dialog = StartMatchDialog.newInstance(match);
        dialog.show(getActivity().getSupportFragmentManager(), "Dialog");
    }
}
