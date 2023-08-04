package View;

import Model.Board;
import Model.ChessPiece;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class ApplicationFrame extends JFrame  {
	ChessBoardPanel chessBoard = new ChessBoardPanel();

	public ApplicationFrame() throws IOException {
		this.add(chessBoard);
		this.setSize(1920, 1080);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void placeChessPiece(){
		try {
			chessBoard.placeChessPiece();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
