package com.aayushshukla.chess.Piece;

import java.util.Objects;

public class King extends Pieces {
    public King(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "K";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "k";
        }
    }
}
