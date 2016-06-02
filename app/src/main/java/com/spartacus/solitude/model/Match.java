package com.spartacus.solitude.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Match implements Parcelable {

    public static final int STANDARD_MATCH = 1;
    public static final int FREE_PLAY_MATCH = 2;

    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_CREATED = "created";

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

    @SerializedName("table_number")
    private Integer table;

    @SerializedName("status")
    private String status;

    private int matchType = STANDARD_MATCH;

    protected Match(Parcel in) {
        id = in.readInt();
        tournamentId = in.readInt();
        race = in.readInt();
        matchType = in.readInt();
        status = in.readString();


        players = new ArrayList<>();
        in.readTypedList(players, MatchPlayer.CREATOR);

        if (in.readByte() == 1) {
            table = in.readInt();
        }
    }

    private Match(Builder builder) {
        this.id = builder.id;
        this.tournamentId = builder.tournamentId;
        this.race = builder.race;
        this.status = builder.status;
        this.players = new ArrayList<>(builder.players);
        this.table = builder.table;
        this.matchType = builder.matchType;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tournamentId);
        dest.writeInt(race);
        dest.writeInt(matchType);
        dest.writeString(status);
        dest.writeTypedList(players);

        if (table != null) {
            dest.writeByte((byte) 1);
            dest.writeInt(table);
        } else {
            dest.writeByte((byte) 0);
        }
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", race=" + race +
                ", tournamentId=" + tournamentId +
                ", players=" + players +
                ", table=" + table +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (id != match.id) return false;
        if (race != match.race) return false;
        if (tournamentId != match.tournamentId) return false;
        if (players != null ? !players.equals(match.players) : match.players != null) return false;
        if (table != null ? !table.equals(match.table) : match.table != null) return false;
        return status != null ? status.equals(match.status) : match.status == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + race;
        result = 31 * result + tournamentId;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
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

    public MatchPlayer getPlayerOne() {
        return getPlayers().get(0);
    }

    public MatchPlayer getPlayerTwo() {
        return getPlayers().get(1);
    }

    public Integer getTable() {
        return table;
    }

    public String getStatus() {
        return status;
    }

    public int getMatchType() {
        return matchType;
    }

    public static class Builder {
        private Integer table;
        private String status = STATUS_CREATED;
        private int id;
        private int race;
        private int tournamentId;
        private int matchType = STANDARD_MATCH;
        private List<MatchPlayer> players = new ArrayList<>();

        public Builder addPlayer(MatchPlayer matchPlayer) {
            players.add(matchPlayer);
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTournamentId(int tournamentId) {
            this.tournamentId = tournamentId;
            return this;
        }

        public Builder setRace(int race) {
            this.race = race;
            return this;
        }

        public Builder setMatchType(int matchType) {
            this.matchType = matchType;
            return this;
        }

        public Builder setMatchStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setTable(int table) {
            this.table = table;
            return this;
        }

        public Match build() {
            if (players.size() != 2) {
                throw new IllegalArgumentException("Match must contain 2 players");
            }

            return new Match(this);
        }
    }
}
