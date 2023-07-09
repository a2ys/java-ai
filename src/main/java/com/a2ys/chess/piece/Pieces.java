package com.a2ys.chess.piece;

import java.util.ArrayList;
import java.util.Objects;

import com.a2ys.chess.Move;

public class Pieces {
    private int rank, file;
    private final String color;
    private final ArrayList<Move> moveHistory = new ArrayList<>();
    protected String alpha;

    public Pieces(int rank, int file, String color) {
        this.rank = rank;
        this.file = file;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pieces pieces = (Pieces) o;
        return rank == pieces.rank && file == pieces.file && Objects.equals(color, pieces.color) && moveHistory.equals(pieces.moveHistory) && alpha.equals(pieces.alpha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file, color, moveHistory, alpha);
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public String getColor() {
        return color;
    }

    public String getAlpha() {
        return alpha;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public void addToMoveHistory(Move move) {
        moveHistory.add(move);
    }

    public void undoLastMove() {
        if (moveHistory.size() > 0) {
            moveHistory.remove(moveHistory.size() - 1);
        }
    }

    public String oppositeColor() {
        if (color.equals("empty")) {
            return color;
        }
        return (color.equals("white")) ? "black" : "white";
    }
}

