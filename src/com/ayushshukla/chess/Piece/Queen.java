package com.ayushshukla.chess.Piece;

import java.util.Objects;

public class Queen  extends Pieces {
    public Queen(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "Q";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "q";
        }
    }
}
