package com.spartacus.solitude;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchUpdate;

import rx.Subscriber;

public class MatchUpdateService extends IntentService {

    private static final String TAG = "MatchUpdateService";
    public static final String ACTION_MATCH_UPDATE = "ACTION_UPDATE_SCORE";

    public static final String EXTRA_MATCH_UPDATE = "EXTRA_SCORE_UPDATE";
    public static final String EXTRA_MATCH = "EXTRA_MATCH";

    public MatchUpdateService() {
        this("MatchUpdateService");
    }
    public MatchUpdateService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        switch (intent.getAction()) {
            case ACTION_MATCH_UPDATE:
                onUpdateScore(intent);
                break;
        }
    }

    private void onUpdateScore(Intent intent) {
        final Match match = intent.getParcelableExtra(EXTRA_MATCH);
        final MatchUpdate update = intent.getParcelableExtra(EXTRA_MATCH_UPDATE);

        if (match == null || update == null) {
            Log.e(TAG, "Missing match or update.");
            return;
        }

        SolitudeApp.getInstance()
                .getService()
                .updateMatch(match.getId(), update)
                .retry(2)
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Update finished: + update");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Failed to update: ", e);
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });

        Log.e(TAG, "DONE");
    }
}
