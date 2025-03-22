import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final static char[] files = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private final static char[] ranks = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
    private final Piece empty;
    private final Piece pawn;
    private final Piece knight;
    private final Piece bishop;
    private final Piece rook;
    private final Piece queen;
    private final Piece king;
    private final Piece[] pieces;
    private final int[] knightMovesX = new int[]{-1, 1, 2, 2, -1, 1, -2, -2}; // x offsets for knight moves
    private final int[] knightMovesY = new int[]{2, 2, -1, 1, -2, -2, -1, 1}; // y offsets for knight moves
    Piece[][] board = new Piece[8][8];
    private Piece.color turn;

    public Board() {
        pawn = new Piece(Piece.color.NULL, Piece.type.PAWN, "♟");
        knight = new Piece(Piece.color.NULL, Piece.type.KNIGHT, "♞");
        bishop = new Piece(Piece.color.NULL, Piece.type.BISHOP, "♝");
        rook = new Piece(Piece.color.NULL, Piece.type.ROOK, "♜");
        queen = new Piece(Piece.color.NULL, Piece.type.QUEEN, "♛");
        king = new Piece(Piece.color.NULL, Piece.type.KING, "♚");
        empty = new Piece(" ");
        pieces = new Piece[]{pawn, knight, bishop, rook, queen, king, empty};
        setUp();
        turn = Piece.color.WHITE;
    }

    public Board(Piece[][] board, Piece[] pieces) {
        this.board = board;
        this.pieces = pieces;
        pawn = pieces[0];
        knight = pieces[1];
        bishop = pieces[2];
        rook = pieces[3];
        queen = pieces[4];
        king = pieces[5];
        empty = pieces[6];
        turn = Piece.color.WHITE;
    }

    public static int[] swapArray(Integer[] integerArray) {
        if (integerArray == null) {
            return new int[0];
        }

        int[] intArray = new int[integerArray.length];
        for (int i = 0; i < integerArray.length; i++) {
            //Handle null values in the Integer array to avoid NullPointerException
            if (integerArray[i] != null) {
                intArray[i] = integerArray[i];
            } else {
                intArray[i] = 0; //assign 0 as default value for nulls
            }
        }
        return intArray;
    }

    public static String squareToString(int[] square) {
        return "" + files[square[1]] + ranks[square[0]];
    }

    public static int[] stringToSquare(String string) {
        if (string.length() != 2) {
            System.out.println("Invalid square string");
            return new int[]{-1, -1};
        } else {
            return new int[]{8 - Integer.parseInt("" + (string.charAt(1))), Integer.parseInt("" + (indexOf(files, string.charAt(0))))};
        }
    }

    public static int indexOf(char[] array, char target) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (target == array[i]) {
                return i;
            }
        }
        return -1;
    }

    public static Integer[] swapArray(int[] array) {
        return Arrays.stream(array).boxed().toArray(Integer[]::new);
    }

    public static boolean contains(ArrayList<Integer[]> arrayList, Integer[] square) {
        boolean t = true;
        for (Integer[] array : arrayList) {
            for (int i = 0; i < array.length; i++) {
                if (!array[i].equals(square[i])) {
                    t = false;
                    break;
                }
            }
            if (!t) t = true;
            else return true;
        }
        return false;
    }

    private void setUp() {
        board[0][0] = rook.Black();
        board[0][1] = knight.Black();
        board[0][2] = bishop.Black();
        board[0][3] = queen.Black();
        board[0][4] = king.Black();
        board[0][5] = bishop.Black();
        board[0][6] = knight.Black();
        board[0][7] = rook.Black();
        board[7][7] = rook.White();
        board[7][6] = knight.White();
        board[7][5] = bishop.White();
        board[7][4] = king.White();
        board[7][3] = queen.White();
        board[7][2] = bishop.White();
        board[7][1] = knight.White();
        board[7][0] = rook.White();
        for (int i = 7; i >= 0; i--) {
            board[6][i] = pawn.White();
            board[1][i] = pawn.Black();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = board[i][j] == null ? empty : board[i][j];
            }
        }
    }

    public Piece getSquare(int row, int col) {
        return board[row][col];
    }

    public Piece getSquare(int[] rowAndCol) {
        return getSquare(rowAndCol[0], rowAndCol[1]);
    }

    public void setSquare(int[] square, Piece piece) {
        board[square[0]][square[1]] = piece;
    }

    public void playMove(int[] startSquare, int[] endSquare) {
        Piece start = getSquare(startSquare);
        Piece end = getSquare(endSquare);
        if (start.isEmpty()) {
            System.out.println("Invalid start square for move");
        } else if (start.getType() != Piece.type.EMPTY) {
            if (contains(moveGen(startSquare, turn), (swapArray(endSquare)))) {
                setSquare(endSquare, start);
                setSquare(startSquare, empty);
                turn = turn == Piece.color.WHITE ? Piece.color.BLACK : Piece.color.WHITE;
            } else {
                System.out.println("Invalid end square for move");
            }
        }
    }

    public void playMove(String startSquare, String endSquare) {
        playMove(stringToSquare(startSquare), stringToSquare(endSquare));
    }

    public ArrayList<Integer[]> moveGen(int[] square, Piece.color turn) {
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
        Piece piece = board[square[0]][square[1]];
        if (piece.isEmpty()) {
            System.out.println("Invalid start square");
        } else if (piece.equals(pawn.White()) && turn == Piece.color.WHITE) {
            if (checkSquare(push(square, 0, 1)) && getSquare(push(square, 0, 1)).isEmpty()) { // up 1 square
                moves.add(swapArray(push(square, 0, 1)));
                if (checkSquare(push(square, 0, 2)) && getSquare(push(square, 0, 2)).isEmpty()) { // up 2 squares
                    moves.add(swapArray(push(square, 0, 2)));
                }
            }
            if (checkSquare(push(square, -1, 1)) && getSquare(push(square, -1, 1)).diffColor(piece)) {
                moves.add(swapArray(push(square, -1, 1))); // left capture
            }
            if (checkSquare(push(square, 1, 1)) && getSquare(push(square, 1, 1)).diffColor(piece)) {
                moves.add(swapArray(push(square, 1, 1))); // right capture
            }
        } else if (piece.equals(pawn.Black()) && turn == Piece.color.BLACK) {
            if (checkSquare(push(square, 0, -1)) && getSquare((push(square, 0, -1))).isEmpty()) { // up 1 square
                moves.add(swapArray(push(square, 0, -1)));
                if (checkSquare(push(square, 0, -2)) && getSquare(push(square, 0, -2)).isEmpty()) { // up 2 squares
                    moves.add(swapArray(push(square, 0, -2)));
                }
            }
            if (checkSquare(push(square, -1, -1)) && getSquare(push(square, -1, -1)).diffColor(piece)) {
                moves.add(swapArray(push(square, -1, -1))); // left capture
            }
            if (checkSquare(push(square, 1, -1)) && getSquare(push(square, 1, -1)).diffColor(piece)) {
                moves.add(swapArray(push(square, 1, -1))); // right capture
            }
        } else if (piece.sameType(knight) && turn == piece.getColor()) {
            for (int i = 0; i < 8; i++) {
                int xOffset = knightMovesX[i];
                int yOffset = knightMovesY[i];
                if (checkSquare(push(square, xOffset, yOffset))) {
                    if (getSquare(push(square, xOffset, yOffset)).getColor() != piece.getColor()) {
                        moves.add(swapArray(push(square, xOffset, yOffset)));
                    }
                }
            }
        } else if (piece.sameType(bishop) && turn == piece.getColor()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 7; j++) {
                    int xOffset = i % 2 == 0 ? (j + 1) : -(j + 1);
                    int yOffset = i / 2 == 0 ? (j + 1) : -(j + 1);
                    if (checkSquare(push(square, xOffset, yOffset))) {
                        if (getSquare(push(square, xOffset, yOffset)).getColor() != piece.getColor()) {
                            moves.add(swapArray(push(square, xOffset, yOffset)));
                            if (getSquare(push(square, xOffset, yOffset)).diffColor(piece)) {
                                break; // stop if we hit a piece of a different color
                            }
                        } else {
                            break; // stop if we hit a piece of the same color
                        }
                    }
                }
            }
        } else if (piece.sameType(rook) && turn == piece.getColor()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 7; j++) {
                    int xOffset = (i % 2 == 0 ? 0 : (i / 2 == 0 ? (j + 1) : -(j + 1)));
                    int yOffset = (i % 2 == 1 ? 0 : (i / 2 == 0 ? (j + 1) : -(j + 1)));
                    if (checkSquare(push(square, xOffset, yOffset))) {
                        if (getSquare(push(square, xOffset, yOffset)).getColor() != piece.getColor()) {
                            moves.add(swapArray(push(square, xOffset, yOffset)));
                            if (getSquare(push(square, xOffset, yOffset)).diffColor(piece)) {
                                break; // stop if we hit a piece of a different color
                            }
                        } else {
                            break; // stop if we hit a piece of the same color
                        }
                    }
                }
            }
        } else if (piece.sameType(queen) && turn == piece.getColor()) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 7; j++) {
                    int xOffset = (i % 2 == 0 ? 0 : (i / 2 == 0 ? (j + 1) : -(j + 1)));
                    int yOffset = (i % 2 == 1 ? 0 : (i / 2 == 0 ? (j + 1) : -(j + 1)));
                    if (checkSquare(push(square, xOffset, yOffset))) {
                        if (getSquare(push(square, xOffset, yOffset)).getColor() != piece.getColor()) {
                            moves.add(swapArray(push(square, xOffset, yOffset)));
                            if (getSquare(push(square, xOffset, yOffset)).diffColor(piece)) {
                                break; // stop if we hit a piece of a different color
                            }
                        } else {
                            break; // stop if we hit a piece of the same color
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 7; j++) {
                    int xOffset = i % 2 == 0 ? (j + 1) : -(j + 1);
                    int yOffset = i / 2 == 0 ? (j + 1) : -(j + 1);
                    if (checkSquare(push(square, xOffset, yOffset))) {
                        if (getSquare(push(square, xOffset, yOffset)).getColor() != piece.getColor()) {
                            moves.add(swapArray(push(square, xOffset, yOffset)));
                            if (getSquare(push(square, xOffset, yOffset)).diffColor(piece)) {
                                break; // stop if we hit a piece of a different color
                            }
                        } else {
                            break; // stop if we hit a piece of the same color
                        }
                    }
                }
            }
        } else if (piece.sameType(king) && turn == piece.getColor()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue; // skip the current square
                    if (checkSquare(push(square, i, j))) {
                        if (getSquare(push(square, i, j)).getColor() != piece.getColor()) {
                            moves.add(swapArray(push(square, i, j)));
                        }
                    }
                }
            }
        }
        return moves;
    }

    private boolean checkSquare(int[] square) {
        return 0 <= square[0] && square[0] <= 7 && 0 <= square[1] && square[1] <= 7;
    }

    private int[] push(int[] square, int xOffset, int yOffset) {
        return new int[]{square[0] - yOffset, square[1] + xOffset};
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardString.append((row - col) % 2 == 0 ? Text.WHITE_BACKGROUND : Text.BLACK_BACKGROUND);
                boardString.append(" ").append(board[row][col].toString()).append(" ");
            }
            boardString.append(Text.RESET).append("\n");
        }

        boardString.append("\n ").append(turn == Piece.color.WHITE ? "White" : "Black").append(" to move\n");
        return boardString.toString();
    }

    public void printBoard() {
        System.out.println(toString());
    }

    public Piece.color getTurn() {
        return turn;
    }
}

