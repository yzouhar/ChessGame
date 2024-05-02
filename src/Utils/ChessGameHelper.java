package Utils;

import Model.ChessPiece;
import Model.Spot;

import java.util.ArrayList;
import java.util.List;

public class ChessGameHelper {


    public static  List<Spot> possibleBishopMoves(int chessPieceRow, int chessPieceColumn) {

        List<Spot> possibleSpotList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (chessPieceRow + i <= 7 && chessPieceRow + i >= 0 && chessPieceColumn + i >= 0 && chessPieceColumn + i <= 7)
                possibleSpotList.add(new Spot(chessPieceRow + i, chessPieceColumn + i));
        }

        for (int i = 0; i < 8; i++) {
            if (chessPieceRow + i <= 7 && chessPieceRow + i >= 0 && chessPieceColumn + i >= 0 && chessPieceColumn + i <= 7) {
                possibleSpotList.add(new Spot(chessPieceRow + i, chessPieceColumn - i));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (chessPieceRow + i <= 7 && chessPieceRow + i >= 0 && chessPieceColumn + i >= 0 && chessPieceColumn + i <= 7) {
                possibleSpotList.add(new Spot(chessPieceRow - i, chessPieceColumn + i));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (chessPieceRow + i <= 7 && chessPieceRow + i >= 0 && chessPieceColumn + i >= 0 && chessPieceColumn + i <= 7) {
                possibleSpotList.add(new Spot(chessPieceRow - i, chessPieceColumn - i));
            }
        }

        return possibleSpotList;
    }

    public static List<Spot> possiblePawnMoves(ChessPiece chessPiece, Spot destinationSpot, int chessPieceRow, int chessPieceColumn) {

        List<Spot> possibleSpotList = new ArrayList<>();

            if (chessPiece.getPosition() != null && chessPiece.getPosition().getRow() == 6) {
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow - 2, chessPieceColumn));
            }

            if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK) && destinationSpot.getColumn() > chessPieceColumn) {
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn + 1));
            } else if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK) && destinationSpot.getColumn() < chessPieceColumn){
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn - 1));
            }

            if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && chessPiece.getPosition().getColumn() == 0 && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK)) {
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn + 1));
            }

            if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && chessPiece.getPosition().getColumn() == 7 && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK)) {
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn - 1));
            }
            if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && chessPiece.getPosition().getRow() != 6  && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK)) {
                possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
            }

            if (chessPiece.getPosition().getRow() == 1) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow + 2, chessPieceColumn));
            }

            if (chessPiece.getPosition() != null && destinationSpot.getChessPiece() != null && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.WHITE) && destinationSpot.getColumn() > chessPieceColumn) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn + 1));
            } else if (destinationSpot.getChessPiece() != null && destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.WHITE) && destinationSpot.getColumn() < chessPieceColumn) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn - 1));
            }

            if (chessPiece.getPosition() != null && chessPiece.getPosition().getColumn() == 0) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn + 1));
            }

            if (chessPiece.getPosition() != null && chessPiece.getPosition().getColumn() == 7) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn - 1));
            }

            if (chessPiece.getPosition() != null && chessPieceRow != 6) {
                possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
            }
        return possibleSpotList;
    }
}
