package com.spartacus.solitude.match.play;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ExitMatchDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Quit Match")
                .setMessage("Are you sure you want to quit the match?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
                        }
                    }
                })
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
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