package com.spartacus.solitude.match.play;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.spartacus.solitude.model.MatchPlayer;

public class FinishMatchDialog extends DialogFragment {

    public static final String PLAYER = "PLAYER";

    public static FinishMatchDialog newInstance(MatchPlayer player) {
        Bundle args = new Bundle();
        args.putParcelable(PLAYER, player);

        FinishMatchDialog fragment = new FinishMatchDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MatchPlayer player = getArguments().getParcelable(PLAYER);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(player.getName() + " wins.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
                        }
                    }
                })
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .create();
        
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


}