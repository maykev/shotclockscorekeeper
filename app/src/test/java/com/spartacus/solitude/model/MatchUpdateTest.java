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
public class MatchUpdateTest {

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

        MatchUpdate update = new MatchUpdate.Builder()
                .setPlayerScore(playerOne, 10)
                .setPlayerScore(playerTwo, 20)
                .build();

        assertNotNull(update);
    }

    @Test
    public void testMissingScores() {
        exception.expect(IllegalStateException.class);

        MatchUpdate update = new MatchUpdate.Builder()
                .setTable(10)
                .setPlayerScore(playerTwo, 20)
                .build();

        assertNotNull(update);
    }

    @Test
    public void testDuplicatePlayer() {
        exception.expect(IllegalStateException.class);

        MatchUpdate update = new MatchUpdate.Builder()
                .setTable(10)
                .setPlayerScore(playerOne, 20)
                .setPlayerScore(playerOne, 20)
                .build();

        assertNotNull(update);
    }

    @Test
    public void testTooManyScores() {
        Player playerThree = mock(Player.class);
        when(playerThree.getId()).thenReturn(3);

        exception.expect(IllegalStateException.class);

        MatchUpdate update = new MatchUpdate.Builder()
                .setTable(10)
                .setPlayerScore(playerOne, 1)
                .setPlayerScore(playerTwo, 2)
                .setPlayerScore(playerThree, 3)
                .build();

        assertNotNull(update);
    }
}