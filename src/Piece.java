public class Piece {
    color pColor;
    type pType;
    String c;

    public Piece(color color, type type, String c) {
        this.c = c;
        this.pType = type;
        this.pColor = color;
    }

    public Piece(String c) {
        this.c = c;
        pColor = color.NULL;
        pType = type.EMPTY;
    }

    public String getChar() {
        return c;
    }

    public void setChar(String c) {
        this.c = c;
    }

    public Piece swap() {
        return new Piece(pColor != color.NULL ? (pColor == color.WHITE ? color.BLACK : color.WHITE) : color.NULL, pType, c);
    }

    @Override
    public String toString() {
        return pColor == color.WHITE ? Text.WHITE_BOLD + c : (Text.BLACK_BOLD + c);
    }

    public boolean equals(Piece piece) {
        return pColor == piece.pColor && pType == piece.pType;
    }

    public boolean sameType(Piece piece) {
        return pType == piece.pType;
    }

    public boolean sameColor(Piece piece) {
        return pColor == piece.pColor;
    }

    public boolean diffColor(Piece piece) {
        return pColor != piece.pColor && piece.pColor != color.NULL && pColor != color.NULL;
    }

    public boolean isEmpty() {
        return pType == type.EMPTY;
    }

    public color getColor() {
        return pColor;
    }

    public type getType() {
        return pType;
    }

    public Piece White() {
        return new Piece(color.WHITE, pType, c);
    }

    public Piece Black() {
        return new Piece(color.BLACK, pType, c);
    }


    public enum color {
        WHITE, BLACK, NULL
    }

    public enum type {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, EMPTY
    }
}