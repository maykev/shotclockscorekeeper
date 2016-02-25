package com.spartacus.shotclockscorekeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spartacus.shotclockscorekeeper.models.Match;
import com.spartacus.shotclockscorekeeper.models.PlayerListingInfo;

import java.util.ArrayList;
import java.util.List;

public class CreateMatchActivity extends AppCompatActivity {

    private Spinner spinnerPlayer1;
    private Spinner spinnerPlayer2;
    private Spinner spinnerTableNumber;
    private Button buttonStartMatch;

    private List<PlayerListingInfo> players = new ArrayList<>();

    private MatchClient client;

    private boolean isRefreshing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("Version: " + BuildConfig.VERSION_NAME);

        client = new MatchClient(this, this);
        spinnerPlayer1 = (Spinner) findViewById(R.id.spinnerPlayer1);
        spinnerPlayer1.setAdapter(new PlayerListingAdapter(this));

        spinnerPlayer2 = (Spinner) findViewById(R.id.spinnerPlayer2);
        spinnerPlayer2.setAdapter(new PlayerListingAdapter(this));

        spinnerTableNumber = (Spinner) findViewById(R.id.spinnerTableNumber);
        buttonStartMatch = (Button) findViewById(R.id.buttonStartMatch);
        buttonStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMatch();
            }
        });

        if (savedInstanceState != null) {
            List<PlayerListingInfo> savedPlayerInfo = savedInstanceState.getParcelableArrayList("player_info");
            if (savedPlayerInfo != null) {
                setPlayerListing(savedPlayerInfo);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_match, menu);

        MenuItem item = menu.findItem(R.id.menu_refresh);
        if (isRefreshing) {
            item.setEnabled(false);
            item.setActionView(R.layout.action_refresh);
        } else {
            item.setEnabled(true);
            item.setActionView(null);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refreshPlayerListing();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!players.isEmpty()) {
            outState.putParcelableArrayList("player_info", new ArrayList<>(players));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (players.isEmpty()) {
           refreshPlayerListing();
        } else {
            buttonStartMatch.setEnabled(true);
        }
    }

    private void refreshPlayerListing() {
        if (isRefreshing) {
            return;
        }

        isRefreshing = true;
        invalidateOptionsMenu();

        buttonStartMatch.setEnabled(false);

        client.fetchPlayers(new MatchClient.Callback<List<PlayerListingInfo>>() {
            @Override
            public void onSuccess(List<PlayerListingInfo> players) {
                setPlayerListing(players);
                buttonStartMatch.setEnabled(true);

                isRefreshing = false;
                invalidateOptionsMenu();
            }

            @Override
            public void onFailure() {
                Toast.makeText(CreateMatchActivity.this, "Failed to fetch players.", Toast.LENGTH_LONG).show();

                isRefreshing = false;
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        client.cancelRequests();
    }

    private void createMatch() {
        buttonStartMatch.setEnabled(false);

        String playerOneId = ((PlayerListingInfo) spinnerPlayer1.getSelectedItem()).getId();
        String playerTwoId = ((PlayerListingInfo) spinnerPlayer2.getSelectedItem()).getId();
        int tableNumber = Integer.valueOf(spinnerTableNumber.getSelectedItem().toString());

        client.createGame(playerOneId, playerTwoId, tableNumber, new MatchClient.Callback<Match>() {

            @Override
            public void onSuccess(Match match) {
                Intent intent = new Intent(CreateMatchActivity.this, PlayMatchActivity.class)
                        .putExtra(PlayMatchActivity.EXTRA_MATCH, match);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(CreateMatchActivity.this, "Failed to create match.", Toast.LENGTH_LONG).show();
                buttonStartMatch.setEnabled(true);
            }
        });
    }

    private void setPlayerListing(List<PlayerListingInfo> players) {
        this.players.clear();
        this.players.addAll(players);


        ((PlayerListingAdapter) spinnerPlayer1.getAdapter()).replaceAll(players);
        ((PlayerListingAdapter) spinnerPlayer2.getAdapter()).replaceAll(players);
    }

    private static class PlayerListingAdapter extends ListAdapter<PlayerListingInfo> {

        public PlayerListingAdapter(Context context) {
            super(context, android.R.layout.simple_spinner_item);
            this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        protected void bindView(View view, PlayerListingInfo item, int position) {
            ((TextView) view).setText(item.getFullName());
        }
    }

}
