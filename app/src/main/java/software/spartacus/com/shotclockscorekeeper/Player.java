package software.spartacus.com.shotclockscorekeeper;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Player implements Parcelable {
    private final String id;
    private final String displayName;
    private final String gamesOnTheWire;
    private final String fullName;


    public Player(String id, String fullName, String displayName, String gamesOnTheWire) {
        this.id = id;
        this.fullName = fullName;
        this.displayName = displayName;
        this.gamesOnTheWire = gamesOnTheWire;
    }

    public static Player fromJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String displayName = jsonObject.getString("display_name");
        String gamesOnTheWire = String.valueOf(jsonObject.getInt("games_on_the_wire"));
        String fullName = jsonObject.getString("full_name");

        return new Player(id, fullName, displayName, gamesOnTheWire);
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGamesOnTheWire() {
        return gamesOnTheWire;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(displayName);
        dest.writeString(gamesOnTheWire);
        dest.writeString(fullName);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            String id = in.readString();
            String displayName = in.readString();
            String gamesOnTheWire = in.readString();
            String fullName = in.readString();

            return new Player(id, fullName, displayName, gamesOnTheWire);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };


}
