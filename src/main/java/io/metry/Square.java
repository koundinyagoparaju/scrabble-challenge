package io.metry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Square {
    private Tile tile;

    private boolean isTilePlaced() {
        return tile != null;
    }
    Set<String> getPlayersUsingThisSquare() {
        if(isTilePlaced()) {
            return tile.getUsedBy();
        } else {
            return Collections.emptySet();
        }
    }

    int getPoints() {
        if(isTilePlaced()) {
            return tile.getPoints();
        } else {
            return 0;
        }
    }
}
