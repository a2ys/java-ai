package com.aayushshukla.chess.piece;

import java.util.Objects;

public class Rook extends Pieces {
    public Rook(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "R";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "r";
        }
    }
}
