package io.metry;

import lombok.Data;


@Data
public class Board {
    public static final int BOARD_LENGTH = 15;
    private Square[][] squares;

    public Board() {
        squares = new Square[BOARD_LENGTH][BOARD_LENGTH];
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                squares[i][j] = new Square();
            }
        }
    }

    public void placeTile(Tile tile, int x, int y) {
        squares[x][y].setTile(tile);
    }
}
