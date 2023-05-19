package com.ayushshukla.chess.Piece;

import java.util.Objects;

public class Knight extends Pieces {
    public Knight(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "N";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "n";
        }
    }
}
