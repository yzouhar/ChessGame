package View;

import Model.ChessClock;
import Model.ChessGame;
import Model.ChessPiece;
import Model.SpotPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

/** Headless smoke test for square input, turn synchronization, and invalid move handling. */
public final class ChessBoardPanelTest {
    private static Object field(Object target,String name)throws Exception{Field f=target.getClass().getDeclaredField(name);f.setAccessible(true);return f.get(target);}
    private static void click(SpotPanel square){Component target=square.getComponentCount()==0?square:square.getComponent(0);long now=System.currentTimeMillis();target.dispatchEvent(new MouseEvent(target,MouseEvent.MOUSE_CLICKED,now,0,4,4,1,false));}
    public static void main(String[] args)throws Exception{
        final Throwable[] failure={null};SwingUtilities.invokeAndWait(()->{try{
            ChessBoardPanel panel=new ChessBoardPanel();((Timer)field(panel,"timer")).stop();SpotPanel[][] squares=(SpotPanel[][])field(panel,"squares");ChessGame game=(ChessGame)field(panel,"game");ChessClock clock=(ChessClock)field(panel,"clock");
            click(squares[6][4]);click(squares[3][4]);if(game.getTurn()!=ChessPiece.Color.WHITE||clock.getActive()!=ChessPiece.Color.WHITE)throw new AssertionError("invalid move switched turns");
            click(squares[6][4]);click(squares[4][4]);if(game.getTurn()!=ChessPiece.Color.BLACK||clock.getActive()!=ChessPiece.Color.BLACK)throw new AssertionError("valid move did not switch both turns");
            ((Timer)field(panel,"timer")).stop();
        }catch(Throwable t){failure[0]=t;}});if(failure[0]!=null)throw new AssertionError(failure[0]);System.out.println("ChessBoardPanelTest: all checks passed");
    }
}
