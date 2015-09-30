package software.spartacus.com.shotclockscorekeeper;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match implements Parcelable {

    private final String id;
    private final Player playerOne;
    private final Player playerTwo;

    public Match(String id, Player playerOne, Player playerTwo) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public static Match fromJson(JSONObject matchJson) throws JSONException {
        String id = matchJson.getString("id");

        JSONArray playersJson = matchJson.getJSONArray("players");
        Player playerOne = Player.fromJson(playersJson.getJSONObject(0));
        Player playerTwo = Player.fromJson(playersJson.getJSONObject(1));

        return new Match(id, playerOne, playerTwo);
    }

    public String getId() {
        return id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(playerOne, 0);
        dest.writeParcelable(playerTwo, 0);
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel in) {
            String id = in.readString();
            Player playerOne = in.readParcelable(Player.class.getClassLoader());
            Player playerTwo = in.readParcelable(Player.class.getClassLoader());

            return new Match(id, playerOne, playerTwo);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };


}
