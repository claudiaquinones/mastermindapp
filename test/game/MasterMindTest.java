package game;

import java.awt.Color;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import game.MasterMind.Response;
import game.MasterMind.Status;

import static org.junit.jupiter.api.Assertions.*;

public class MasterMindTest {

    MasterMind masterMind;

    @BeforeEach
    void init() {
        List<Color> selection = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        masterMind = new MasterMind(selection);
    }

    @Test
    void canary() {
        assertTrue(true);
    }

    @Test
    void userGuessesNoColorCorrectly() {
        List<Color> userInput = Arrays.asList(Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(6, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(0, (long) response.get(Response.MATCH)),
                () -> assertEquals(0, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void userGuessesOneColorCorrectInWrongPosition() {
        List<Color> userInput = Arrays.asList(Color.PINK, Color.BLUE, Color.PINK, Color.PINK, Color.PINK, Color.PINK);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(5, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(0, (long) response.get(Response.MATCH)),
                () -> assertEquals(1, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void userGuessesAllColorsCorrectTwoColorsInWrongPosition() {
        List<Color> userInput = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE, Color.CYAN);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(0, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(2, (long) response.get(Response.MATCH)),
                () -> assertEquals(4, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void userGuessesFiveColorsCorrectAndOneColorInWrongPosition() {
        List<Color> userInput = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.CYAN);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(1, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(1, (long) response.get(Response.MATCH)),
                () -> assertEquals(4, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void userGuessesCorrectColorsReversedPositions() {
        List<Color> userInput = Arrays.asList(Color.CYAN, Color.WHITE, Color.BLACK, Color.GREEN, Color.BLUE, Color.RED);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(0, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(6, (long) response.get(Response.MATCH)),
                () -> assertEquals(0, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void colorDuplicated() {
        List<Color> userInput = Arrays.asList(Color.PINK, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(5, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(1, (long) response.get(Response.MATCH)),
                () -> assertEquals(0, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void colorDuplicatedWithAPositionalMatch() {
        List<Color> userInput = Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);

        Map<Response, Long> response = masterMind.guess(userInput);

        assertAll(
                () -> assertEquals(5, (long) response.get(Response.NO_MATCH)),
                () -> assertEquals(0, (long) response.get(Response.MATCH)),
                () -> assertEquals(1, (long) response.get(Response.POSITIONAL_MATCH)));
    }

    @Test
    void checkGameStatusAtStartOfGame() {
        assertEquals(Status.IN_PROGRESS, masterMind.getGameStatus());
    }

    @Test
    void checkGameStatusAfterIncorrectGuessCalledTwice() {
        List<Color> userInput = Arrays.asList(Color.RED, Color.GREEN, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);
        masterMind.guess(userInput);

        masterMind.guess(userInput);

        assertEquals(Status.IN_PROGRESS, masterMind.getGameStatus());
    }

    @Test
    void checkGameStatusAfterCorrectGuess() {
        List<Color> userInput = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        masterMind.guess(userInput);

        assertEquals(Status.WON, masterMind.getGameStatus());
    }

    @Test
    void checkGameStatusAfter20IncorrectGuesses() {
        masterMind.tries = 19;
        List<Color> userInput = Arrays.asList(Color.RED, Color.GREEN, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        masterMind.guess(userInput);

        assertEquals(Status.LOST, masterMind.getGameStatus());
    }

    @Test
    void checkGameStatusForIncorrectGuessAfterCorrectGuess() {
        List<Color> userInput1 = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);
        List<Color> userInput2 = Arrays.asList(Color.BLUE, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);
        masterMind.guess(userInput1);

        masterMind.guess(userInput2);

        assertEquals(Status.WON, masterMind.getGameStatus());
    }

    @Test
    void checkGameStatusForCorrectGuessOn20thGuess() {
        masterMind.tries = 19;
        List<Color> userInput = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        masterMind.guess(userInput);

        assertEquals(Status.WON, masterMind.getGameStatus());
    }

    @Test
    void toSuppressCoverageIssueForResponseEnum() {
        Response.values();
        Response.valueOf("NO_MATCH");
        assertTrue(true);
    }

    @Test
    void toSuppressCoverageIssueForStatusEnum() {
        Status.values();
        Status.valueOf("WON");
        assertTrue(true);
    }

    @Test
    void checkIfGenerateRandomColorsGeneratesTwoEqualSequencesWithSameSeed() {
        Random random1 = new Random(123);
        Random random2 = new Random(123);

        List<Color> randomList1 = masterMind.generateRandomColors(random1);
        List<Color> randomList2 = masterMind.generateRandomColors(random2);

        assertEquals(randomList1, randomList2);
    }

    @Test
    void checkIfGenerateRandomColorsGeneratesTwoDifferentSequencesWithDifferentSeed() {
        Random random1 = new Random(123);
        Random random2 = new Random(281);

        List<Color> randomList1 = masterMind.generateRandomColors(random1);
        List<Color> randomList2 = masterMind.generateRandomColors(random2);

        assertNotEquals(randomList1, randomList2);
    }

    @Test
    void checkIfGenerateRandomColorsIsCalledInConstructor() {
        AtomicBoolean called = new AtomicBoolean();
        MasterMind stub = new MasterMind() {

            protected List<Color> generateRandomColors(Random random) {
                called.set(true);
                return null;
            }
        };

        assertTrue(called.get());
    }

    @Test
    void testGetSelectionAtStartOfGame(){
        List<Color> selection = Arrays.asList(Color.RED, Color.MAGENTA, Color.BLUE, Color.BLACK, Color.GREEN, Color.CYAN);

        masterMind = new MasterMind(selection);

        assertEquals(selection, masterMind.getSelection());
    }
}


