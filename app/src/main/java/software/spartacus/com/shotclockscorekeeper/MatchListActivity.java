package software.spartacus.com.shotclockscorekeeper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MatchListActivity extends ListActivity {
    String[] matchPlayers = null;
    JSONObject matches = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_list);

        try {
            matches = new JSONObject("{\"matches\":[{\"id\":1,\"players\":[{\"full_name\":\"Kevin May\",\"display_name\":\"Spartacus\",\"games_on_the_wire\":2},{\"full_name\":\"Oscar Dominguez\",\"display_name\":\"Oscar\",\"games_on_the_wire\":0}]}]}");
        } catch (JSONException e) {
            Log.d("DEBUG", "error creating JSON");
        }

        matchPlayers = new String[]{"Kevin May vs Oscar Dominguez"};

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, matchPlayers);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String match = matchPlayers[position];
        android.widget.Toast.makeText(this, match, Toast.LENGTH_LONG).show();

        String player1DisplayName = null;
        String player2DisplayName = null;
        String player1GamesOnTheWire = null;
        String player2GamesOnTheWire = null;

        try {
            Log.d("DISPLAY_NAME", matches.getJSONArray("matches").getJSONObject(0).getJSONArray("players").getJSONObject(0).getString("display_name"));
            player1DisplayName = matches.getJSONArray("matches").getJSONObject(0).getJSONArray("players").getJSONObject(0).getString("display_name");
            player2DisplayName = matches.getJSONArray("matches").getJSONObject(0).getJSONArray("players").getJSONObject(1).getString("display_name");
            player1GamesOnTheWire = String.valueOf(matches.getJSONArray("matches").getJSONObject(0).getJSONArray("players").getJSONObject(0).getInt("games_on_the_wire"));
            player2GamesOnTheWire = String.valueOf(matches.getJSONArray("matches").getJSONObject(0).getJSONArray("players").getJSONObject(1).getInt("games_on_the_wire"));
        } catch (JSONException e) {
            Log.d("DEBUG", e.getMessage());
        }

        Intent intent = new Intent(this, PlayMatchActivity.class);
        intent.putExtra("player1Name", player1DisplayName);
        intent.putExtra("player2Name", player2DisplayName);
        intent.putExtra("player1GamesOnTheWire", player1GamesOnTheWire);
        intent.putExtra("player2GamesOnTheWire", player2GamesOnTheWire);
        startActivity(intent);
    }
}
