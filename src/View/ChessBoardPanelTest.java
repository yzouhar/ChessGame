package View;

import Model.ChessClock;
import Model.ChessGame;
import Model.ChessPiece;
import Model.SpotPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Headless smoke test for board geometry, move hints, check indication, and mouse input. */
public final class ChessBoardPanelTest {
    private static Object field(Object target,String name)throws Exception{Field f=target.getClass().getDeclaredField(name);f.setAccessible(true);return f.get(target);}
    private static void click(SpotPanel square){Component target=square.getComponentCount()==0?square:square.getComponent(0);long now=System.currentTimeMillis();target.dispatchEvent(new MouseEvent(target,MouseEvent.MOUSE_PRESSED,now,MouseEvent.BUTTON1_DOWN_MASK,4,4,1,false,MouseEvent.BUTTON1));target.dispatchEvent(new MouseEvent(target,MouseEvent.MOUSE_RELEASED,now+20,0,4,4,1,false,MouseEvent.BUTTON1));}
    private static ChessBoardPanel panel()throws Exception{return new ChessBoardPanel();}
    private static void layoutTree(Container container){container.doLayout();for(Component child:container.getComponents())if(child instanceof Container)layoutTree((Container)child);}
    private static void stopTimer(ChessBoardPanel panel)throws Exception{((Timer)field(panel,"timer")).stop();}
    public static void main(String[] args)throws Exception{
        final Throwable[] failure={null};SwingUtilities.invokeAndWait(()->{try{
            ChessBoardPanel panel=panel();stopTimer(panel);panel.setSize(900,1000);layoutTree(panel);
            SpotPanel[][] squares=(SpotPanel[][])field(panel,"squares");JPanel board=(JPanel)field(panel,"boardGrid");
            if(board.getWidth()<=0||board.getWidth()!=board.getHeight()||squares[0][0].getWidth()!=squares[0][0].getHeight())throw new AssertionError("board cells must remain square");
            ChessGame game=(ChessGame)field(panel,"game");ChessClock clock=(ChessClock)field(panel,"clock");
            click(squares[6][4]);
            if(squares[4][4].getBackground().equals(new Color(238,238,210))||squares[4][4].getBackground().equals(new Color(118,150,86)))throw new AssertionError("legal destination was not highlighted");
            click(squares[3][4]);if(game.getTurn()!=ChessPiece.Color.WHITE||clock.getActive()!=ChessPiece.Color.WHITE)throw new AssertionError("invalid move switched turns");
            click(squares[6][4]);click(squares[4][4]);if(game.getTurn()!=ChessPiece.Color.BLACK||clock.getActive()!=ChessPiece.Color.BLACK)throw new AssertionError("valid single-click move did not switch both turns");
            if(board.getComponent(0)!=squares[7][7])throw new AssertionError("board did not flip to Black's perspective");
            JTextArea log=(JTextArea)field(panel,"moveLog");if(!log.getText().contains("1. e2-e4"))throw new AssertionError("move was not logged");
            click(squares[1][4]);click(squares[3][4]);if(game.getTurn()!=ChessPiece.Color.WHITE||board.getComponent(0)!=squares[0][0])throw new AssertionError("board did not flip back: turn="+game.getTurn()+", top="+board.getComponent(0)+", expected="+squares[0][0]);

            Object[] pieces={7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,4,ChessPiece.Category.KING,ChessPiece.Color.BLACK,3,4,ChessPiece.Category.QUEEN,ChessPiece.Color.WHITE};
            Method setup=ChessGame.class.getDeclaredMethod("setupForTest",ChessPiece.Color.class,Object[].class);setup.setAccessible(true);setup.invoke(game,ChessPiece.Color.BLACK,pieces);
            Method refresh=ChessBoardPanel.class.getDeclaredMethod("refresh");refresh.setAccessible(true);refresh.invoke(panel);
            JLabel message=(JLabel)field(panel,"message");if(!message.getText().contains("CHECK"))throw new AssertionError("check status missing from UI");
            if(!squares[0][4].getBackground().equals(new Color(220,92,92)))throw new AssertionError("checked king square not highlighted");stopTimer(panel);

            ChessBoardPanel dragPanel=panel();stopTimer(dragPanel);dragPanel.setSize(900,1000);layoutTree(dragPanel);SpotPanel[][] dragSquares=(SpotPanel[][])field(dragPanel,"squares");JPanel dragBoard=(JPanel)field(dragPanel,"boardGrid");
            Component source=dragSquares[6][4].getComponent(0);Point destination=SwingUtilities.convertPoint(dragSquares[4][4],new Point(dragSquares[4][4].getWidth()/2,dragSquares[4][4].getHeight()/2),source);long now=System.currentTimeMillis();
            source.dispatchEvent(new MouseEvent(source,MouseEvent.MOUSE_PRESSED,now,MouseEvent.BUTTON1_DOWN_MASK,4,4,1,false,MouseEvent.BUTTON1));
            source.dispatchEvent(new MouseEvent(source,MouseEvent.MOUSE_RELEASED,now+100,0,destination.x,destination.y,1,false,MouseEvent.BUTTON1));
            ChessGame dragGame=(ChessGame)field(dragPanel,"game");ChessClock dragClock=(ChessClock)field(dragPanel,"clock");if(dragGame.getTurn()!=ChessPiece.Color.BLACK||dragClock.getActive()!=ChessPiece.Color.BLACK)throw new AssertionError("press-and-release drag did not move and switch clock");stopTimer(dragPanel);
        }catch(Throwable t){failure[0]=t;}});if(failure[0]!=null)throw new AssertionError(failure[0]);System.out.println("ChessBoardPanelTest: all checks passed");
    }
}
