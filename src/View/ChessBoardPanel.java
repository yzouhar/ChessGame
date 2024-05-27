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
import java.util.ArrayList;
import java.util.List;


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

	public ChessBoardPanel() throws IOException {
		this.setLayout(new GridLayout(8, 8));
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

		SpotPanel spotPanel = (SpotPanel) this.getComponentAt(e.getPoint());

		this.selectedPiece = spotPanel.getSpot().getChessPiece();
		spotPanel.setChessPiece(null);
		spotPanel.removeAll();
		spotPanel.invalidate();
		spotPanel.validate();
		spotPanel.repaint();

		if (selectedPiece != null) {
			try {

				chessImage = ImageIO.read(new File(selectedPiece.getImage()));
				int scaledWidth = (int) (chessImage.getWidth() * 0.6);
				int scaledHeight = (int) (chessImage.getHeight() * 0.6);
				Image newImage = chessImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
				graphics2D = (Graphics2D) getGraphics();
				//changes opacity
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
		boolean flag = false;

		List<Spot> possibleMoves = getCandidateMoves(selectedPiece, destinationSpotPanel.getSpot());

        for (Spot possibleMove : possibleMoves) {
            System.out.println("Row: " + possibleMove.getRow());
            System.out.println("Column: " + possibleMove.getColumn());

				if (destinationSpotPanel.getSpot().getRow() == possibleMove.getRow() && destinationSpotPanel.getSpot().getColumn() == possibleMove.getColumn()) {
                //isValidMove(selectedPiece.getPosition(), destinationSpotPanel.getSpot());
                flag = true;
            }
        }

		if (flag && isValidMove(selectedPiece.getPosition(), destinationSpotPanel.getSpot()) && isSpotAvailable(selectedPiece.getPosition().getChessPiece(), destinationSpotPanel.getSpot(), selectedPiece.getPosition()) && !selectedPiece.isBishop() && !selectedPiece.isRook()) {
			try {
				destinationSpotPanel.removeAll();
				selectedPiece.getPosition().setChessPiece(null);
				addChessPiece(selectedPiece, row, column);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		else if (flag && selectedPiece.isBishop() && isBishopMoveAvailable(selectedPiece ,selectedPiece.getPosition(), destinationSpotPanel.getSpot()) && isSpotAvailable(selectedPiece.getPosition().getChessPiece(), destinationSpotPanel.getSpot(), selectedPiece.getPosition())) {
			try {
				destinationSpotPanel.removeAll();
				selectedPiece.getPosition().setChessPiece(null);
				addChessPiece(selectedPiece, row, column);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} else if (flag && selectedPiece.isRook() && isRookMoveAvailable(selectedPiece, selectedPiece.getPosition(), destinationSpotPanel.getSpot()) && isSpotAvailable(selectedPiece.getPosition().getChessPiece(), destinationSpotPanel.getSpot(), selectedPiece.getPosition())) {
			try {
				destinationSpotPanel.removeAll();
				selectedPiece.getPosition().setChessPiece(null);
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
		System.out.println("_____________________________________________________________");
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
	public void mouseMoved(MouseEvent e) { }

	public boolean isValidMove(Spot fromSpot, Spot toSpot) {
		return true;
	}

	public List<Spot> getCandidateMoves(ChessPiece chessPiece, Spot destinationSpot) {
		List<Spot> possibleSpotList = new ArrayList<>();

		int chessPieceRow = chessPiece.getPosition().getRow();
		int chessPieceColumn = chessPiece.getPosition().getColumn();

		if (chessPiece.getCategory().equals(ChessPiece.Category.PAWN)) {
			 possiblePawnMoves(chessPiece, destinationSpot, possibleSpotList, chessPieceRow, chessPieceColumn);
		}

		if (chessPiece.getCategory().equals(ChessPiece.Category.KNIGHT)) {
			possibleKnightMoves(chessPiece, destinationSpot, possibleSpotList, chessPieceRow, chessPieceColumn);
		}

		if (chessPiece.getCategory().equals(ChessPiece.Category.BISHOP)) {
			return possibleBishopMoves(chessPieceRow, chessPieceColumn);
		}

		if (chessPiece.getCategory().equals(ChessPiece.Category.ROOK)) {
			return possibleRookMoves(chessPiece, destinationSpot, chessPieceRow, chessPieceColumn);
		}

		if (chessPiece.getCategory().equals(ChessPiece.Category.KING)) {
			possibleKingMoves(chessPiece, possibleSpotList,destinationSpot, chessPieceRow, chessPieceColumn);
		}

		return possibleSpotList;
	}


	private void possibleKnightMoves(ChessPiece chessPiece, Spot destinationSpot, List<Spot> possibleSpotList, int chessPieceRow, int chessPieceColumn) {
			//if (destinationSpot == null || destinationSpot.getChessPiece().getColor().equals(ChessPiece.Color.BLACK)) {
				possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn + 2));
				possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn - 2));

				possibleSpotList.add(new Spot(chessPieceRow - 2, chessPieceColumn + 1));
				possibleSpotList.add(new Spot(chessPieceRow - 2, chessPieceColumn - 1));

				possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn + 2));
				possibleSpotList.add(new Spot(chessPieceRow + 2, chessPieceColumn + 1));

				possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn - 2));
				possibleSpotList.add(new Spot(chessPieceRow + 2, chessPieceColumn - 1));


			//}
	}

	private static void possiblePawnMoves(ChessPiece chessPiece, Spot destinationSpot, List<Spot> possibleSpotList, int chessPieceRow, int chessPieceColumn) {
		if (chessPiece.getColor().equals(ChessPiece.Color.WHITE)) {
			if (chessPiece.getPosition().getRow() == 6) {
				possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
				possibleSpotList.add(new Spot(chessPieceRow - 2, chessPieceColumn));
			} else if (chessPiece.getPosition().getRow() != 6) {
				possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
			}
			//if (chessPiece.getPosition().getChessPiece().getColor() == ChessPiece.Color.BLACK )

		}
		if (chessPiece.getColor().equals(ChessPiece.Color.BLACK)) {
			if (chessPiece.getPosition().getRow() == 1) {
				possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
				possibleSpotList.add(new Spot(chessPieceRow + 2, chessPieceColumn));
			}  else if (chessPiece.getPosition().getRow() != 1) {
				possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
			}
		}

	}

	public static  List<Spot> possibleBishopMoves(int chessPieceRow, int chessPieceColumn) {

		List<Spot> possibleSpotList = new ArrayList<>();
		for (int i = 0; i <= 7; i++) {

			if (isWhithinRange(chessPieceRow - i, chessPieceColumn - i))  {
				possibleSpotList.add(new Spot(chessPieceRow - i, chessPieceColumn - i));
			}
			if (isWhithinRange(chessPieceRow + i, chessPieceColumn + i)) {
				possibleSpotList.add(new Spot(chessPieceRow + i, chessPieceColumn + i));
			}
			if (isWhithinRange(chessPieceRow - i, chessPieceColumn + i)) {
				possibleSpotList.add(new Spot(chessPieceRow - i, chessPieceColumn + i));
			}
			if (isWhithinRange(chessPieceRow + i, chessPieceColumn - i)) {
				possibleSpotList.add(new Spot(chessPieceRow + i, chessPieceColumn - i));
			}
		}

		return possibleSpotList;
	}

	static boolean isWhithinRange(int row, int column){
		return row <=7 && row >=0 && column <=7 && column >=0;
	}

	public static List<Spot> possibleRookMoves(ChessPiece chessPiece, Spot destinationSpot, int chessPieceRow, int chessPieceColumn) {
		List<Spot> possibleSpotList = new ArrayList<>();

		for (int i = 0; i <= 7 ; i++) {
			possibleSpotList.add(new Spot(chessPieceRow - i, chessPieceColumn));
			possibleSpotList.add(new Spot(chessPieceRow, chessPieceColumn - i));
			possibleSpotList.add(new Spot(chessPieceRow + i, chessPieceColumn));
			possibleSpotList.add(new Spot(chessPieceRow, chessPieceColumn + i));

		}
		return possibleSpotList;
	}

	private static void possibleKingMoves(ChessPiece chessPiece,  List<Spot> possibleSpotList,Spot destinationSpot, int chessPieceRow, int chessPieceColumn) {
		possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn));
		possibleSpotList.add(new Spot(chessPieceRow, chessPieceColumn + 1));
		possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn));
		possibleSpotList.add(new Spot(chessPieceRow, chessPieceColumn - 1));
		possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn - 1));
		possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn - 1));
		possibleSpotList.add(new Spot(chessPieceRow - 1, chessPieceColumn + 1));
		possibleSpotList.add(new Spot(chessPieceRow + 1, chessPieceColumn + 1));

	}
 	public boolean isSpotAvailable(ChessPiece chessPiece, Spot toSpot, Spot fromSpot) {
		return toSpot.getChessPiece() == null || !chessPiece.getColor().equals(toSpot.getChessPiece().getColor());
	}

	public boolean isBishopMoveAvailable(ChessPiece chessPiece,Spot fromSpot, Spot toSpot)  {

		int startXCoordinate = fromSpot.getRow();
		int startYCoordinate = fromSpot.getColumn();

		int endXCoordinate = toSpot.getRow();
		int endYCoordinate = toSpot.getColumn();

		while (startXCoordinate > endXCoordinate && startYCoordinate > endYCoordinate ) {
			SpotPanel currentSpot = spots[--startXCoordinate][--startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot) ) {
					return false;
			}
		}

		while (startXCoordinate < endXCoordinate && startYCoordinate < endYCoordinate) {
			SpotPanel currentSpot = spots[++startXCoordinate][++startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot) ) {
				return false;
			}
		}
		while (startXCoordinate > endXCoordinate && startYCoordinate < endYCoordinate) {
			SpotPanel currentSpot = spots[--startXCoordinate][++startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot) ) {
				return false;
			}
		}

		while (startXCoordinate < endXCoordinate && startYCoordinate > endYCoordinate) {
			SpotPanel currentSpot = spots[++startXCoordinate][--startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot)) {
				return false;
			}
		}

		if (toSpot.getChessPiece() != null && !chessPiece.getCategory().equals(ChessPiece.Category.KING)) {
			if (!toSpot.getChessPiece().getColor().equals(fromSpot.getChessPiece().getColor())) {
				return true;
			}
		}
		return true;
	}

	public boolean isRookMoveAvailable(ChessPiece chessPiece, Spot fromSpot, Spot toSpot) {
		int startXCoordinate = fromSpot.getRow();
		int startYCoordinate = fromSpot.getColumn();

		int endXCoordinate = toSpot.getRow();
		int endYCoordinate = toSpot.getColumn();

		while (startXCoordinate > endXCoordinate) {
			SpotPanel currentSpot = spots[--startXCoordinate][startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot)) {
				return false;
			}
		}

		while (startXCoordinate < endXCoordinate) {
			SpotPanel currentSpot = spots[++startXCoordinate][startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot)) {
				return false;
			}
		}


		while (startYCoordinate < endYCoordinate) {
			SpotPanel currentSpot = spots[startXCoordinate][++startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot)) {
				return false;
			}
		}

		while (startYCoordinate > endYCoordinate) {
			SpotPanel currentSpot = spots[startXCoordinate][--startYCoordinate];
			if (currentSpot.isOccupied() && !currentSpot.getSpot().equals(toSpot)) {
				return false;
			}
		}

		if (toSpot.getChessPiece() != null && !chessPiece.getCategory().equals(ChessPiece.Category.KING)) {
			if (!toSpot.getChessPiece().getColor().equals(fromSpot.getChessPiece().getColor())) {
				return true;
			}
		}

		return true;
	}
}
