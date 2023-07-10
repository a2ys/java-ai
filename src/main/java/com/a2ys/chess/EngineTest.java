package com.a2ys.chess;

import com.a2ys.chess.piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {
    @Nested
    @DisplayName("Checking for total moves")
    class TotalMovesTests {
        private final Board board = new Board();
        private final Engine engine = new Engine();
        private final MoveGenerator moveGenerator = new MoveGenerator();

        long totalMoves(int depth) throws KingCapturedError {
            Pieces[][] boardArray = board.getBoard();
            long total = 0;

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

        @Test
        @DisplayName("Testing for 6 plies")
        void testSixth() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(119060324L, totalMoves(6));
        }

        @Test
        @DisplayName("Testing for 7 plies")
        void testSeventh() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(3195901860L, totalMoves(7));
        }
    }

    @Nested
    @DisplayName("Checking for total checkmates")
    class CheckmateTests {
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
        void testFirst() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(0L, totalCheckmates(1));
        }

        @Test
        @DisplayName("Testing for 2 plies")
        void testSecond() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(0L, totalCheckmates(2));

        }

        @Test
        @DisplayName("Testing for 3 plies")
        void testThird() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(0L, totalCheckmates(3));
        }

        @Test
        @DisplayName("Testing for 4 plies")
        void testFourth() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(8L, totalCheckmates(4));
        }

        @Test
        @DisplayName("Testing for 5 plies")
        void testFifth() throws InvalidFENError, KingCapturedError {
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

        @Test
        @DisplayName("Testing for 7 plies")
        void testSeventh() throws InvalidFENError, KingCapturedError {
            board.initialize(engine);
            engine.initialize();

            assertEquals(435767L, totalCheckmates(7));
        }
    }
}
