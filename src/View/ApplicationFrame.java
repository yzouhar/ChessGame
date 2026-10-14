package View;

import javax.swing.*;
import java.io.IOException;

public class ApplicationFrame extends JFrame {
    private final ChessBoardPanel chessBoard = new ChessBoardPanel();
    public ApplicationFrame() throws IOException {
        super("Chess");setDefaultCloseOperation(EXIT_ON_CLOSE);setContentPane(chessBoard);setSize(900,900);setMinimumSize(new java.awt.Dimension(620,680));setLocationRelativeTo(null);setVisible(true);
    }
    public void placeChessPiece(){chessBoard.placeChessPiece();}
}
