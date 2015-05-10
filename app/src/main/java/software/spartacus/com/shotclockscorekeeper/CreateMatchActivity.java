package software.spartacus.com.shotclockscorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateMatchActivity extends AppCompatActivity implements HttpGetRequestCompleted, HttpPostRequestCompleted {

    private static final String TAG = "CreateMatchActivity";

    private Spinner spinnerPlayer1;
    private Spinner spinnerPlayer2;
    private Spinner spinnerTableNumber;
    private Button buttonStartMatch;

    private Integer[] playerIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        spinnerPlayer1 = (Spinner) findViewById(R.id.spinnerPlayer1);
        spinnerPlayer2 = (Spinner) findViewById(R.id.spinnerPlayer2);
        spinnerTableNumber = (Spinner) findViewById(R.id.spinnerTableNumber);
        buttonStartMatch = (Button) findViewById(R.id.buttonStartMatch);

        new HttpGetRequestTask(this).execute("http://assistant-tournament-director.herokuapp.com/player");

        buttonStartMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMatch();
            }
        });
    }

    private void createMatch() {
        JSONObject createMatch = new JSONObject();

        try {
            createMatch.put("table_number", spinnerTableNumber.getSelectedItem().toString());

            JSONObject player1 = new JSONObject();
            JSONObject player2 = new JSONObject();

            player1.put("id", playerIds[spinnerPlayer1.getSelectedItemPosition()]);
            player2.put("id", playerIds[spinnerPlayer2.getSelectedItemPosition()]);

            JSONArray players = new JSONArray();
            players.put(player1);
            players.put(player2);
            createMatch.put("players", players);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpPostRequestTask(this).execute(new String[]{"http://assistant-tournament-director.herokuapp.com/match", createMatch.toString()});
    }

    @Override
    public void onHttpGetRequestCompleted(JSONObject json) {
        String[] playerList = null;

        try {
            JSONArray players = json.getJSONArray("players");
            playerList = new String[players.length()];
            playerIds = new Integer[players.length()];
            for(int count = 0; count < players.length(); count++) {
                playerList[count] = players.getJSONObject(count).getString("full_name");
                playerIds[count] = Integer.parseInt(players.getJSONObject(count).getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer1.setAdapter(dataAdapter);
        spinnerPlayer2.setAdapter(dataAdapter);
    }

    @Override
    public void onHttpPostRequestCompleted(JSONObject jsonObject) {
        Match match;
        try {
            match = Match.fromJson(jsonObject);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse match json: " + jsonObject, e);
            return;
        }

        Intent intent = new Intent(this, PlayMatchActivity.class)
                .putExtra(PlayMatchActivity.EXTRA_MATCH, match);

        startActivity(intent);
    }
}
