import View.ApplicationFrame;
import javax.swing.SwingUtilities;

public class MainController {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { ApplicationFrame appFrame = new ApplicationFrame(); appFrame.placeChessPiece(); }
            catch (java.io.IOException e) { throw new RuntimeException(e); }
        });
    }
}
