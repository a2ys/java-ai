package com.ayushshukla.chess.Enums;

public enum Color {
    WHITE(1),
    BLACK(0),
    EMPTY(-1);

    private final int colorCode;

    public int getColorCode() {
        return colorCode;
    }

    Color(int colorCode) {
        this.colorCode = colorCode;
    }
}
