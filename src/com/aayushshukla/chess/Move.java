package com.aayushshukla.chess;

import com.aayushshukla.chess.piece.Pieces;

import java.util.Arrays;
import java.util.Objects;

public class Move {
    protected final int startRank, startFile, endRank, endFile;
    protected int[] specialPos = new int[2];
    protected final Pieces pieceMoved, pieceCaptured;
    protected Pieces extraPiece;
    protected String moveType = "n";

    public Move(int[] startPos, int[] endPos, Pieces[][] board) {
        this.startRank = startPos[0];
        this.startFile = startPos[1];
        this.endRank = endPos[0];
        this.endFile = endPos[1];

        this.pieceMoved = board[startRank][startFile];
        this.pieceCaptured = board[endRank][endFile];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startRank == move.startRank && startFile == move.startFile && endRank == move.endRank && endFile == move.endFile && Arrays.equals(specialPos, move.specialPos) && pieceMoved.equals(move.pieceMoved) && pieceCaptured.equals(move.pieceCaptured) && Objects.equals(extraPiece, move.extraPiece) && moveType.equals(move.moveType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(startRank, startFile, endRank, endFile, pieceMoved, pieceCaptured, extraPiece, moveType);
        result = 31 * result + Arrays.hashCode(specialPos);
        return result;
    }

    public int getStartRank() {
        return startRank;
    }

    public int getStartFile() {
        return startFile;
    }

    public int getEndRank() {
        return endRank;
    }

    public int getEndFile() {
        return endFile;
    }

    public int[] getSpecialPos() {
        return specialPos;
    }

    public Pieces getPieceMoved() {
        return pieceMoved;
    }

    public Pieces getPieceCaptured() {
        return pieceCaptured;
    }

    public Pieces getExtraPiece() {
        return extraPiece;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setSpecialPos(int[] specialPos) {
        this.specialPos = specialPos;
    }

    public void setExtraPiece(Pieces extraPiece) {
        this.extraPiece = extraPiece;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public void printInfo() {
        System.out.println("The piece " + pieceMoved + " wants to go from (" + startRank + ", " + startFile + ") to (" + endRank + ", " + endFile + "), where (" + endRank + ", " + endFile + ") contains " + pieceCaptured + ".");
    }
}
