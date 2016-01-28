package com.spartacus.shotclockscorekeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.spartacus.shotclockscorekeeper.models.Match;

import software.spartacus.com.shotclockscorekeeper.R;

public class CoinFlipDialogFragment extends DialogFragment {

    public static final String COIN_FLIP_WINNER_PLAYER_ID = "COIN_FLIP_WINNER_PLAYER_ID";

    public static DialogFragment newInstance(Match match) {
        Bundle args = new Bundle();
        args.putParcelable("match", match);

        CoinFlipDialogFragment fragment = new CoinFlipDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Match match = getArguments().getParcelable("match");

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.coin_flip_title, match.getTableNumber()))
                .setMessage(R.string.coin_flip_message)
                .setNegativeButton(match.getPlayerOne().getDisplayName(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent().putExtra(COIN_FLIP_WINNER_PLAYER_ID, match.getPlayerOne().getId());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .setPositiveButton(match.getPlayerTwo().getDisplayName(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent().putExtra(COIN_FLIP_WINNER_PLAYER_ID, match.getPlayerTwo().getId());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(Dialog.BUTTON_POSITIVE);
                btnPositive.setTextSize(40);

                Button btnNegative = dialog.getButton(Dialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(40);
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}
