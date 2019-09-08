package io.metry;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

class Scorer {
    private Board board;

    Scorer(Board board) {
        this.board = board;
    }

    Optional<String> calculateWinner() {
        int maxScore = 0;
        String winner = null;
        for (Map.Entry<String, Integer> scoreOfPlayer :
                calculateScores().entrySet()) {
            if (scoreOfPlayer.getValue() > maxScore) {
                winner = scoreOfPlayer.getKey();
                maxScore = scoreOfPlayer.getValue();
            }
        }
        return Optional.ofNullable(winner);
    }

    Map<String, Integer> calculateScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            int index = i;
            mergeMaps(calculateScores(board.getSquares()[i]), scores);
            mergeMaps(calculateScores(Arrays.stream(board.getSquares()).map(row -> row[index]).toArray(Square[]::new)), scores);
        }
        return board.getAllPlayers().stream().map(player -> new SimpleEntry<String, Integer>(player, scores.getOrDefault(player, 0))).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
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
