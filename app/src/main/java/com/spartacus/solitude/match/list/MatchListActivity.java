package com.spartacus.solitude.match.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.spartacus.solitude.model.Tournament;

public class MatchListActivity extends AppCompatActivity {
    public static final String TOURNAMENT_EXTRA = "TOURNAMENT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tournament tournament = getIntent() == null ? null : (Tournament) getIntent().getParcelableExtra(TOURNAMENT_EXTRA);
        if (tournament == null) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, MatchListFragment.newInstance(tournament.getId()))
                    .commit();
        }

        setTitle(tournament.getName());
    }
}
