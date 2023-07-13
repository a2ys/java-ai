package com.a2ys.chess;

import com.a2ys.chess.piece.*;

import java.util.*;

public class Engine {
    private boolean whiteToMove;
    private final int[] whiteKingPosition = new int[2];
    private final int[] blackKingPosition = new int[2];
    private HashMap<String, boolean[]> castleRights = new HashMap<>();
    private final ArrayList<Move> moveLog = new ArrayList<>();
    private final MoveGenerator moveGenerator = new MoveGenerator();
    public static List<List<List<Integer>>> ZobristTable = new ArrayList<>();
    private boolean lol = false;
    Board board;

    public void initialize() {
        whiteToMove = true;
        whiteKingPosition[0] = 7;
        whiteKingPosition[1] = 4;
        blackKingPosition[0] = 0;
        blackKingPosition[1] = 4;

        castleRights.put("white", new boolean[]{true, true});
        castleRights.put("black", new boolean[]{true, true});

        initializeZob();
        initTable();
    }

    public boolean makeMove(Move move, boolean testing, Board board, Pieces[][] boardArray) throws KingCapturedError {
        this.board = board;

        if (boardArray[move.getStartRank()][move.getStartFile()].getColor().equals(boardArray[move.getEndRank()][move.getEndFile()].getColor())) {
            return false;
        } else if ((boardArray[move.getStartRank()][move.getStartFile()].getColor().equals("white") && !whiteToMove)
                || (boardArray[move.getStartRank()][move.getStartFile()].getColor().equals("black") && whiteToMove)) {
            return false;
        } else {
            Pieces piece = move.getPieceMoved();

            if (!testing) {
                ArrayList<Move> pseudoLegalMoves = moveGenerator.pseudoLegalMoves(piece, boardArray);
                ArrayList<Move> legalMoves = getLegalMoves(pseudoLegalMoves, board, boardArray);

//                if (legalMoves.size() == 0) System.out.println("Here is a checkmate!");

                legalMoves.addAll(specialMoves(piece, boardArray));

                if (legalMoves.contains(move)) {
                    boardArray[move.getStartRank()][move.getStartFile()] = new WhiteSpace(move.getStartRank(), move.getStartFile());
                    boardArray[move.getEndRank()][move.getEndFile()] = piece;
                    piece.setRank(move.getEndRank());
                    piece.setFile(move.getEndFile());
                    piece.addToMoveHistory(move); // TODO - Change to stack if possible

                    if (move.getPieceCaptured() instanceof King) throw new KingCapturedError(Constants.kingCapturedErrorMessage);

                    // Implement removal of castle rights after castling
                    if (move.getMoveType().equals("c")) {
                        if (move.getStartFile() - move.getEndFile() < 0) {
                            boardArray[move.getStartRank()][5] = boardArray[move.getStartRank()][7];
                            boardArray[move.getStartRank()][5].setRank(move.getStartRank());
                            boardArray[move.getStartRank()][5].setFile(5);
                            boardArray[move.getStartRank()][5].addToMoveHistory(move);
                            boardArray[move.getStartRank()][7] = new WhiteSpace(move.getStartRank(), 7);
                        } else if (move.getStartFile() - move.getEndFile() > 0) {
                            boardArray[move.getStartRank()][3] = boardArray[move.getStartRank()][0];
                            boardArray[move.getStartRank()][3].setRank(move.getStartRank());
                            boardArray[move.getStartRank()][3].setFile(3);
                            boardArray[move.getStartRank()][3].addToMoveHistory(move);
                            boardArray[move.getStartRank()][0] = new WhiteSpace(move.getStartRank(), 0);
                        }
                    } else if (move.getMoveType().equals("e")) {
                        if (move.getPieceMoved().getColor().equals("white")) {
                            boardArray[move.getEndRank() + 1][move.getEndFile()] = new WhiteSpace(move.getEndRank() + 1, move.getEndFile());
                        } else if (move.getPieceMoved().getColor().equals("black")) {
                            boardArray[move.getEndRank() - 1][move.getEndFile()] = new WhiteSpace(move.getEndRank() - 1, move.getEndFile());
                        }
                    }

                    if (move.getPieceMoved().getAlpha().equals("K")) {
                        whiteKingPosition[0] = move.getEndRank();
                        whiteKingPosition[1] = move.getEndFile();
                    } else if (move.getPieceMoved().getAlpha().equals("k")) {
                        blackKingPosition[0] = move.getEndRank();
                        blackKingPosition[1] = move.getEndFile();
                    }

                    if (move.getPieceMoved().getColor().equals(move.getPieceCaptured().getColor())) System.out.println("This move was illegal!");

                    moveLog.add(move);

                    int hashValue = computeHash(boardArray);

                    if (lol) {
                        System.out.println(hashValue);
                    }

                    whiteToMove = !whiteToMove;
                }
            } else {
                if (moveGenerator.pseudoLegalMoves(piece, boardArray).contains(move)) {
                    boardArray[move.getStartRank()][move.getStartFile()] = new WhiteSpace(move.getStartRank(), move.getStartFile());
                    boardArray[move.getEndRank()][move.getEndFile()] = piece;
                    piece.setRank(move.getEndRank());
                    piece.setFile(move.getEndFile());

                    if (move.getPieceMoved().getAlpha().equals("K")) {
                        whiteKingPosition[0] = move.getEndRank();
                        whiteKingPosition[1] = move.getEndFile();
                    } else if (move.getPieceMoved().getAlpha().equals("k")) {
                        blackKingPosition[0] = move.getEndRank();
                        blackKingPosition[1] = move.getEndFile();
                    }

                    moveLog.add(move);
                    whiteToMove = !whiteToMove;
                }
            }
            return true;
        }
    }

