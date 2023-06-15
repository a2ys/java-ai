package com.ayushshukla.chess;

import com.ayushshukla.chess.Piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {
    Board board = new Board();
    Engine engine = new Engine();
    MoveGenerator moveGenerator = new MoveGenerator();

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

//    @Test
//    @DisplayName("Testing for 6 plies")
//    void testSixth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(119060324L, totalMoves(6));
//    }
//
//    @Test
//    @DisplayName("Testing for 7 plies")
//    void testSeventh() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(3195901860L, totalMoves(7));
//    }
//
//    @Test
//    @DisplayName("Testing for 8 plies")
//    void testEighth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(84998978956L, totalMoves(8));
//    }

//    @Test
//    @DisplayName("Testing for 9 plies")
//    void testNinth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(2439530234167	, totalMoves(9));
//    }
//
//    @Test
//    @DisplayName("Testing for 10 plies")
//    void testTenth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(69352859712417, totalMoves(10));
//    }
}