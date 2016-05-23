package com.spartacus.solitude;

import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchRequest;
import com.spartacus.solitude.model.MatchUpdate;
import com.spartacus.solitude.model.Player;
import com.spartacus.solitude.model.Tournament;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SolitudeService {

    /**
     * Gets the tournament listing.
     *
     * Error Codes:
     *  - 500 - Unexpected Server Error
     *
     * Note: Empty [] means there are no tournaments `In progress`.
     *
     * @return A retrofit call instance for the request.
     */
    @GET("api/tournaments")
    Observable<List<Tournament>> listTournaments();

    /**
     * Gets the current available tables for a tournament.
     *
     * Error Codes:
     *  - 500 - Unexpected Server Error
     *
     * Note: Empty [] means there are no tables available.
     *
     * @param tournamentId The tournament ID.
     * @return A retrofit call instance for the request.
     */
    @GET("api/tournaments/{id}/tables")
    Observable<List<Integer>> listTables(@Path("id") int tournamentId);

    /**
     * Gets the tournament listing.
     *
     * Error Codes:
     *  - 404 – Not Found – Tournament was not found
     *  - 500 – Unexpected Server Error
     *
     * @param tournamentId The tournament ID.
     * @return A retrofit call instance for the request.
     */
    @GET("api/matches")
    Observable<List<Match>> listMatches(@Query("tournament") int tournamentId);


    /**
     * Gets the player listing for a tournament.
     *
     * Error Codes:
     *  - 500 – Unexpected Server Error
     *
     * Note: Empty [] means there are no players.
     *
     * @param tournamentId The tournament ID.
     * @return A retrofit call instance for the request.
     */
    @GET("api/players/")
    Observable<List<Player>> listPlayers(@Query("tournament") int tournamentId);

    /**
     * Gets the full player listing.
     *
     * Error Codes:
     *  - 500 – Unexpected Server Error
     *
     * Note: Empty [] means there are no players.
     *
     * @return A retrofit call instance for the request.
     */
    @GET("api/players/")
    Observable<List<Player>> listPlayers();

    /**
     * Updates a match.
     *
     * Status Codes:
     *  - 404 – Not Found – returned if the match ID or either of the player ID’s can’t be found.
     *  - 409 – Conflict – The match has already been finished.
     *  - 500 – Unexpected Server Error
     *
     * @param matchUpdate The match update.
     * @return A retrofit call instance for the request.
     */
    @PUT("api/matches/{id}")
    Observable<Void> updateMatch(@Path("id") int matchId, @Body MatchUpdate matchUpdate);

    /**
     * Creates a match.
     *
     * Status Codes:
     *  - 404 – Not Found – returned if the tournament ID or either of the player ID’s can’t be found.
     *  - 409 – Conflict – The match has already been finished.
     *  - 500 – Unexpected Server Error
     *
     * @param matchRequest The match request.
     * @return A retrofit call instance for the request.
     */
    @POST("api/matches")
    Observable<Match> createMatch(@Body MatchRequest matchRequest);
}