    public void undoMove(boolean testing, Pieces[][] board) {
        int numberOfMoves = moveLog.size();

        if (numberOfMoves > 0) {
            Move lastMove = moveLog.remove(numberOfMoves - 1);
            board[lastMove.getStartRank()][lastMove.getStartFile()] = lastMove.getPieceMoved();
            board[lastMove.getStartRank()][lastMove.getStartFile()].setRank(lastMove.getStartRank());
            board[lastMove.getStartRank()][lastMove.getStartFile()].setFile(lastMove.getStartFile());

            // Implement revival of castle rights once undo castling is performed
            if (lastMove.getMoveType().equals("c")) {
                int specialRank = lastMove.getSpecialPos()[0];
                int specialFile = lastMove.getSpecialPos()[1];
                Pieces specialPiece = lastMove.getExtraPiece();

                board[specialPiece.getRank()][specialPiece.getFile()] = new WhiteSpace(specialPiece.getRank(), specialPiece.getFile());
                board[specialRank][specialFile] = specialPiece;

                board[specialRank][specialFile].setRank(specialRank);
                board[specialRank][specialFile].setFile(specialFile);
                board[specialRank][specialFile].undoLastMove();
            } else if (lastMove.getMoveType().equals("e")) {
                Pieces specialPiece = lastMove.getExtraPiece();

                board[specialPiece.getRank()][specialPiece.getFile()] = specialPiece;
            }

            if (!testing) {
                board[lastMove.getStartRank()][lastMove.getStartFile()].undoLastMove();
            }

            board[lastMove.getEndRank()][lastMove.getEndFile()] = lastMove.getPieceCaptured();

            if (lastMove.getPieceMoved().getAlpha().equals("K")) {
                whiteKingPosition[0] = lastMove.getStartRank();
                whiteKingPosition[1] = lastMove.getStartFile();
            } else if (lastMove.getPieceMoved().getAlpha().equals("k")) {
                blackKingPosition[0] = lastMove.getStartRank();
                blackKingPosition[1] = lastMove.getStartFile();
            }

            whiteToMove = !whiteToMove;
        }
    }

    public ArrayList<Move> getLegalMoves(ArrayList<Move> pseudoLegalMoves, Board board, Pieces[][] boardArray) throws KingCapturedError {
        ArrayList<Move> legalMoves;
        legalMoves = pseudoLegalMoves;

        for (int i = legalMoves.size() - 1; i > -1; i--) {
            Move move = legalMoves.get(i);
            makeMove(move, true, board, boardArray);

            if (inCheck(move.getPieceMoved().getColor(), boardArray)) {
                legalMoves.remove(i);
            }

            undoMove(true, boardArray);
        }

        return legalMoves;
    }

    public String getActivePlayer() {
        if (whiteToMove) {
            return "white";
        } else {
            return "black";
        }
    }

