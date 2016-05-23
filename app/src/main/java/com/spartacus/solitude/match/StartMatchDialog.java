package com.spartacus.solitude.match;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.match.play.MatchActivity;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.databinding.FragmentStartMatchBinding;
import com.spartacus.solitude.databinding.ViewModelDialogFragment;

public class StartMatchDialog extends ViewModelDialogFragment<StartMatchViewModel, FragmentStartMatchBinding> implements StartMatchViewModel.Listener {
    private static final String ARG_MATCH = "ARG_MATCH";

    public static StartMatchDialog newInstance(Match match) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MATCH, match);

        StartMatchDialog fragment = new StartMatchDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Select Table");
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataBinding().table.setAdapter(getViewModel().tableAdapter);
    }

    @Override
    protected StartMatchViewModel createViewModel(FragmentStartMatchBinding dataBinding) {
        Match match = getArguments().getParcelable(ARG_MATCH);

        return new StartMatchViewModel(getContext(), this, match);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_start_match;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void onStartMatch(Match match, int table) {
        Intent intent = new Intent(getContext(), MatchActivity.class)
                .putExtra(MatchActivity.EXTRA_MATCH, match)
                .putExtra(MatchActivity.EXTRA_TABLE, table);

        getContext().startActivity(intent);
        dismiss();
    }

    @Override
    public void onToast(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
