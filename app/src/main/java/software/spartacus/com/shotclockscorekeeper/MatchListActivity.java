package software.spartacus.com.shotclockscorekeeper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchListActivity extends ListActivity implements HttpGetRequestCompleted {

    private static final String TAG = "MatchListActivity";
    List<Match> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_list);

        new HttpGetRequestTask(this).execute("http://assistant-tournament-director.herokuapp.com");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, PlayMatchActivity.class)
                .putExtra(PlayMatchActivity.EXTRA_MATCH, matches.get(position));

        startActivity(intent);
    }

    @Override
    public void onHttpGetRequestCompleted(JSONObject json) {
        matches = new ArrayList<>();
        String[] matchPlayers = null;

        try {
            JSONArray matchesJson = json.getJSONArray("matches");
            matchPlayers = new String[matchesJson.length()];

            for(int i = 0; i < matchesJson.length(); i++) {
                Match match = Match.fromJson(matchesJson.getJSONObject(i));
                matchPlayers[i] = match.getPlayerOne().getFullName() + " vs " + match.getPlayerTwo().getFullName();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse matches json: " + json, e);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, matchPlayers);
        setListAdapter(adapter);
    }
}
