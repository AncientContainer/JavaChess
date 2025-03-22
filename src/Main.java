import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            board.printBoard();
            board.playMove(scanner.nextLine(), scanner.nextLine());
//            for (int i = 0; i < 8; i++) {
//                for (Integer[] move : board.moveGen(Board.stringToSquare(scanner.nextLine()), Piece.color.WHITE)) {
//                    System.out.println("Possible move: " + Board.squareToString(Board.swapArray(move)));
//                }
//            }
        }
    }
}