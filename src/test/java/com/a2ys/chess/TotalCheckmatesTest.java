package com.a2ys.chess;

import com.a2ys.chess.piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TotalCheckmatesTest {
    private final Board board = new Board();
    private final Engine engine = new Engine();
    private final MoveGenerator moveGenerator = new MoveGenerator();

    long totalCheckmates(int depth) throws KingCapturedError {
        Pieces[][] boardArray = board.getBoard();
        long total = 0;

        if (depth == 0) {
            if (engine.isCheckmate(engine.getActivePlayer(), boardArray)) {
                total += 1;

                return total;
            }
        }

        for (Pieces[] rank : boardArray) {
            for (Pieces piece : rank) {
                if (piece.getColor().equals(engine.getActivePlayer())) {
                    ArrayList<Move> moves = engine.getLegalMoves(moveGenerator.pseudoLegalMoves(piece, boardArray), board, boardArray);
                    moves.addAll(engine.specialMoves(piece, boardArray));
                    for (Move ignored : moves) {
                        if (depth == 0) {
                            if (engine.isCheckmate(engine.getActivePlayer(), boardArray)) {
                                System.out.println("Found a checkmate!");
                                total += 1;
                            }
                        } else {
                            engine.makeMove(ignored, false, board, boardArray);
                            total += totalCheckmates(depth - 1);
                            engine.undoMove(false, boardArray);
                        }
                    }
                }
            }
        }
        return total;
    }

    @Test
    @DisplayName("Testing for 1 ply")
    public void testFirst() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(0L, totalCheckmates(1));
    }

    @Test
    @DisplayName("Testing for 2 plies")
    public void testSecond() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(0L, totalCheckmates(2));

    }

    @Test
    @DisplayName("Testing for 3 plies")
    public void testThird() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(0L, totalCheckmates(3));
    }

    @Test
    @DisplayName("Testing for 4 plies")
    public void testFourth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(8L, totalCheckmates(4));
    }

    @Test
    @DisplayName("Testing for 5 plies")
    public void testFifth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(347L, totalCheckmates(5));
    }

    @Test
    @DisplayName("Testing for 6 plies")
    void testSixth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(10828L, totalCheckmates(6));
    }
}