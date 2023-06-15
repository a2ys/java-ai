package com.aayushshukla.chess.Piece;

import java.util.Objects;

public class Pawn extends Pieces {
    public Pawn(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "P";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "p";
        }
    }
}
