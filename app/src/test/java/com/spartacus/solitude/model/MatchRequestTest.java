package com.spartacus.solitude.model;

import com.spartacus.solitude.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class MatchRequestTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Player playerOne;
    private Player playerTwo;

    @Before
    public void setup() {
        playerOne = mock(Player.class);
        when(playerOne.getId()).thenReturn(1);

        playerTwo = mock(Player.class);
        when(playerTwo.getId()).thenReturn(2);
    }

    @Test
    public void testMissingTable() {
        exception.expect(IllegalStateException.class);

        MatchRequest request = new MatchRequest.Builder()
                .setTournamentId(10)
                .setPlayers(playerOne, playerTwo)
                .build();

        assertNotNull(request);
    }

    @Test
    public void testMissingTournamentId() {
        exception.expect(IllegalStateException.class);

        MatchRequest request = new MatchRequest.Builder()
                .setTable(1)
                .setPlayers(playerOne, playerTwo)
                .build();

        assertNotNull(request);
    }

    @Test
    public void testMissingPlayers() {
        exception.expect(IllegalStateException.class);

        MatchRequest request = new MatchRequest.Builder()
                .setTable(1)
                .setTournamentId(10)
                .build();

        assertNotNull(request);
    }

    @Test
    public void testDuplicatePlayer() {
        exception.expect(IllegalArgumentException.class);

        MatchRequest request = new MatchRequest.Builder()
                .setTable(1)
                .setTournamentId(10)
                .setPlayers(playerOne, playerOne)
                .build();

        assertNotNull(request);
    }
}