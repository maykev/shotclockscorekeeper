package com.spartacus.solitude.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchUpdate implements Parcelable {

    @SerializedName("table")
    private int table;

    @SerializedName("finished")
    private boolean isFinished;

    @SerializedName("players")
    private List<PlayerScore> playerScores;

    MatchUpdate() {
        // GSON
    }
    
    MatchUpdate(Builder builder) {
        this.table = builder.table;
        this.isFinished = builder.isFinished;
        this.playerScores = new ArrayList<>(builder.playerScores);
    }


    protected MatchUpdate(Parcel in) {
        table = in.readInt();
        isFinished = in.readByte() != 0;
        playerScores = in.createTypedArrayList(PlayerScore.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(table);
        dest.writeByte((byte) (isFinished ? 1 : 0));
        dest.writeTypedList(playerScores);
    }

    @Override
    public int describeContents() {
        return 0;
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
                ", isFinished=" + isFinished +
                ", playerScores=" + playerScores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchUpdate)) return false;

        MatchUpdate that = (MatchUpdate) o;

        if (table != that.table) return false;
        if (isFinished != that.isFinished) return false;
        return !(playerScores != null ? !playerScores.equals(that.playerScores) : that.playerScores != null);

    }

    @Override
    public int hashCode() {
        int result = table;
        result = 31 * result + (isFinished ? 1 : 0);
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

        private int table = -1;
        private boolean isFinished;
        private List<PlayerScore> playerScores = new ArrayList<>();

        public Builder setMatchFinished(boolean isFinished) {
            this.isFinished = isFinished;
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
            if (table < 0) {
                throw new IllegalStateException("Missing table");
            }

            if (playerScores.size() != 2) {
                throw new IllegalStateException("Scores must be provided for 2 players");
            }

            return new MatchUpdate(this);
        }
    }
}
