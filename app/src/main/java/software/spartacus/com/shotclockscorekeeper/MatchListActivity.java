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

public class MatchListActivity extends ListActivity implements HttpGetRequestCompleted {
    String[] matchPlayers = null;
    JSONArray matches = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_list);

        new HttpGetRequestTask(this).execute(new String[]{"http://assistant-tournament-director.herokuapp.com"});
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String matchId = null;
        String player1Id = null;
        String player2Id = null;
        String player1DisplayName = null;
        String player2DisplayName = null;
        String player1GamesOnTheWire = null;
        String player2GamesOnTheWire = null;

        try {
            JSONObject match = matches.getJSONObject(position);
            matchId = match.getString("id");
            JSONArray players = match.getJSONArray("players");
            player1Id = players.getJSONObject(0).getString("id");
            player2Id = players.getJSONObject(1).getString("id");
            player1DisplayName = players.getJSONObject(0).getString("display_name");
            player2DisplayName = players.getJSONObject(1).getString("display_name");
            player1GamesOnTheWire = String.valueOf(players.getJSONObject(0).getInt("games_on_the_wire"));
            player2GamesOnTheWire = String.valueOf(players.getJSONObject(1).getInt("games_on_the_wire"));
        } catch (JSONException e) {
            Log.d("DEBUG", e.getMessage());
        }

        Intent intent = new Intent(this, PlayMatchActivity.class);
        intent.putExtra("matchId", matchId);
        intent.putExtra("player1Id", player1Id);
        intent.putExtra("player2Id", player2Id);
        intent.putExtra("player1Name", player1DisplayName);
        intent.putExtra("player2Name", player2DisplayName);
        intent.putExtra("player1GamesOnTheWire", player1GamesOnTheWire);
        intent.putExtra("player2GamesOnTheWire", player2GamesOnTheWire);
        startActivity(intent);
    }

    @Override
    public void onHttpGetRequestCompleted(JSONObject json) {
        try {
            matches = json.getJSONArray("matches");
            matchPlayers = new String[matches.length()];
            for(int count = 0; count < matches.length(); count++) {
                JSONArray players = matches.getJSONObject(count).getJSONArray("players");
                matchPlayers[count] = players.getJSONObject(0).getString("full_name") + " vs " + players.getJSONObject(1).getString("full_name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, matchPlayers);
        setListAdapter(adapter);
    }
}
