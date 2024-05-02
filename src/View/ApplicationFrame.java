package View;


import Model.Spot;

import javax.swing.*;
import java.io.IOException;

public class ApplicationFrame extends JFrame  {
	ChessBoardPanel chessBoard = new ChessBoardPanel(new Spot());

	public ApplicationFrame() throws IOException {
		this.add(chessBoard);
		this.setSize(1920, 1080);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void placeChessPiece(){
		try {
			chessBoard.placeChessPiece();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
    }


}