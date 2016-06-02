package com.spartacus.solitude.match.play;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchUpdate;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MatchActivity extends AppCompatActivity {

    private static final String TAG = "MatchActivity";

    public static final String EXTRA_MATCH = "match";
    public static final String EXTRA_TABLE = "table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Match match = getIntent().getParcelableExtra(EXTRA_MATCH);
        int table = getIntent().getIntExtra(EXTRA_TABLE, -1);

        if (match == null) {
            Log.e(TAG, "Activity requires a match");
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, LagWinnerFragment.newInstance(match, table))
                    .commit();
        }

        switch (match.getMatchType()) {
            case Match.FREE_PLAY_MATCH:
                setTitle("Free Play");
                break;
            case Match.STANDARD_MATCH:
                setTitle("Match - Table " + table);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        ExitMatchDialog dialog = new ExitMatchDialog();
        dialog.show(getSupportFragmentManager(), "EXIT");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class ExitMatchDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Quit Match")
                    .setMessage("Are you sure you want to quit the match?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();

                            // TODO move this to a better place

                            Match match = getActivity().getIntent().getParcelableExtra(EXTRA_MATCH);
                            if (match == null || match.getMatchType() == Match.FREE_PLAY_MATCH) {
                                return;
                            }

                            MatchUpdate matchUpdate = new MatchUpdate.Builder()
                                    .setStatus(Match.STATUS_CREATED)
                                    .setPlayerScore(match.getPlayerOne(), match.getPlayerOne().getGamesOnTheWire())
                                    .setPlayerScore(match.getPlayerTwo(), match.getPlayerTwo().getGamesOnTheWire())
                                    .build();

                            SolitudeApp.getInstance().getService()
                                    .updateMatch(match.getId(), matchUpdate)
                                    .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .retry()
                                    .subscribe(new Subscriber<Void>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Void aVoid) {
                                        }
                                    });
                        }
                    })
                    .create();

            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}
