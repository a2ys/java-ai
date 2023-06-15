package com.aayushshukla.chess;

import java.util.Scanner;

public class Main {
    protected static final Board board = new Board();
    protected static final Engine engine = new Engine();
    protected static final Scanner scanner = new Scanner(System.in);
    protected static boolean running = true;

    public static void mainloop() throws KingCapturedError {
        board.displayBoard();
        System.out.print("Enter the move ID of the move you want to make: ");
        while (running) {
            String input = scanner.next();

            if (input.equals("quit")) {
                running = false;
            } else if (input.equals("undo")) {
                engine.undoMove(false, board.getBoard());
                System.out.println("Last move was undone!");
                board.displayBoard();
                System.out.print("Enter the move ID of the move you want to make: ");
                continue;
            }

            int rankFrom = input.charAt(0) - '0';
            int fileFrom = input.charAt(1) - '0';
            int rankTo = input.charAt(2) - '0';
            int fileTo = input.charAt(3) - '0';

            Move move = new Move(new int[]{rankFrom, fileFrom}, new int[]{rankTo, fileTo}, board.getBoard());
            boolean valid = engine.makeMove(move, false, board, board.getBoard());

            board.displayBoard();

            if (valid) {
                System.out.println("Move was made, valid!");
            } else {
                System.out.println("Move was not made, invalid!");
            }

            System.out.print("Enter the move ID of the move you want to make: ");
        }
    }

    public static void main(String[] args) throws InvalidFENError, KingCapturedError {
        engine.initialize();
        board.initialize(engine);

        mainloop();
    }
}
