package com.spartacus.shotclockscorekeeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spartacus.shotclockscorekeeper.models.Match;
import com.spartacus.shotclockscorekeeper.models.PlayerListingInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchClient {

    private static final String SCORE_URL = "http://assistant-tournament-director.herokuapp.com/match/";
    private static final String CREATE_GAME_URL = "http://assistant-tournament-director.herokuapp.com/match";
    private static final String FETCH_PLAYERS_URL = "http://assistant-tournament-director.herokuapp.com/player";


    private static RequestQueue requestQueue;
    private final Object tag;

    public void cancelRequests() {
        requestQueue.cancelAll(tag);
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure();
    }

    public MatchClient(Context context, Object tag) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.start();
        }
        this.tag = tag;
    }


    public void fetchPlayers(@NonNull final Callback<List<PlayerListingInfo>> callback) {
        JsonObjectRequest request = new JsonObjectRequest(FETCH_PLAYERS_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<PlayerListingInfo> playerListing = new ArrayList<>();
                    JSONArray playersJson = response.getJSONArray("players");
                    for(int i = 0; i < playersJson.length(); i++) {
                        playerListing.add(PlayerListingInfo.fromJson(playersJson.getJSONObject(i)));
                    }
                    callback.onSuccess(playerListing);
                } catch (JSONException e) {
                    Log.e("MatchClient", "Failed to parse players response: " + response, e);
                    callback.onFailure();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MatchClient", "Failed to fetch players: " + error);
                callback.onFailure();
            }
        });

        request.setTag(tag);
        requestQueue.add(request);
    }

    public void createGame(String playerOneId, String playerTwoId, int tableNumber, final Callback<Match> callback) {
        JSONObject createMatchJson = new JSONObject();

        try {
            createMatchJson.put("table_number", String.valueOf(tableNumber));

            JSONObject playerOneJson = new JSONObject();
            playerOneJson.put("id", playerOneId);

            JSONObject playerTwoJson = new JSONObject();
            playerTwoJson.put("id", playerTwoId);

            JSONArray playersJson = new JSONArray();
            playersJson.put(playerOneJson);
            playersJson.put(playerTwoJson);
            createMatchJson.put("players", playersJson);
        } catch (JSONException e) {
            Log.e("MatchClient", "Failed to create match json", e);
            callback.onFailure();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(CREATE_GAME_URL, createMatchJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    callback.onSuccess(Match.fromJson(response));
                } catch (JSONException e) {
                    Log.e("MatchClient", "Failed to parse match response", e);
                    callback.onFailure();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MatchClient", "Failed to create match: " + error);
                callback.onFailure();
            }
        });

        request.setTag(tag);
        requestQueue.add(request);
    }

    public void updateScore(Match match, int playerOneScore, int playerTwoScore) {
        JSONObject scoreUpdate = new JSONObject();

        try {
            JSONObject player1 = new JSONObject();
            JSONObject player2 = new JSONObject();

            player1.put("id", match.getPlayerOne().getId());
            player1.put("score", playerOneScore);
            player2.put("id", match.getPlayerTwo().getId());
            player2.put("score", playerTwoScore);
            JSONArray players = new JSONArray();
            players.put(player1);
            players.put(player2);
            scoreUpdate.put("players", players);
        } catch (JSONException e) {
            Log.e("MatchClient", "Failed to create score update json", e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, SCORE_URL + match.getId(), scoreUpdate, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("MatchClient", "Score updated successfully");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("MatchClient", "Score failed to update " + error);
            }
        });

        request.setTag(tag);
        requestQueue.add(request);
    }
}
