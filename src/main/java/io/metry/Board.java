package io.metry;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class Board {
    public static final int BOARD_LENGTH = 15;
    private Square[][] squares;

    Board() {
        squares = new Square[BOARD_LENGTH][BOARD_LENGTH];
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                squares[i][j] = new Square();
            }
        }
    }

    void placeTile(Tile tile, int x, int y) {
        if(x >= BOARD_LENGTH || y >= BOARD_LENGTH) throw new IllegalArgumentException("X, Y coordinates must not be greater than board length");
        squares[x][y].setTile(tile);
    }

    Set<String> getAllPlayers() {
        Set<String> players = new HashSet<>();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                players.addAll(squares[i][j].getPlayersUsingThisSquare());
            }
        }
        return players;
    }
}
