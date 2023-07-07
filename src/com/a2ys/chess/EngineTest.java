package com.a2ys.chess;

import com.a2ys.chess.piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
                    ArrayList<Move> moves = engine.getLegalMoves(moveGenerator.pseudoLegalMoves(piece, boardArray), board, boardArray);
                    moves.addAll(engine.specialMoves(piece, boardArray));
                    for (Move ignored : moves) {
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

    long totalCheckmates(int depth) throws KingCapturedError {
        Pieces[][] boardArray = board.getBoard();
        int totalCheckmates = 0;

        for (Pieces[] rank : boardArray) {
            for (Pieces piece : rank) {
                if (piece.getColor().equals(engine.getActivePlayer())) {
                    ArrayList<Move> moves = engine.getLegalMoves(moveGenerator.pseudoLegalMoves(piece, boardArray), board, boardArray);
                    moves.addAll(engine.specialMoves(piece, boardArray));
                    for (Move ignored : moves) {
                        if (depth == 0) {
                            Pieces[][] zeroBoardArray = board.getBoard();

                            for (Pieces[] zeroRank : zeroBoardArray) {
                                for (Pieces zeroPiece : zeroRank) {
                                    if (zeroPiece.getColor().equals(engine.getActivePlayer())) {
                                        ArrayList<Move> zeroMoves = engine.getLegalMoves(moveGenerator.pseudoLegalMoves(zeroPiece, zeroBoardArray), board, zeroBoardArray);
                                        zeroMoves.addAll(engine.specialMoves(zeroPiece, zeroBoardArray));

                                        for (Move zeroIgnored : zeroMoves) {
                                            engine.makeMove(zeroIgnored, false, board, zeroBoardArray);
                                            if (engine.isCheckmate(zeroPiece.getColor(), zeroBoardArray)) {
                                                System.out.println("found a checkmate");
                                                totalCheckmates += 1;
                                            }
                                            engine.undoMove(false, zeroBoardArray);
                                        }
                                    }
                                }
                            }
                        } else {
                            engine.makeMove(ignored, false, board, boardArray);
                            totalCheckmates += totalCheckmates(depth - 1);
                            engine.undoMove(false, boardArray);
                        }
                    }
                }
            }
        }
        return totalCheckmates;
    }
    
//    @Test
//    @DisplayName("Testing for 1 ply")
//    void testFirst() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
////        assertEquals(20L, totalMoves(1));
//        assertEquals(0L, totalCheckmates(1));
//    }
//
//    @Test
//    @DisplayName("Testing for 2 plies")
//    void testSecond() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
////        assertEquals(400L, totalMoves(2));
//        assertEquals(0L, totalCheckmates(2));
//
//    }
//
//    @Test
//    @DisplayName("Testing for 3 plies")
//    void testThird() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
////        assertEquals(8902L, totalMoves(3));
//        assertEquals(0L, totalCheckmates(3));
//    }
//
    @Test
    @DisplayName("Testing for 4 plies")
    void testFourth() throws InvalidFENError, KingCapturedError {
        board.initialize(engine);
        engine.initialize();

//        assertEquals(197281L, totalMoves(4));
        assertEquals(8L, totalCheckmates(4));
    }

//    @Test
//    @DisplayName("Testing for 5 plies")
//    void testFifth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(4865609L, totalMoves(5));
//    }
//
//    @Test
//    @DisplayName("Testing for 6 plies")
//    void testSixth() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(119060324L, totalMoves(6));
//    }

//    @Test
//    @DisplayName("Testing for 5 plies")
//    void testSeventh() throws InvalidFENError, KingCapturedError {
//        board.initialize(engine);
//        engine.initialize();
//
//        assertEquals(3195901860L, totalMoves(7));
//    }
}