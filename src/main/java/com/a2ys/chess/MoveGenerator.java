package com.a2ys.chess;

import com.a2ys.chess.piece.*;

import java.util.ArrayList;

public class MoveGenerator {
    private ArrayList<Move> diagonalMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        for (int i = rank + 1; i < 8; i++) {
            if (file + (i - rank) <= 7) {
                Move move = new Move(new int[]{rank, file}, new int[]{i, file + (i - rank)}, board);
                if (board[i][file + (i - rank)] instanceof WhiteSpace) {
                    moves.add(move);
                } else if (board[i][file + (i - rank)].getColor().equals(piece.oppositeColor())) {
                    moves.add(move);
                    break;
                } else {
                    break;
                }
            }
        }

        for (int i = rank + 1; i < 8; i++) {
            if (file - (i - rank) >= 0) {
                Move move = new Move(new int[]{rank, file}, new int[]{i, file - (i - rank)}, board);
                if (board[i][file - (i - rank)] instanceof WhiteSpace) {
                    moves.add(move);
                } else if (board[i][file - (i - rank)].getColor().equals(piece.oppositeColor())) {
                    moves.add(move);
                    break;
                } else {
                    break;
                }
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            if (file + (rank - i) <= 7) {
                Move move = new Move(new int[]{rank, file}, new int[]{i, file + (rank - i)}, board);
                if (board[i][file + (rank - i)] instanceof WhiteSpace) {
                    moves.add(move);
                } else if (board[i][file + (rank - i)].getColor().equals(piece.oppositeColor())) {
                    moves.add(move);
                    break;
                } else {
                    break;
                }
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            if (file - (rank - i) >= 0) {
                Move move = new Move(new int[]{rank, file}, new int[]{i, file - (rank - i)}, board);
                if (board[i][file - (rank - i)] instanceof WhiteSpace) {
                    moves.add(move);
                } else if (board[i][file - (rank - i)].getColor().equals(piece.oppositeColor())) {
                    moves.add(move);
                    break;
                } else {
                    break;
                }
            }
        }

        return moves;
    }

    private ArrayList<Move> straightMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        for (int i = rank + 1; i < 8; i++) {
            Move move = new Move(new int[]{rank, file}, new int[]{i, file}, board);
            if (board[i][file] instanceof WhiteSpace) {
                moves.add(move);
            } else if (board[i][file].getColor().equals(piece.oppositeColor())) {
                moves.add(move);
                break;
            } else {
                break;
            }
        }

        for (int i = rank - 1; i > -1; i--) {
            Move move = new Move(new int[]{rank, file}, new int[]{i, file}, board);
            if (board[i][file] instanceof WhiteSpace) {
                moves.add(move);
            } else if (board[i][file].getColor().equals(piece.oppositeColor())) {
                moves.add(move);
                break;
            } else {
                break;
            }
        }

        for (int i = file + 1; i < 8; i++) {
            Move move = new Move(new int[]{rank, file}, new int[]{rank, i}, board);
            if (board[rank][i] instanceof WhiteSpace) {
                moves.add(move);
            } else if (board[rank][i].getColor().equals(piece.oppositeColor())) {
                moves.add(move);
                break;
            } else {
                break;
            }
        }

        for (int i = file - 1; i > -1; i--) {
            Move move = new Move(new int[]{rank, file}, new int[]{rank, i}, board);
            if (board[rank][i] instanceof WhiteSpace) {
                moves.add(move);
            } else if (board[rank][i].getColor().equals(piece.oppositeColor())) {
                moves.add(move);
                break;
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<Move> pawnMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        if (piece.getColor().equals("white")) {
            if (rank > 0) {
                if (board[rank - 1][file] instanceof WhiteSpace) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file}, board));
                    if (rank == 6) {
                        if (board[rank - 2][file] instanceof WhiteSpace) {
                            moves.add(new Move(new int[]{rank, file}, new int[]{rank - 2, file}, board));
                        }
                    }
                }
            }

