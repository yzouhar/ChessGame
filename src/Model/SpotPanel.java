package Model;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SpotPanel extends JPanel implements MouseListener, MouseMotionListener {

	Spot spot;

	public SpotPanel(Spot spot){
		this.spot = spot;
		this.setSpot(spot);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/*System.out.println("row: " + spot.getRow());
		System.out.println("column: " + spot.getColumn());
		ChessPiece chessPiece = spot.getChessPiece();
		if (chessPiece != null) {
			System.out.println("Chess Piece: " + chessPiece.category);
		}*/
	}

	@Override
	public void mousePressed(MouseEvent e) {
		/*System.out.println("Mouse Pressed .. " );
		System.out.println("row: " + spot.getRow());
		System.out.println("column: " + spot.getColumn());

		ChessPiece chessPiece = spot.getChessPiece();

		if (chessPiece != null) {
			System.out.println("Chess Piece: " + chessPiece.getCategory());
		}*/
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		/*System.out.println("Mouse Released .. " );
		System.out.println("row: " + spot.getRow());
		System.out.println("column: " + spot.getColumn());
		ChessPiece chessPiece = spot.getChessPiece();

		if (chessPiece != null) {
			System.out.println("Chess Piece: " + chessPiece.getCategory());
		}*/

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		/*System.out.println("Mouse Released .. " );
		System.out.println("row: " + spot.getRow());
		System.out.println("column: " + spot.getColumn());
		ChessPiece chessPiece = spot.getChessPiece();

		if (chessPiece != null) {
			System.out.println("Chess Piece: " + chessPiece.getCategory());
		}*/

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}
}
