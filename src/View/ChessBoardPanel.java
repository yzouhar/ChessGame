package View;

import Model.ChessGame;
import Model.ChessClock;
import Model.ChessPiece;
import Model.Spot;
import Model.SpotPanel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.IOException;

/** Swing presentation for the chess rules engine. */
public class ChessBoardPanel extends JPanel {
    private final ChessGame game = new ChessGame();
    private final SpotPanel[][] squares = new SpotPanel[8][8];
    private final MouseListener[][] squareListeners = new MouseListener[8][8];
    private final ChessPiece[][] renderedPieces = new ChessPiece[8][8];
    private JPanel boardGrid;
    private int pressedRow=-1, pressedCol=-1;
    private boolean dragCandidate;
    private List<Spot> selectedMoves = Collections.emptyList();
    private final JLabel whiteClock = new JLabel(), blackClock = new JLabel(), message = new JLabel("White to move");
    private final JButton claimDraw = new JButton("Claim draw");
    private final Timer timer;
    private ChessClock clock;
    private long lastTick;
    private boolean timedOut;
    private ChessPiece.Color timeoutWinner;
    private int selectedRow=-1, selectedCol=-1;

    public ChessBoardPanel() throws IOException {
        super(new BorderLayout(8,8));
        int minutes=10, inc=0;
        if(!GraphicsEnvironment.isHeadless()){
            try {String value=JOptionPane.showInputDialog(null,"Starting time in minutes (increment: 0)","Chess clock",JOptionPane.QUESTION_MESSAGE);if(value!=null&&!value.trim().isEmpty())minutes=Math.max(1,Integer.parseInt(value.trim()));}catch(NumberFormatException ignored){}
            try {String value=JOptionPane.showInputDialog(null,"Increment per move in seconds", "0");if(value!=null&&!value.trim().isEmpty())inc=Math.max(0,Integer.parseInt(value.trim()));}catch(NumberFormatException ignored){}
        }
        clock=new ChessClock(minutes*60_000L,inc*1000L);timer=new Timer(100,e->tick());
        JPanel clocks=new JPanel(new GridLayout(1,2,12,0));whiteClock.setHorizontalAlignment(SwingConstants.CENTER);blackClock.setHorizontalAlignment(SwingConstants.CENTER);whiteClock.setFont(new Font(Font.SANS_SERIF,Font.BOLD,22));blackClock.setFont(new Font(Font.SANS_SERIF,Font.BOLD,22));clocks.add(whiteClock);clocks.add(blackClock);add(clocks,BorderLayout.NORTH);
        boardGrid=new JPanel(new GridLayout(8,8));boardGrid.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
        JPanel squareHolder=new JPanel(null){
            @Override public void doLayout(){int side=Math.min(getWidth(),getHeight());int x=(getWidth()-side)/2;int y=(getHeight()-side)/2;boardGrid.setBounds(x,y,side,side);boardGrid.doLayout();}
            @Override public Dimension getPreferredSize(){return new Dimension(688,688);}
        };
        for(int r=0;r<8;r++)for(int c=0;c<8;c++){
            final int row=r,col=c;SpotPanel sq=new SpotPanel(new Spot(r,c));sq.setLayout(new GridBagLayout());sq.setOpaque(true);sq.setBackground(baseColor(r,c));sq.setPreferredSize(new Dimension(86,86));sq.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            MouseAdapter listener=new MouseAdapter(){
                @Override public void mousePressed(MouseEvent e){pressedRow=row;pressedCol=col;ChessPiece p=game.getPiece(row,col);dragCandidate=p!=null&&p.getColor()==game.getTurn();if(dragCandidate)selectPiece(row,col);}
                @Override public void mouseReleased(MouseEvent e){if(!dragCandidate)return;Point point=SwingUtilities.convertPoint((Component)e.getSource(),e.getPoint(),boardGrid);int side=Math.min(boardGrid.getWidth(),boardGrid.getHeight());if(side<=0)return;int targetRow=point.y*8/side,targetCol=point.x*8/side;if(targetRow>=0&&targetRow<8&&targetCol>=0&&targetCol<8&&(targetRow!=pressedRow||targetCol!=pressedCol))onSquare(targetRow,targetCol);dragCandidate=false;}
                @Override public void mouseClicked(MouseEvent e){onSquare(row,col);}
            };
            sq.addMouseListener(listener);squareListeners[r][c]=listener;squares[r][c]=sq;boardGrid.add(sq);
        }
        squareHolder.add(boardGrid);add(squareHolder,BorderLayout.CENTER);message.setHorizontalAlignment(SwingConstants.CENTER);message.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));JPanel footer=new JPanel(new BorderLayout());footer.add(message,BorderLayout.CENTER);claimDraw.setEnabled(false);claimDraw.addActionListener(e->{if(game.claimThreefoldDraw()||game.claimFiftyMoveDraw()){timer.stop();refresh();}});footer.add(claimDraw,BorderLayout.EAST);add(footer,BorderLayout.SOUTH);
        refresh();lastTick=System.nanoTime();timer.start();
    }
    public void placeChessPiece(){refresh();}
    private void onSquare(int r,int c){if(game.isGameOver()||timedOut)return;advanceClock();if(clock.isFlagged()){timedOut=true;timeoutWinner=clock.getWinnerOnTime();timer.stop();refresh();return;}ChessPiece piece=game.getPiece(r,c);
        if(selectedRow<0){if(piece!=null&&piece.getColor()==game.getTurn())selectPiece(r,c);return;}
        if(piece!=null&&piece.getColor()==game.getTurn()){selectPiece(r,c);return;}
        ChessPiece mover=game.getPiece(selectedRow,selectedCol);ChessPiece.Category promotion=ChessPiece.Category.QUEEN;
        if(mover!=null&&mover.isPawn()&&(r==0||r==7)&&!GraphicsEnvironment.isHeadless()){
            Object[] options={"Queen","Rook","Bishop","Knight"};int choice=JOptionPane.showOptionDialog(this,"Promote pawn to:","Pawn promotion",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);if(choice>=0)promotion=new ChessPiece.Category[]{ChessPiece.Category.QUEEN,ChessPiece.Category.ROOK,ChessPiece.Category.BISHOP,ChessPiece.Category.KNIGHT}[choice];
        }
        boolean moved=game.move(selectedRow,selectedCol,r,c,promotion);selectedRow=selectedCol=-1;selectedMoves=Collections.emptyList();
        if(moved){if(game.isGameOver())timer.stop();else clock.moveCompleted();lastTick=System.nanoTime();}
        refresh();
    }
    private void advanceClock(){long now=System.nanoTime();long elapsed=(now-lastTick)/1_000_000L;lastTick=now;clock.tick(elapsed);}
    private void tick(){if(game.isGameOver()||clock.isFlagged())return;advanceClock();if(clock.isFlagged()){timer.stop();timedOut=true;timeoutWinner=clock.getWinnerOnTime();refresh();}else refreshClocks();}
    private String format(long ms){long sec=(ms+999)/1000;return String.format("%d:%02d",sec/60,sec%60);}
    private void refreshClocks(){whiteClock.setText("White  "+format(clock.getRemaining(ChessPiece.Color.WHITE)));blackClock.setText("Black  "+format(clock.getRemaining(ChessPiece.Color.BLACK)));whiteClock.setForeground(clock.getActive()==ChessPiece.Color.WHITE&&!game.isGameOver()&&!timedOut?new Color(30,90,180):Color.DARK_GRAY);blackClock.setForeground(clock.getActive()==ChessPiece.Color.BLACK&&!game.isGameOver()&&!timedOut?new Color(30,90,180):Color.DARK_GRAY);}
    private void selectPiece(int row,int col){selectedRow=row;selectedCol=col;selectedMoves=game.getLegalMoves(row,col);refresh();}
    private Color baseColor(int row,int col){return ((row+col)&1)==0?new Color(238,238,210):new Color(118,150,86);}
    private boolean isHighlighted(int row,int col){for(Spot move:selectedMoves)if(move.getRow()==row&&move.getColumn()==col)return true;return false;}
    private void refresh(){
        for(int r=0;r<8;r++)for(int c=0;c<8;c++){
            SpotPanel square=squares[r][c];ChessPiece piece=game.getPiece(r,c);square.getSpot().setChessPiece(piece);
            if(renderedPieces[r][c]!=piece){square.removeAll();if(piece!=null){try{BufferedImage image=ImageIO.read(new File(piece.getImage()));if(image!=null){int size=Math.max(24,Math.min(square.getWidth(),square.getHeight())-16);if(size<=24)size=64;Image scaled=image.getScaledInstance(size,size,Image.SCALE_SMOOTH);JLabel icon=new JLabel(new ImageIcon(scaled));icon.addMouseListener(squareListeners[r][c]);square.add(icon);}else square.add(new JLabel(pieceSymbol(piece)));}catch(IOException ignored){square.add(new JLabel(pieceSymbol(piece)));}}renderedPieces[r][c]=piece;}
            boolean checkedKing=piece!=null&&piece.isKing()&&piece.getColor()==game.getTurn()&&game.isInCheck(piece.getColor());
            boolean legalTarget=isHighlighted(r,c);square.setBackground(checkedKing?new Color(220,92,92):legalTarget?(piece==null?new Color(190,205,90):new Color(205,105,95)):baseColor(r,c));
            square.setBorder(r==selectedRow&&c==selectedCol?BorderFactory.createLineBorder(Color.YELLOW,4):null);square.revalidate();square.repaint();
        }
        if(timedOut)message.setText(timeoutWinner+" wins on time");else if(game.getStatus()==ChessGame.Status.CHECKMATE)message.setText((game.getTurn()==ChessPiece.Color.WHITE?"Black":"White")+" wins by checkmate");else if(game.getStatus()==ChessGame.Status.STALEMATE)message.setText("Draw by stalemate");else if(game.getStatus()==ChessGame.Status.DRAW_REPETITION)message.setText("Draw claimed by threefold repetition");else if(game.getStatus()==ChessGame.Status.DRAW_FIFTY_MOVE)message.setText("Draw claimed by 50-move rule");else if(game.getStatus()==ChessGame.Status.DRAW_FIVEFOLD_REPETITION)message.setText("Draw by fivefold repetition");else if(game.getStatus()==ChessGame.Status.DRAW_SEVENTY_FIVE_MOVE)message.setText("Draw by 75-move rule");else if(game.getStatus()==ChessGame.Status.DRAW_INSUFFICIENT)message.setText("Draw by insufficient material");else message.setText(game.getTurn()+" to move"+(game.getStatus()==ChessGame.Status.CHECK?" — CHECK":""));
        claimDraw.setEnabled(!timedOut&&(game.canClaimThreefoldDraw()||game.canClaimFiftyMoveDraw()));refreshClocks();
    }
    private String pieceSymbol(ChessPiece p){String[] symbols={"♟","♚","♛","♜","♞","♝"};int i=p.isPawn()?0:p.isKing()?1:p.isQueen()?2:p.isRook()?3:p.isKnight()?4:5;return symbols[i];}
}
