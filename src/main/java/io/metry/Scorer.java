package io.metry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Scorer {
    private Board board;

    public Scorer(Board board) {
        this.board = board;
    }

    public String calculateWinner() {
        int maxScore = Integer.MIN_VALUE;
        String winner = null;
        for (Map.Entry<String, Integer> scoreOfPlayer :
                calculateScores().entrySet()) {
            if (scoreOfPlayer.getValue() >= maxScore) {
                winner = scoreOfPlayer.getKey();
                maxScore = scoreOfPlayer.getValue();
            }
        }
        return winner;
    }

    public Map<String, Integer> calculateScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            int index = i;
            mergeMaps(calculateScores(board.getSquares()[i]), scores);
            mergeMaps(calculateScores(Arrays.stream(board.getSquares()).map(row -> row[index]).toArray(Square[]::new)), scores);
        }
        return scores;
    }


    private void mergeMaps(Map<String, Integer> source, Map<String, Integer> destination) {
        source.forEach((sourceKey, sourceValue) -> destination.compute(
                sourceKey,
                (destinationKey, destinationValue) -> destinationValue == null ? sourceValue : sourceValue + destinationValue
        ));
    }

    private Map<String, Integer> calculateScores(Square[] rowOrColumn) {
        Map<String, Integer> scoresOfPreviousSquare = new HashMap<>();
        Map<String, Integer> scoresOfRowOrColumn = new HashMap<>();
        Map<String, Integer> contiguousSquaresCountForPlayer = new HashMap<>();
        for (Square square : rowOrColumn) {
            square.getPlayersUsingThisSquare().forEach(player -> contiguousSquaresCountForPlayer.compute(
                            player,
                            (key, value) -> value == null ? 1 : value + 1)
            );
            contiguousSquaresCountForPlayer.forEach((player, contiguousTilesCount) -> {
                if (contiguousTilesCount > 1) {
                    scoresOfRowOrColumn.compute(player, (key, value) ->
                            value == null ? scoresOfPreviousSquare.get(player) : value + scoresOfPreviousSquare.get(player)
                    );
                }
            });
            scoresOfPreviousSquare.clear();
            square.getPlayersUsingThisSquare().forEach(player -> scoresOfPreviousSquare.put(player, square.getPoints()));
            contiguousSquaresCountForPlayer.replaceAll((player, contiguousTilesCount) ->
                    !square.getPlayersUsingThisSquare().contains(player) ? 0 : contiguousTilesCount
            );
        }
        contiguousSquaresCountForPlayer.forEach((player, contiguousTilesCount) -> {
            if (contiguousTilesCount > 1) {
                scoresOfRowOrColumn.compute(player, (key, value) ->
                        value == null ? scoresOfPreviousSquare.get(player) : value + scoresOfPreviousSquare.get(player)
                );
            }
        });
        return scoresOfRowOrColumn;
    }
}
