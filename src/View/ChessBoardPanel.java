package View;

import Model.ChessPiece;
import Model.Spot;
import Model.SpotPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChessBoardPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	Spot spot;

	static String whitePawn = "resources/images/whitePawn.png";
	static String blackPawn = "resources/images/blackPawn.png";

	static String whiteRook = "resources/images/whiteRook.png";
	static String blackRook = "resources/images/blackRook.png";

	static String whiteKnight = "resources/images/whiteKnight.png";
	static String blackKnight = "resources/images/blackKnight.png";

	static String whiteBishop = "resources/images/whiteBishop.png";
	static String blackBishop = "resources/images/blackBishop.png";

	static String whiteKing = "resources/images/whiteKing.png";
	static String blackKing = "resources/images/blackKing.png";

	static String whiteQueen = "resources/images/whiteQueen.png";
	static String blackQueen ="resources/images/blackQueen.png";

	static SpotPanel[][] spots = new SpotPanel[8][8];


	public ChessBoardPanel(Spot spot) throws IOException {
		this.setLayout(new GridLayout(8, 8));
		this.spot = spot;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		createBoardTiles(this);

	}

	public void placeChessPiece() throws IOException, InterruptedException {
		for (int i = 0; i < 8; i++) {
			addChessPiece(new ChessPiece(ChessPiece.Category.PAWN, ChessPiece.Color.BLACK, blackPawn), 1, i);
		}

		for (int i = 0; i <8; i++) {
			addChessPiece(new ChessPiece(ChessPiece.Category.PAWN, ChessPiece.Color.WHITE, whitePawn), 6, i);
		}

		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.BLACK, blackRook),0,0);
		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.BLACK, blackRook),0,7);

		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.BLACK, blackKnight), 0,1);
		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.BLACK, blackKnight), 0,6);

		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.BLACK, blackBishop), 0,2);
		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.BLACK, blackBishop), 0,5);

		addChessPiece(new ChessPiece(ChessPiece.Category.QUEEN, ChessPiece.Color.BLACK, blackQueen),0,3);
		addChessPiece(new ChessPiece(ChessPiece.Category.QUEEN, ChessPiece.Color.WHITE, whiteQueen),7,3);

		addChessPiece(new ChessPiece(ChessPiece.Category.KING, ChessPiece.Color.BLACK, blackKing),0,4);
		addChessPiece(new ChessPiece(ChessPiece.Category.KING, ChessPiece.Color.WHITE, whiteKing),7,4);

		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.WHITE, whiteRook),7,0);
		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.WHITE, whiteRook),7,7);

		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.WHITE, whiteKnight),7,1);
		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.WHITE, whiteKnight),7,6);

		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.WHITE, whiteBishop),7,2);
		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.WHITE, whiteBishop),7,5);
	}
	public static void createBoardTiles(JPanel mainPanel) throws IOException {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				SpotPanel panel = new SpotPanel(new Spot(row, col));
				spots[row][col] = panel;
				panel.setPreferredSize(new Dimension(300,300));
				if ((row + col) % 2 == 0) {
					panel.setBackground(Color.WHITE);
				} else {
					panel.setBackground(Color.decode("#769656"));
				}
				mainPanel.add(panel);
			}
		}
	}
	public static void addChessPiece(ChessPiece chessPiece, int row, int column) throws IOException {
		SpotPanel spotPanel = spots[row][column];
		spotPanel.getSpot().setChessPiece(chessPiece);
		chessPiece.setPosition(spotPanel.getSpot());
		BufferedImage myPicture = ImageIO.read(new File(chessPiece.getImage()));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		spotPanel.add(picLabel);

		spotPanel.invalidate();
		spotPanel.validate();
		spotPanel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	   SpotPanel object = (SpotPanel)this.getComponentAt(e.getPoint());
	   System.out.printf("row %s ", object.getSpot().getRow());
	   System.out.printf("column %s ", object.getSpot().getColumn());
		if (object.getSpot().getChessPiece() != null) {
			System.out.printf("ChessPiece : %s", object.getSpot().getChessPiece().getCategory());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		SpotPanel object = (SpotPanel)this.getComponentAt(e.getPoint());
		System.out.println("Mouse Pressed .....");
		System.out.printf("row %s ", object.getSpot().getRow());
		System.out.printf("column %s ", object.getSpot().getColumn());
		if (object.getSpot().getChessPiece() != null) {
			System.out.printf("ChessPiece : %s", object.getSpot().getChessPiece().getCategory());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		SpotPanel object = (SpotPanel)this.getComponentAt(e.getPoint());
		System.out.println("Mouse Released .....");
		System.out.printf("row %s ", object.getSpot().getRow());
		System.out.printf("column %s ", object.getSpot().getColumn());
		if (object.getSpot().getChessPiece() != null) {
			System.out.printf("ChessPiece : %s", object.getSpot().getChessPiece().getCategory());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		SpotPanel object = (SpotPanel)this.getComponentAt(e.getPoint());
		System.out.println("Mouse Entered .....");
		System.out.printf("row %s ", object.getSpot().getRow());
		System.out.printf("column %s ", object.getSpot().getColumn());
		if (object.getSpot().getChessPiece() != null) {
			System.out.printf("ChessPiece : %s", object.getSpot().getChessPiece().getCategory());
		}	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		SpotPanel object = (SpotPanel)this.getComponentAt(e.getPoint());
		System.out.println("Mouse Dragged .....");
		System.out.printf("row %s ", object.getSpot().getRow());
		System.out.printf("column %s ", object.getSpot().getColumn());
		if (object.getSpot().getChessPiece() != null) {
			System.out.printf("ChessPiece : %s", object.getSpot().getChessPiece().getCategory());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
