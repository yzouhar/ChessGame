package Model;

import javax.swing.*;

public class SpotPanel extends JPanel {
	Spot spot;
	ChessPiece chessPiece;
	public SpotPanel(Spot spot){
		this.spot = spot;
		this.setSpot(spot);
	}
	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	public void setChessPiece(ChessPiece chessPiece) {
		this.chessPiece = chessPiece;
	}
}
