package game;

import java.util.*;

import static game.MasterMind.Response.*;
import java.awt.Color;
import java.util.stream.IntStream;
import java.util.function.IntFunction;
import java.util.function.Function;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;

public class MasterMind {
    public enum Status {LOST, IN_PROGRESS, WON}

    public enum Response {NO_MATCH, MATCH, POSITIONAL_MATCH}

    public static final int SIZE = 6;
    private List<Color> selection;
    protected int tries = 0;
    private Status gameStatus = Status.IN_PROGRESS;

    public MasterMind() {
        long currentTime = System.currentTimeMillis();
        Random rand = new Random(currentTime);
        selection = generateRandomColors(rand);
    }

    protected MasterMind(List<Color> colors) {
        selection = colors;
    }

    public Status getGameStatus() {
        return gameStatus;
    }

    public List<Color> getAvailableColors() {
        return Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.PINK, Color.GRAY, Color.CYAN, Color.WHITE, Color.YELLOW, Color.MAGENTA);
    }

    public List<Color> getSelection() {
        return selection;
    }

    public Map<Response, Long> guess(List<Color> userInput) {

        tries++;

        IntFunction<Response> computeMatchAtPosition = index ->
                selection.get(index) == userInput.get(index) ? POSITIONAL_MATCH :
                        userInput.contains(selection.get(index)) ? MATCH : NO_MATCH;

        Map<Response, Long> response =
                IntStream.range(0, SIZE)
                        .mapToObj(computeMatchAtPosition)
                        .collect(groupingBy(Function.identity(), counting()));

        response.computeIfAbsent(NO_MATCH, key -> 0L);
        response.computeIfAbsent(MATCH, key -> 0L);
        response.computeIfAbsent(POSITIONAL_MATCH, key -> 0L);

        updateGameStatus(response);
        return response;
    }

    private void updateGameStatus(Map<Response, Long> response) {
        if(response.get(Response.POSITIONAL_MATCH) == SIZE)
            gameStatus = Status.WON;
        else if(tries >= 15)
            gameStatus = Status.LOST;
    }

    protected List<Color> generateRandomColors(Random random) {
        List<Color> randomColors = new ArrayList<>(getAvailableColors());

        while(randomColors.size() > SIZE){
            randomColors.remove(random.nextInt(SIZE));
        }

        Collections.shuffle(randomColors, random);
        return randomColors;
    }
}
