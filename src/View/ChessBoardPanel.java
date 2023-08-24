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

public class ChessBoardPanel extends JPanel implements MouseListener, MouseMotionListener {
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
	static String blackQueen = "resources/images/blackQueen.png";

	static SpotPanel[][] spots = new SpotPanel[8][8];

	ChessPiece selectedPiece;
	BufferedImage chessImage;
	Graphics2D graphics2D;

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

		for (int i = 0; i < 8; i++) {
			addChessPiece(new ChessPiece(ChessPiece.Category.PAWN, ChessPiece.Color.WHITE, whitePawn), 6, i);
		}

		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.BLACK, blackRook), 0, 0);
		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.BLACK, blackRook), 0, 7);

		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.BLACK, blackKnight), 0, 1);
		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.BLACK, blackKnight), 0, 6);

		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.BLACK, blackBishop), 0, 2);
		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.BLACK, blackBishop), 0, 5);

		addChessPiece(new ChessPiece(ChessPiece.Category.QUEEN, ChessPiece.Color.BLACK, blackQueen), 0, 3);
		addChessPiece(new ChessPiece(ChessPiece.Category.QUEEN, ChessPiece.Color.WHITE, whiteQueen), 7, 3);

		addChessPiece(new ChessPiece(ChessPiece.Category.KING, ChessPiece.Color.BLACK, blackKing), 0, 4);
		addChessPiece(new ChessPiece(ChessPiece.Category.KING, ChessPiece.Color.WHITE, whiteKing), 7, 4);

		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.WHITE, whiteRook), 7, 0);
		addChessPiece(new ChessPiece(ChessPiece.Category.ROOK, ChessPiece.Color.WHITE, whiteRook), 7, 7);

		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.WHITE, whiteKnight), 7, 1);
		addChessPiece(new ChessPiece(ChessPiece.Category.KNIGHT, ChessPiece.Color.WHITE, whiteKnight), 7, 6);

		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.WHITE, whiteBishop), 7, 2);
		addChessPiece(new ChessPiece(ChessPiece.Category.BISHOP, ChessPiece.Color.WHITE, whiteBishop), 7, 5);
	}

	public static void createBoardTiles(JPanel mainPanel) throws IOException {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				SpotPanel panel = new SpotPanel(new Spot(row, col));
				spots[row][col] = panel;
				panel.setPreferredSize(new Dimension(300, 300));
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

	}

	@Override
	public void mousePressed(MouseEvent e) {
		SpotPanel object = (SpotPanel) this.getComponentAt(e.getPoint());
		this.selectedPiece = object.getSpot().getChessPiece();

		object.removeAll();
		object.invalidate();
		object.validate();
		object.repaint();

		if (selectedPiece != null) {
			try {
				chessImage = ImageIO.read(new File(selectedPiece.getImage()));
				int scaledWidth = (int) (chessImage.getWidth() * 0.6);
				int scaledHeight = (int) (chessImage.getHeight() * 0.6);
				Image newImage = chessImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
				graphics2D = (Graphics2D) getGraphics();
				//changes ]\opacity
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				int x = e.getX() - scaledWidth / 2;
				int y = e.getY() - scaledHeight / 2;
				graphics2D.drawImage(newImage, x, y, this);

				graphics2D.dispose();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		SpotPanel destinationSpotPanel = (SpotPanel) this.getComponentAt(e.getPoint());

		int row = destinationSpotPanel.getSpot().getRow();
		int column = destinationSpotPanel.getSpot().getColumn();

		if (isValidMove(selectedPiece.getPosition(), destinationSpotPanel.getSpot()) && isSpotAvailable(selectedPiece.getPosition().getChessPiece(), destinationSpotPanel.getSpot())) {
			try {
				destinationSpotPanel.removeAll();
				addChessPiece(selectedPiece, row, column);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			try {
				addChessPiece(selectedPiece, selectedPiece.getPosition().getRow(), selectedPiece.getPosition().getColumn());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		/* if (selectedPiece != null) {
			try {
				chessImage = ImageIO.read(new File(selectedPiece.getImage()));
				int scaledWidth = (int) (chessImage.getWidth() * 1.0);
				int scaledHeight = (int) (chessImage.getHeight() * 1.0);
				Image newImage = chessImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
				graphics2D = (Graphics2D) getGraphics();
				//changes ]\opacity
				graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				int x = e.getX() - scaledWidth / 2;
				int y = e.getY() - scaledHeight / 2;
				graphics2D.drawImage(newImage, x, y, this);

				graphics2D.dispose();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} */
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public boolean isValidMove(Spot fromSpot, Spot toSpot) {
       return true;
	}

	public boolean isSpotAvailable(ChessPiece chessPiece, Spot toSpot) {
        return toSpot.getChessPiece() == null || chessPiece.getColor() != toSpot.getChessPiece().getColor();
    }

}
