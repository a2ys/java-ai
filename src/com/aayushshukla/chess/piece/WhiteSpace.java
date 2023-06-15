package com.aayushshukla.chess.piece;

public class WhiteSpace extends Pieces {
    public WhiteSpace(int rank, int file) {
        super(rank, file, null);
        super.alpha = " ";
    }

    @Override
    public String getColor() {
        return "empty";
    }
}