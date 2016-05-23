package com.spartacus.solitude.match.create;


import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.databinding.BindingListViewAdapter;
import com.spartacus.solitude.databinding.FragmentCreateMatchBinding;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.databinding.ViewModelFragment;
import com.spartacus.solitude.match.StartMatchDialog;

import rx.Subscription;

public class CreateMatchFragment extends ViewModelFragment<CreateMatchViewModel, FragmentCreateMatchBinding> implements CreateMatchViewModel.View {

    private static final String ARG_TOURNAMENT_ID = "ARG_TOURNAMENT_ID";

    public static CreateMatchFragment newInstance(int tournamentId) {
        Bundle args = new Bundle();
        args.putInt(ARG_TOURNAMENT_ID, tournamentId);

        CreateMatchFragment fragment = new CreateMatchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BindingListViewAdapter spinnerAdapter = new BindingListViewAdapter<PlayerViewModel>(getContext(), R.layout.item_player, getViewModel().players) {
            @Override
            public void bind(ViewDataBinding binding, PlayerViewModel model) {
                binding.setVariable(BR.viewModel, model);
            }
        };

        getDataBinding().spinnerPlayer1.setAdapter(spinnerAdapter);
        getDataBinding().spinnerPlayer2.setAdapter(spinnerAdapter);

        getDataBinding().spinnerPlayer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getViewModel().onPlayerOneSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getDataBinding().spinnerPlayer2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getViewModel().onPlayerTwoSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected CreateMatchViewModel createViewModel(FragmentCreateMatchBinding dataBinding) {
        int tournamentId = getArguments().getInt(ARG_TOURNAMENT_ID);

        return new CreateMatchViewModel(this, tournamentId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_match;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void showStartMatchDialog(Match match) {
        StartMatchDialog dialog = StartMatchDialog.newInstance(match);
        dialog.show(getActivity().getSupportFragmentManager(), "StartMatchDialog");
    }

    @Override
    public void showPlayerFetchError() {
        Toast.makeText(getContext(), "Failed to fetch players", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showCreateMatchError() {
        Toast.makeText(getContext(), "Match failed to create", Toast.LENGTH_SHORT).show();

    }
}
