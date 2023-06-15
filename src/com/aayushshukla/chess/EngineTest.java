package com.aayushshukla.chess;

import com.aayushshukla.chess.Piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    private final Board board = new Board();
    private final Engine engine = new Engine();
    private final MoveGenerator moveGenerator = new MoveGenerator();

    long totalMoves(int depth) throws KingCapturedError {
        Pieces[][] boardArray = board.getBoard();
        int total = 0;

        for (Pieces[] rank : boardArray) {
            for (Pieces piece : rank) {
                if (piece.getColor().equals(engine.getActivePlayer())) {
                    for (Move ignored : engine.getLegalMoves(moveGenerator.pseudoLegalMoves(piece, boardArray), board, boardArray)) {
                        if (depth == 1) {
                            total += 1;
                        } else {
                            engine.makeMove(ignored, false, board, boardArray);
                            total += totalMoves(depth - 1);
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
    void testFirst() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(20L, totalMoves(1));
    }

    @Test
    @DisplayName("Testing for 2 plies")
    void testSecond() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(400L, totalMoves(2));
    }

    @Test
    @DisplayName("Testing for 3 plies")
    void testThird() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(8902L, totalMoves(3));
    }

    @Test
    @DisplayName("Testing for 4 plies")
    void testFourth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(197281L, totalMoves(4));
    }

    @Test
    @DisplayName("Testing for 5 plies")
    void testFifth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

        assertEquals(4865609L, totalMoves(5));
    }
}