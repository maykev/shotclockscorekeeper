package com.spartacus.solitude.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchUpdate implements Parcelable {

    @SerializedName("table")
    private Integer table;

    @SerializedName("status")
    private String status;

    @SerializedName("players")
    private List<PlayerScore> playerScores;

    MatchUpdate(Builder builder) {
        this.table = builder.table;
        this.status = builder.status;
        this.playerScores = new ArrayList<>(builder.playerScores);
    }

    protected MatchUpdate(Parcel in) {
        if (in.readByte() != 0) {
            table = in.readInt();
        }
        status = in.readString();
        playerScores = in.createTypedArrayList(PlayerScore.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (table != null) {
            dest.writeByte((byte)1);
            dest.writeInt(table);
        } else {
            dest.writeByte((byte)0);
        }

        dest.writeString(status);
        dest.writeTypedList(playerScores);
    }

    public static final Creator<MatchUpdate> CREATOR = new Creator<MatchUpdate>() {
        @Override
        public MatchUpdate createFromParcel(Parcel in) {
            return new MatchUpdate(in);
        }

        @Override
        public MatchUpdate[] newArray(int size) {
            return new MatchUpdate[size];
        }
    };

    @Override
    public String toString() {
        return "MatchUpdate{" +
                "table=" + table +
                ", status=" + status +
                ", playerScores=" + playerScores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchUpdate)) return false;

        MatchUpdate that = (MatchUpdate) o;

        if (table != null ? !table.equals(that.table) : that.table != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return playerScores != null ? playerScores.equals(that.playerScores) : that.playerScores == null;

    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (playerScores != null ? playerScores.hashCode() : 0);
        return result;
    }

    private static class PlayerScore implements Parcelable{
        @SerializedName("id")
        private int id;

        @SerializedName("score")
        private int score;

        private PlayerScore(int id, int score) {
            this.id = id;
            this.score = score;
        }

        protected PlayerScore(Parcel in) {
            id = in.readInt();
            score = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(score);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlayerScore> CREATOR = new Creator<PlayerScore>() {
            @Override
            public PlayerScore createFromParcel(Parcel in) {
                return new PlayerScore(in);
            }

            @Override
            public PlayerScore[] newArray(int size) {
                return new PlayerScore[size];
            }
        };

        @Override
        public String toString() {
            return "PlayerScore{" +
                    "id=" + id +
                    ", score=" + score +
                    '}';
        }
    }

    public static class Builder {

        private Integer table;
        private String status;
        private List<PlayerScore> playerScores = new ArrayList<>();

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setTable(int table) {
            this.table = table;
            return this;
        }

        public Builder setPlayerScore(Player player, int score) {
            for (PlayerScore playerScore : playerScores) {
                if (playerScore.id == player.getId()) {
                    playerScores.remove(playerScore);
                    break;
                }
            }

            PlayerScore playerScore = new PlayerScore(player.getId(), score);
            playerScores.add(playerScore);

            return this;
        }

        public MatchUpdate build() {
            return new MatchUpdate(this);
        }
    }
}
