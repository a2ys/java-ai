package com.aayushshukla.chess;

import com.aayushshukla.chess.Piece.*;

import java.util.HashMap;

public class Board {
    private Engine engine;
    private Pieces[][] board;

    public void initialize(Engine engine) throws InvalidFENError {
        this.engine = engine;
        fenToBoard(Constants.initialFen);
    }

    public void fenToBoard(String fen) throws InvalidFENError {
        if (!(fen.contains("k")) || !(fen.contains("K"))) throw new InvalidFENError(Constants.invalidFENErrorMessage);

        Pieces[][] board = new Pieces[8][8];
        int rank = 0, file = 0;

        String boardArrangement = fen.split(" ")[0];
        for (int i = 0, n = boardArrangement.length(); i < n; i++) {
            char c = boardArrangement.charAt(i);
            if (c == '/') {
                rank += 1;
                file = 0;
            } else if (Character.isDigit(c)) {
                for (int j = 0; j < Character.getNumericValue(c); j++) {
                    board[rank][file] = new WhiteSpace(rank, file);
                    file += 1;
                }
            } else {
                switch (c) {
                    case 'K' -> board[rank][file] = new King(rank, file, "white");
                    case 'Q' -> board[rank][file] = new Queen(rank, file, "white");
                    case 'B' -> board[rank][file] = new Bishop(rank, file, "white");
                    case 'N' -> board[rank][file] = new Knight(rank, file, "white");
                    case 'R' -> board[rank][file] = new Rook(rank, file, "white");
                    case 'P' -> board[rank][file] = new Pawn(rank, file, "white");
                    case 'k' -> board[rank][file] = new King(rank, file, "black");
                    case 'q' -> board[rank][file] = new Queen(rank, file, "black");
                    case 'b' -> board[rank][file] = new Bishop(rank, file, "black");
                    case 'n' -> board[rank][file] = new Knight(rank, file, "black");
                    case 'r' -> board[rank][file] = new Rook(rank, file, "black");
                    case 'p' -> board[rank][file] = new Pawn(rank, file, "black");
                    default -> throw new InvalidFENError(Constants.invalidFENErrorMessage);
                }
                file += 1;
            }
        }

        if (board.length * board[0].length != 64) throw new InvalidFENError(Constants.invalidFENErrorMessage);

        String playerToMove = fen.split(" ")[1];
        if (playerToMove.equals("w")) {
            engine.setActivePlayer("white");
        } else if (playerToMove.equals("b")) {
            engine.setActivePlayer("black");
        }

        String castleRights = fen.split(" ")[2];
        HashMap<String, boolean[]> castleRightsDictionary = new HashMap<>();
        castleRightsDictionary.put("white", new boolean[]{false, false});
        castleRightsDictionary.put("black", new boolean[]{false, false});
        for (int i = 0, n = castleRights.length(); i < n; i++) {
            switch (castleRights.charAt(i)) {
                case 'K':
                    castleRightsDictionary.put("white", new boolean[]{true, castleRightsDictionary.get("white")[1]});
                case 'Q':
                    castleRightsDictionary.put("white", new boolean[]{castleRightsDictionary.get("white")[0], true});
                case 'k':
                    castleRightsDictionary.put("black", new boolean[]{true, castleRightsDictionary.get("black")[1]});
                case 'q':
                    castleRightsDictionary.put("black", new boolean[]{castleRightsDictionary.get("black")[0], true});
            }
        }

        setBoard(board);
    }

    public void displayBoard() {
        System.out.print('\n');
        System.out.print("   ");
        for (int i = 0; i < 49; i ++) {
            if (i % 6 == 0) {
                System.out.print("+");
            } else {
                System.out.print("-");
            }
        }
        System.out.print('\n');
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (file == 0) {
                    System.out.print(rank + "  |  ");
                }
                System.out.print(board[rank][file].getAlpha() + "  |  ");
            }
            System.out.print('\n');
            System.out.print("   ");
            for (int i = 0; i < 49; i ++) {
                if (i % 6 == 0) {
                    System.out.print("+");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print('\n');
        }

        for (int i = 0; i < 52; i++) {
            if (i == 0) {
                System.out.print(" ");
                continue;
            }
            if (i % 6 == 0) {
                System.out.print(i / 6 - 1);
            }
            else {
                System.out.print(" ");
            }
        }
        System.out.print("\n");
    }

    public Pieces[][] getBoard() {
        return board;
    }

    public void setBoard(Pieces[][] board) {
        this.board = board;
    }
}