            if (file - 1 >= 0) {
                if (!(board[rank - 1][file - 1] instanceof WhiteSpace) && !(board[rank - 1][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file - 1}, board));
                }
            }

            if (file + 1 <= 7) {
                if (!(board[rank - 1][file + 1] instanceof WhiteSpace) && !(board[rank - 1][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file + 1}, board));
                }
            }
        } else if (piece.getColor().equals("black")) {
            if (rank < 7) {
                if (board[rank + 1][file] instanceof WhiteSpace) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file}, board));
                    if (rank == 1) {
                        if (board[rank + 2][file] instanceof WhiteSpace) {
                            moves.add(new Move(new int[]{rank, file}, new int[]{rank + 2, file}, board));
                        }
                    }
                }
            }

            if (file - 1 >= 0) {
                if (!(board[rank + 1][file - 1] instanceof WhiteSpace) && !(board[rank + 1][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file - 1}, board));
                }
            }

            if (file + 1 <= 7) {
                if (!(board[rank + 1][file + 1] instanceof WhiteSpace) && !(board[rank + 1][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file + 1}, board));
                }
            }
        }

        return moves;
    }

    private ArrayList<Move> knightMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        if (file + 2 <= 7) {
            if (rank + 1 <= 7) {
                if (!(board[rank + 1][file + 2].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file + 2}, board));
                }
            }
            if (rank - 1 >= 0) {
                if (!(board[rank - 1][file + 2].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file + 2}, board));
                }
            }
        }

        if (file - 2 >= 0) {
            if (rank + 1 <= 7) {
                if (!(board[rank + 1][file - 2].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file - 2}, board));
                }
            }
            if (rank - 1 >= 0) {
                if (!(board[rank - 1][file - 2].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file - 2}, board));
                }
            }
        }

        if (rank + 2 <= 7) {
            if (file + 1 <= 7) {
                if (!(board[rank + 2][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 2, file + 1}, board));
                }
            }
            if (file - 1 >= 0) {
                if (!(board[rank + 2][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 2, file - 1}, board));
                }
            }
        }

        if (rank - 2 >= 0) {
            if (file + 1 <= 7) {
                if (!(board[rank - 2][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 2, file + 1}, board));
                }
            }
            if (file - 1 >= 0) {
                if (!(board[rank - 2][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 2, file - 1}, board));
                }
            }
        }

        return moves;
    }

    private ArrayList<Move> queenMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves;

        moves = diagonalMoves(piece, board);
        moves.addAll(straightMoves(piece, board));

        return moves;
    }

    private ArrayList<Move> kingMoves(Pieces piece, Pieces[][] board) {
        ArrayList<Move> moves = new ArrayList<>();
        int rank = piece.getRank();
        int file = piece.getFile();

        if (file + 1 <= 7) {
            if (rank + 1 <= 7) {
                if (!(board[rank + 1][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file + 1}, board));
                }
            }
            if (rank - 1 >= 0) {
                if (!(board[rank - 1][file + 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file + 1}, board));
                }
            }
        }

        if (file - 1 >= 0) {
            if (rank + 1 <= 7) {
                if (!(board[rank + 1][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file - 1}, board));
                }
            }
            if (rank - 1 >= 0) {
                if (!(board[rank - 1][file - 1].getColor().equals(piece.getColor()))) {
                    moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file - 1}, board));
                }
            }
        }

        if (rank + 1 <= 7) {
            if (!(board[rank + 1][file].getColor().equals(piece.getColor()))) {
                moves.add(new Move(new int[]{rank, file}, new int[]{rank + 1, file}, board));
            }
        }
        if (rank - 1 >= 0) {
            if (!(board[rank - 1][file].getColor().equals(piece.getColor()))) {
                moves.add(new Move(new int[]{rank, file}, new int[]{rank - 1, file}, board));
            }
        }

        if (file + 1 <= 7) {
            if (!(board[rank][file + 1].getColor().equals(piece.getColor()))) {
                moves.add(new Move(new int[]{rank, file}, new int[]{rank, file + 1}, board));
            }
        }
        if (file - 1 >= 0) {
            if (!(board[rank][file - 1].getColor().equals(piece.getColor()))) {
                moves.add(new Move(new int[]{rank, file}, new int[]{rank, file - 1}, board));
            }
        }

        return moves;
    }

    public ArrayList<Move> pseudoLegalMoves(Pieces piece, Pieces[][] board) {
        if (piece instanceof Bishop) {
            return diagonalMoves(piece, board);
        } else if (piece instanceof Rook) {
            return straightMoves(piece, board);
        } else if (piece instanceof Queen) {
            return queenMoves(piece, board);
        } else if (piece instanceof King) {
            return kingMoves(piece, board);
        } else if (piece instanceof Knight) {
            return knightMoves(piece, board);
        } else if (piece instanceof Pawn) {
            return pawnMoves(piece, board);
        } else {
            return new ArrayList<>();
        }
    }
}