    public void setActivePlayer(String activePlayer) {
        this.whiteToMove = activePlayer.equals("white");
    }

    public void setCastleRights(HashMap<String, boolean[]> castleRights) {
        this.castleRights = castleRights;
    }

    public boolean inCheck(String color, Pieces[][] board) {
        if (color.equals("white")) {
            return squareUnderAttack(whiteKingPosition[0], whiteKingPosition[1], "black", board);
        } else {
            return squareUnderAttack(blackKingPosition[0], blackKingPosition[1], "white", board);
        }
    }

    public boolean isCheckmate(String color, Pieces[][] board) throws KingCapturedError {
        if (!inCheck(color, board)) return false;

        for (Pieces[] row: board) {
            for (Pieces piece: row) {
                if (!piece.getColor().equals(color)) {
                    continue;
                }

                ArrayList<Move> pseudoLegalMoves = moveGenerator.pseudoLegalMoves(piece, board);
                ArrayList<Move> legalMoves = getLegalMoves(pseudoLegalMoves, getBoard(), board);

                if (legalMoves.size() != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Deprecated
    // This algorithm is naive and very slow, and thus discontinued.
    public boolean squareUnderAttack(int rank, int file, Pieces[][] board) {
        Pieces attackedPiece = board[rank][file];
        for (Pieces[] row : board) {
            for (Pieces piece : row) {
                if (piece.getColor().equals(attackedPiece.oppositeColor())) {
                    ArrayList<Move> pseudoLegalMoves = moveGenerator.pseudoLegalMoves(piece, board);

                    for (Move move : pseudoLegalMoves) {
                        if (move.getPieceCaptured().equals(attackedPiece)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean squareUnderAttack(int rank, int file, String enemyColor, Pieces[][] board) {
        String friendlyColor = (enemyColor.equals("black")) ? "white" : "black";

        for (int i = rank + 1; i < 8; i++) {
            if (board[i][file].getColor().equals(friendlyColor)) {
                break;
            } else if (board[i][file].getColor().equals(enemyColor)) {
                if (board[i][file] instanceof Rook || board[i][file] instanceof Queen) {
                    return true;
                } else {
                    break;
                }
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            if (board[i][file].getColor().equals(friendlyColor)) {
                break;
            } else if (board[i][file].getColor().equals(enemyColor)) {
                if (board[i][file] instanceof Rook || board[i][file] instanceof Queen) {
                    return true;
                } else {
                    break;
                }
            }
        }

        for (int i = file + 1; i < 8; i++) {
            if (board[rank][i].getColor().equals(friendlyColor)) {
                break;
            } else if (board[rank][i].getColor().equals(enemyColor)) {
                if (board[rank][i] instanceof Rook || board[rank][i] instanceof Queen) {
                    return true;
                } else {
                    break;
                }
            }
        }

        for (int i = file - 1; i > -1; i--) {
            if (board[rank][i].getColor().equals(friendlyColor)) {
                break;
            } else if (board[rank][i].getColor().equals(enemyColor)) {
                if (board[rank][i] instanceof Rook || board[rank][i] instanceof Queen) {
                    return true;
                } else {
                    break;
                }
            }
        }

        for (int i = rank + 1; i < 8; i++) {
            if (file + (i - rank) <= 7) {
                if (board[i][file + (i - rank)].getColor().equals(friendlyColor)) {
                    break;
                } else if (board[i][file + (i - rank)].getColor().equals(enemyColor)) {
                    if (board[i][file + (i - rank)] instanceof Queen || board[i][file + (i - rank)] instanceof Bishop) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        for (int i = rank + 1; i < 8; i++) {
            if (file - (i - rank) >= 0) {
                if (board[i][file - (i - rank)].getColor().equals(friendlyColor)) {
                    break;
                } else if (board[i][file - (i - rank)].getColor().equals(enemyColor)) {
                    if (board[i][file - (i - rank)] instanceof Queen || board[i][file - (i - rank)] instanceof Bishop) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            if (file + (rank - i) <= 7) {
                if (board[i][file + (rank - i)].getColor().equals(friendlyColor)) {
                    break;
                } else if (board[i][file + (rank - i)].getColor().equals(enemyColor)) {
                    if (board[i][file + (rank - i)] instanceof Queen || board[i][file + (rank - i)] instanceof Bishop) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            if (file - (rank - i) >= 0) {
                if (board[i][file - (rank - i)].getColor().equals(friendlyColor)) {
                    break;
                } else if (board[i][file - (rank - i)].getColor().equals(enemyColor)) {
                    if (board[i][file - (rank - i)] instanceof Queen || board[i][file - (rank - i)] instanceof Bishop) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        if (file + 2 <= 7) {
            if (rank + 1 <= 7) {
                if (board[rank + 1][file + 2].getColor().equals(enemyColor) && board[rank + 1][file + 2] instanceof Knight) {
                    return true;
                }
            }
            if (rank - 1 >= 0) {
                if (board[rank - 1][file + 2].getColor().equals(enemyColor) && board[rank - 1][file + 2] instanceof Knight) {
                    return true;
                }
            }
        }

        if (file - 2 >= 0) {
            if (rank + 1 <= 7) {
                if (board[rank + 1][file - 2].getColor().equals(enemyColor) && board[rank + 1][file - 2] instanceof Knight) {
                    return true;
                }
            }
            if (rank - 1 >= 0) {
                if (board[rank - 1][file - 2].getColor().equals(enemyColor) && board[rank - 1][file - 2] instanceof Knight) {
                    return true;
                }
            }
        }

        if (rank + 2 <= 7) {
            if (file + 1 <= 7) {
                if (board[rank + 2][file + 1].getColor().equals(enemyColor) && board[rank + 2][file + 1] instanceof Knight) {
                    return true;
                }
            }
            if (file - 1 >= 0) {
                if (board[rank + 2][file - 1].getColor().equals(enemyColor) && board[rank + 2][file - 1] instanceof Knight) {
                    return true;
                }
            }
        }

        if (rank - 2 >= 0) {
            if (file + 1 <= 7) {
                if (board[rank - 2][file + 1].getColor().equals(enemyColor) && board[rank - 2][file + 1] instanceof Knight) {
                    return true;
                }
            }
            if (file - 1 >= 0) {
                if (board[rank - 2][file - 1].getColor().equals(enemyColor) && board[rank - 2][file - 1] instanceof Knight) {
                    return true;
                }
            }
        }

        if (enemyColor.equals("black")) {
            if (rank > 0) {
                if (file - 1 >= 0) {
                    if (board[rank - 1][file - 1].getColor().equals(enemyColor) && board[rank - 1][file - 1] instanceof Pawn) {
                        return true;
                    }
                }

                if (file + 1 <= 7) {
                    if (board[rank - 1][file + 1].getColor().equals(enemyColor) && board[rank - 1][file + 1] instanceof Pawn) {
                        return true;
                    }
                }
            }
        } else {
            if (rank < 7) {
                if (file - 1 >= 0) {
                    if (board[rank + 1][file - 1].getColor().equals(enemyColor) && board[rank + 1][file - 1] instanceof Pawn) {
                        return true;
                    }
                }

                if (file + 1 <= 7) {
                    if (board[rank + 1][file + 1].getColor().equals(enemyColor) && board[rank + 1][file + 1] instanceof Pawn) {
                        return true;
                    }
                }
            }
        }

        if (file + 1 <= 7) {
            if (rank + 1 <= 7) {
                if (board[rank + 1][file + 1].getColor().equals(enemyColor) && board[rank + 1][file + 1] instanceof King) {
                    return true;
                }
            }
            if (rank - 1 >= 0) {
                if (board[rank - 1][file + 1].getColor().equals(enemyColor) && board[rank - 1][file + 1] instanceof King) {
                    return true;
                }
            }
        }

        if (file - 1 >= 0) {
            if (rank + 1 <= 7) {
                if (board[rank + 1][file - 1].getColor().equals(enemyColor) && board[rank + 1][file - 1] instanceof King) {
                    return true;
                }
            }
            if (rank - 1 >= 0) {
                if (board[rank - 1][file - 1].getColor().equals(enemyColor) && board[rank - 1][file - 1] instanceof King) {
                    return true;
                }
            }
        }

        if (rank + 1 <= 7) {
            if (board[rank + 1][file].getColor().equals(enemyColor) && board[rank + 1][file] instanceof King) {
                return true;
            }
        }
        if (rank - 1 >= 0) {
            if (board[rank - 1][file].getColor().equals(enemyColor) && board[rank - 1][file] instanceof King) {
                return true;
            }
        }

        if (file + 1 <= 7) {
            if (board[rank][file + 1].getColor().equals(enemyColor) && board[rank][file + 1] instanceof King) {
                return true;
            }
        }
        if (file - 1 >= 0) {
            if (board[rank][file - 1].getColor().equals(enemyColor) && board[rank][file - 1] instanceof King) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Move> specialMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        // Castling
        if (piece.getColor().equals("white")) {
            if (castleRights.get("white")[0]) {
                if (rank == 7 && file == 4) {
                    if (board[rank][file + 1] instanceof WhiteSpace && board[rank][file + 2] instanceof WhiteSpace) {
                        if (board[rank][file + 3] instanceof Rook && board[rank][file + 3].getColor().equals("white")) {
                            if ((piece.getMoveHistory().size() == 0) && (board[rank][file + 3].getMoveHistory().size() == 0)) {
                                if (!(inCheck("white", board)) && !(squareUnderAttack(rank, file + 1, "black", board)) && !(squareUnderAttack(rank, file + 2, "black", board))) {
                                    Move move = new Move(new int[]{rank, file}, new int[]{rank, file + 2}, board);
                                    move.setMoveType("c");
                                    move.setExtraPiece(board[rank][file + 3]);
                                    move.setSpecialPos(new int[]{rank, file + 3});
                                    moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
            if (castleRights.get("white")[1]) {
                if (rank == 7 && file == 4) {
                    if (board[rank][file - 1] instanceof WhiteSpace && board[rank][file - 2] instanceof WhiteSpace && board[rank][file - 3] instanceof WhiteSpace) {
                        if (board[rank][file - 4] instanceof Rook && board[rank][file - 4].getColor().equals("white")) {
                            if ((piece.getMoveHistory().size() == 0) && (board[rank][file - 4].getMoveHistory().size() == 0)) {
                                if (!(inCheck("white", board)) && !(squareUnderAttack(rank, file - 1, "black", board)) && !(squareUnderAttack(rank, file - 2, "black", board))) {
                                    Move move = new Move(new int[]{rank, file}, new int[]{rank, file - 2}, board);
                                    move.setMoveType("c");
                                    move.setExtraPiece(board[rank][file - 4]);
                                    move.setSpecialPos(new int[]{rank, 0});
                                    moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        } else if (piece.getColor().equals("black")) {
            if (castleRights.get("black")[0]) {
                if (rank == 0 && file == 4) {
                    if (board[rank][file + 1] instanceof WhiteSpace && board[rank][file + 2] instanceof WhiteSpace) {
                        if (board[rank][file + 3] instanceof Rook && board[rank][file + 3].getColor().equals("black")) {
                            if ((piece.getMoveHistory().size() == 0) && (board[rank][file + 3].getMoveHistory().size() == 0)) {
                                if (!(inCheck("black", board)) && !(squareUnderAttack(rank, file + 1, "white", board)) && !(squareUnderAttack(rank, file + 2, "white", board))) {
                                    Move move = new Move(new int[]{rank, file}, new int[]{rank, file + 2}, board);
                                    move.setMoveType("c");
                                    move.setExtraPiece(board[rank][file + 3]);
                                    move.setSpecialPos(new int[]{rank, file + 3});
                                    moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
            if (castleRights.get("black")[1]) {
                if (rank == 0 && file == 4) {
                    if (board[rank][file - 1] instanceof WhiteSpace && board[rank][file - 2] instanceof WhiteSpace && board[rank][file - 3] instanceof WhiteSpace) {
                        if (board[rank][file - 4] instanceof Rook && board[rank][file - 4].getColor().equals("black")) {
                            if ((piece.getMoveHistory().size() == 0) && (board[rank][file - 4].getMoveHistory().size() == 0)) {
                                if (!(inCheck("black", board)) && !(squareUnderAttack(rank, file - 1, "white", board)) && !(squareUnderAttack(rank, file - 2, "white", board))) {
                                    Move move = new Move(new int[]{rank, file}, new int[]{rank, file - 2}, board);
                                    move.setMoveType("c");
                                    move.setExtraPiece(board[rank][file - 4]);
                                    move.setSpecialPos(new int[]{rank, 0});
                                    moves.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }

        // En-passant

        if (moveLog.size() > 0) {
            if (piece instanceof Pawn) {
                if ((Objects.equals(piece.getColor(), "white")) && (piece.getRank() == 3)) {
                    if (file > 0) {
                        Pieces enemy = board[rank][file - 1];
                        if ((enemy instanceof Pawn) && (moveLog.get(moveLog.size() - 1).getPieceMoved() == enemy) && enemy.getMoveHistory().size() == 1) {
                            Move specialMove = new Move(new int[]{rank, file}, new int[]{rank - 1, file - 1}, board);
                            specialMove.setMoveType("e");
                            specialMove.setExtraPiece(board[rank][file - 1]);
                            specialMove.setSpecialPos(new int[]{rank, file - 1});
                            moves.add(specialMove);
                        }
                    }
                    if (file < 7) {
                        Pieces enemy = board[rank][file + 1];
                        if ((enemy instanceof Pawn) && (moveLog.get(moveLog.size() - 1).getPieceMoved() == enemy) && enemy.getMoveHistory().size() == 1) {
                            Move specialMove = new Move(new int[]{rank, file}, new int[]{rank - 1, file + 1}, board);
                            specialMove.setMoveType("e");
                            specialMove.setExtraPiece(board[rank][file + 1]);
                            specialMove.setSpecialPos(new int[]{rank, file + 1});
                            moves.add(specialMove);
                        }
                    }
                } else if ((Objects.equals(piece.getColor(), "black")) && (piece.getRank() == 4)) {
                    if (file > 0) {
                        Pieces enemy = board[rank][file - 1];
                        if ((enemy instanceof Pawn) && (moveLog.get(moveLog.size() - 1).getPieceMoved() == enemy) && enemy.getMoveHistory().size() == 1) {
                            Move specialMove = new Move(new int[]{rank, file}, new int[]{rank + 1, file - 1}, board);
                            specialMove.setMoveType("e");
                            specialMove.setExtraPiece(board[rank][file - 1]);
                            specialMove.setSpecialPos(new int[]{rank, file - 1});
                            moves.add(specialMove);
                        }
                    }
                    if (file < 7) {
                        Pieces enemy = board[rank][file + 1];
                        if ((enemy instanceof Pawn) && (moveLog.get(moveLog.size() - 1).getPieceMoved() == enemy) && enemy.getMoveHistory().size() == 1) {
                            Move specialMove = new Move(new int[]{rank, file}, new int[]{rank + 1, file + 1}, board);
                            specialMove.setMoveType("e");
                            specialMove.setExtraPiece(board[rank][file + 1]);
                            specialMove.setSpecialPos(new int[]{rank, file + 1});
                            moves.add(specialMove);
                        }
                    }
                }
            }
        }

        return moves;
    }

    public static void initializeZob() {
        for (int i = 0; i < 8; i++) {
            ZobristTable.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {
                ZobristTable.get(i).add(new ArrayList<>());
                for (int k = 0; k < 12; k++) {
                    ZobristTable.get(i).get(j).add(0);
                }
            }
        }
    }

    public static long randomLong() {
        long min = 0L;
        long max = (long) Math.pow(2, 64);
        Random rnd = new Random();
        return rnd.nextLong() * (max - min) + min;
    }

    public static void initTable() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 12; k++) {
                    ZobristTable.get(i).get(j).set(k, (int) randomLong());
                }
            }
        }
    }

    public static int zobIndexOf(Pieces piece)
    {
        return switch (piece.getAlpha()) {
            case "P" -> 0;
            case "N" -> 1;
            case "B" -> 2;
            case "R" -> 3;
            case "Q" -> 4;
            case "K" -> 5;
            case "p" -> 6;
            case "n" -> 7;
            case "b" -> 8;
            case "r" -> 9;
            case "q" -> 10;
            case "k" -> 11;
            default -> -1;
        };
    }

    public static int computeHash(Pieces[][] board) {
        int zobHash = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieces piece = board[i][j];
                if (!piece.getColor().equals("empty")) {
                    zobHash ^= ZobristTable.get(i).get(j).get(zobIndexOf(piece));
                }
            }
        }

        return zobHash;
    }

    private Board getBoard() {
        return board;
    }
}
