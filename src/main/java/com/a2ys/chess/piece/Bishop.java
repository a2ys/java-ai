package com.a2ys.chess.piece;

import java.util.Objects;

public class Bishop extends Pieces {
    public Bishop(int rank, int file, String color) {
        super(rank, file, color);
        if (Objects.equals(color, "white")) {
            super.alpha = "B";
        } else if (Objects.equals(color, "black")) {
            super.alpha = "b";
        }
    }
}
