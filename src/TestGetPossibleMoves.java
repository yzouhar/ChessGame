import Model.ChessPiece;
import Model.Spot;
import Utils.ChessGameHelper;

import java.io.IOException;
import java.util.List;

public  class TestGetPossibleMoves {

    void  testBishopPossibleMoves() {

        List<Spot> possibleMoves = ChessGameHelper.possibleBishopMoves(1, 1);
        for (Spot spot: possibleMoves) {
            System.out.println(String.format("piece: %s row : %s, column: %s", "Bishop", spot.getRow(), spot.getColumn()));
        }
    }


    void testPawnPossibleMoves() {
        ChessPiece whiteChessPiece = new ChessPiece(ChessPiece.Category.PAWN, ChessPiece.Color.WHITE, "whitePawn.png");
        Spot pawnSpot = new Spot(7,7);
            List<Spot> possibleMoves = ChessGameHelper.possiblePawnMoves(whiteChessPiece, pawnSpot,1,1);
            for (Spot spot : possibleMoves) {

                if (spot.getRow() > 0 || spot.getColumn() > 0) {
                    int row = spot.getChessPiece().getPosition().getRow();
                    int column  = spot.getChessPiece().getPosition().getColumn();
                    System.out.println(String.format("piece: %s row : %s, column: %s", "Pawn", row, column));
                }
            }

    }

    public static void main(String[] args) throws IOException {
        TestGetPossibleMoves test = new TestGetPossibleMoves();
        test.testBishopPossibleMoves();
        test.testPawnPossibleMoves();
    }


}
