package io.metry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
class Tile {
    private Character letter;
    private Integer points;
    private Set<String> usedBy;
}
