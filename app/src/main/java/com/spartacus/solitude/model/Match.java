package com.spartacus.solitude.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Match implements Parcelable {

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    @SerializedName("id")
    private int id;

    @SerializedName("race")
    private int race;

    @SerializedName("tournament_id")
    private int tournamentId;

    @SerializedName("players")
    private List<MatchPlayer> players;

    protected Match(Parcel in) {
        id = in.readInt();
        tournamentId = in.readInt();
        race = in.readInt();
        players = new ArrayList<>();
        in.readTypedList(players, MatchPlayer.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tournamentId);
        dest.writeInt(race);
        dest.writeTypedList(players);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getRace() {
        return race;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (id != match.id) return false;
        if (race != match.race) return false;
        if (tournamentId != match.tournamentId) return false;
        return !(players != null ? !players.equals(match.players) : match.players != null);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + race;
        result = 31 * result + tournamentId;
        result = 31 * result + (players != null ? players.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", race=" + race +
                ", players=" + players +
                '}';
    }

    public MatchPlayer getPlayerOne() {
        return getPlayers().get(0);
    }

    public MatchPlayer getPlayerTwo() {
        return getPlayers().get(1);
    }


}
