package Model;

public class Spot {

	int row;
	int column;

	ChessPiece chessPiece;

	public Spot(int x, int y){
		this.row = x;
		this.column = y;

	}

	public Spot() {

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public ChessPiece getChessPiece() {
		return chessPiece;
	}

	public void setChessPiece(ChessPiece chessPiece) {
		this.chessPiece = chessPiece;
	}

	public boolean isEmpty(){
		return (chessPiece == null);
	}

	public boolean equalsSpot(Object obj) {
		Spot spot = (Spot) obj;
		return (spot.getRow() == this.getRow() && spot.getColumn() == this.getColumn());
	}
}