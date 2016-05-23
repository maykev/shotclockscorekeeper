package com.spartacus.solitude.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchRequest {

    @SerializedName("tournament_id")
    private int tournamentId;

    @SerializedName("players")
    private List<Integer> playerIds;

    // GSON
    MatchRequest() {}

    MatchRequest(Builder builder) {
        this.tournamentId = builder.tournamentId;
        this.playerIds = new ArrayList<>(builder.playerIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchRequest)) return false;

        MatchRequest request = (MatchRequest) o;

        if (tournamentId != request.tournamentId) return false;
        return !(playerIds != null ? !playerIds.equals(request.playerIds) : request.playerIds != null);

    }

    @Override
    public int hashCode() {
        int result = tournamentId;
        result = 31 * result + (playerIds != null ? playerIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MatchRequest{" +
                "tournamentId=" + tournamentId +
                ", playerIds=" + playerIds +
                '}';
    }

    public static class Builder {
        private int tournamentId = -1;
        private int table = -1;
        private List<Integer> playerIds;

        public Builder setTournamentId(int tournamentId) {
            this.tournamentId = tournamentId;
            return this;
        }

        public Builder setTable(int table) {
            this.table = table;
            return this;
        }

        public Builder setPlayers(@NonNull Player playerOne, @NonNull Player playerTwo) {
            if (playerOne.getId() == playerTwo.getId()) {
                throw new IllegalArgumentException("Players have matching IDs");
            }

            playerIds = new ArrayList<>();
            playerIds.add(playerOne.getId());
            playerIds.add(playerTwo.getId());

            return this;
        }

        public MatchRequest build() {
            if (playerIds == null || playerIds.isEmpty()) {
                throw new IllegalStateException("Missing players");
            }


            if (tournamentId < 0) {
                throw new IllegalStateException("Missing tournament ID");
            }

            return new MatchRequest(this);
        }
    }
}
