package com.spartacus.shotclockscorekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spartacus.shotclockscorekeeper.models.Match;

public class PlayMatchActivity extends AppCompatActivity {

    private static final String TAG = "PlayMatchActivity";

    public static final String EXTRA_MATCH = "match";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Match match = getIntent().getParcelableExtra(EXTRA_MATCH);
            if (match == null) {
                Log.e(TAG, "Activity requires a match");
                finish();
                return;
            }

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, PlayMatchFragment.newInstance(match))
                    .commit();
        }
    }
}
