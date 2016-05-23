package com.spartacus.solitude.tournament;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.databinding.BindingRecyclerViewAdapter;
import com.spartacus.solitude.databinding.FragmentTournamentListBinding;
import com.spartacus.solitude.databinding.ViewModelFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class TournamentListFragment extends ViewModelFragment<TournamentListViewModel, FragmentTournamentListBinding> implements TournamentListViewModel.View {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataBinding().recyclerView.setHasFixedSize(true);
        getDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HorizontalDividerItemDecoration itemDecoration = new HorizontalDividerItemDecoration.Builder(getContext())
                .build();

        getDataBinding().recyclerView.addItemDecoration(itemDecoration);

        BindingRecyclerViewAdapter adapter = new BindingRecyclerViewAdapter<TournamentListViewModel.ItemViewModel>(getViewModel().getItems()) {
            @Override
            public int getLayout(TournamentListViewModel.ItemViewModel model) {
                return R.layout.item_tournament;
            }

            @Override
            public void bind(ViewDataBinding binding, TournamentListViewModel.ItemViewModel model) {
                binding.setVariable(BR.viewModel, model);
            }
        };

        getDataBinding().recyclerView.setAdapter(adapter);

    }

    @Override
    protected TournamentListViewModel createViewModel(FragmentTournamentListBinding dataBinding) {
        return new TournamentListViewModel(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tournament_list;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    public void showFetchError() {
        Toast.makeText(getContext(), "Failed to fetch tournaments", Toast.LENGTH_SHORT).show();
    }
}
