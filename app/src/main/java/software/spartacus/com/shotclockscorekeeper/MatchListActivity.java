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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;

public class MatchListActivity extends ListActivity implements HttpRequestCompleted {
    String[] matchPlayers = null;
    JSONObject matches = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_list);

        new HttpRequestTask(this).execute(new String[]{"http://assistant-tournament-director.herokuapp.com"});
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

    @Override
    public void onHttpRequestCompleted(JSONObject json) {

//        matches = new JSONObject("{\"matches\":[{\"id\":1,\"players\":[{\"full_name\":\"Kevin May\",\"display_name\":\"Spartacus\",\"games_on_the_wire\":2},{\"full_name\":\"Oscar Dominguez\",\"display_name\":\"Oscar\",\"games_on_the_wire\":0}]}]}");
//        matchPlayers = new String[]{"Kevin May vs Oscar Dominguez"};

        matchPlayers = new String[]{};

        JSONArray matches = null;
        try {
            matches = json.getJSONArray("matches");
            for(int count = 0; count < matches.length(); count++) {
                JSONArray players = matches.getJSONObject(count).getJSONArray("players");
                matchPlayers[count] = players.getJSONObject(0).getString("full_name") + " vs " + players.getJSONObject(0).getString("full_name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, matchPlayers);
        setListAdapter(adapter);
    }
}
