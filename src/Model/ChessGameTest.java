package Model;

/** Dependency-free regression suite; run with `java Model.ChessGameTest`. */
public final class ChessGameTest {
    private static void check(boolean value,String name){if(!value)throw new AssertionError(name);}
    private static void move(ChessGame g,int fr,int fc,int tr,int tc){check(g.move(fr,fc,tr,tc),"expected legal move "+fr+fc+tr+tc);}
    private static void move(ChessGame g,int fr,int fc,int tr,int tc,ChessPiece.Category promotion){check(g.move(fr,fc,tr,tc,promotion),"expected promotion move");}
    private static ChessGame position(ChessPiece.Color side,Object... pieces){ChessGame g=new ChessGame();g.setupForTest(side,pieces);return g;}
    public static void main(String[] args){
        ChessGame g=new ChessGame();
        check(g.isLegalMove(7,6,5,5),"knight legal");check(!g.isLegalMove(7,6,5,6),"knight geometry");
        check(!g.isLegalMove(7,2,3,6),"bishop blocked");check(!g.isLegalMove(7,0,5,0),"rook blocked");check(!g.isLegalMove(7,3,3,7),"queen blocked");
        check(!g.isLegalMove(6,4,7,4),"friendly occupied pawn destination");check(!g.isLegalMove(6,4,3,4),"pawn cannot jump");move(g,6,4,4,4);check(g.getTurn()==ChessPiece.Color.BLACK,"turn switches");check(!g.move(6,3,4,3),"wrong side cannot move");
        g=new ChessGame();move(g,6,4,4,4);move(g,1,0,2,0);move(g,4,4,3,4);move(g,1,3,3,3);check(g.isLegalMove(3,4,2,3),"en passant legal");move(g,3,4,2,3);check(g.getPiece(3,3)==null&&g.getPiece(2,3).isPawn(),"en passant removes captured pawn");
        g=new ChessGame();move(g,6,4,4,4);move(g,1,4,3,4);move(g,7,6,5,5);move(g,0,1,2,2);move(g,7,5,4,2);move(g,0,6,2,5);check(g.isLegalMove(7,4,7,6),"kingside castle legal");move(g,7,4,7,6);check(g.getPiece(7,6).isKing()&&g.getPiece(7,5).isRook(),"castle relocates rook");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK,4,4,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE,4,6,ChessPiece.Category.PAWN,ChessPiece.Color.BLACK);
        check(g.isLegalMove(4,4,4,6),"rook capture is legal");move(g,4,4,4,6);check(g.getPiece(4,4)==null&&g.getPiece(4,6).getColor()==ChessPiece.Color.WHITE,"capture removes opponent");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK,4,4,ChessPiece.Category.BISHOP,ChessPiece.Color.WHITE);
        check(g.isLegalMove(4,4,2,2)&&!g.isLegalMove(4,4,2,3),"bishop diagonal geometry");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK,4,4,ChessPiece.Category.QUEEN,ChessPiece.Color.WHITE);
        check(g.isLegalMove(4,4,4,0)&&g.isLegalMove(4,4,1,1)&&!g.isLegalMove(4,4,2,3),"queen combined movement");
        g=position(ChessPiece.Color.WHITE,0,0,ChessPiece.Category.KING,ChessPiece.Color.BLACK,4,4,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,4,ChessPiece.Category.ROOK,ChessPiece.Color.BLACK);
        check(g.isLegalMove(4,4,4,5)&&!g.isLegalMove(4,4,3,4),"king step and attacked square");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK,4,4,ChessPiece.Category.PAWN,ChessPiece.Color.WHITE,3,5,ChessPiece.Category.KNIGHT,ChessPiece.Color.BLACK);
        check(g.isLegalMove(4,4,3,5),"pawn diagonal capture movement");move(g,4,4,3,5);check(g.getPiece(3,5).isPawn(),"pawn capture works");
        g=position(ChessPiece.Color.WHITE,7,4,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,0,ChessPiece.Category.KING,ChessPiece.Color.BLACK,7,7,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE,0,5,ChessPiece.Category.ROOK,ChessPiece.Color.BLACK);
        check(!g.isLegalMove(7,4,7,6),"castling through attacked square forbidden");
        g=position(ChessPiece.Color.WHITE,7,4,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,0,ChessPiece.Category.KING,ChessPiece.Color.BLACK,6,4,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE,0,4,ChessPiece.Category.ROOK,ChessPiece.Color.BLACK);
        check(!g.isLegalMove(6,4,6,3),"pinned rook cannot expose king");
        g=position(ChessPiece.Color.WHITE,7,0,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,4,ChessPiece.Category.KING,ChessPiece.Color.BLACK,1,4,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE);check(!g.isLegalMove(1,4,0,4),"king may not be captured");
        g=position(ChessPiece.Color.WHITE,1,0,ChessPiece.Category.PAWN,ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK);
        move(g,1,0,0,0);check(g.getPiece(0,0).isQueen(),"promotion to queen");
        g=position(ChessPiece.Color.WHITE,1,0,ChessPiece.Category.PAWN,ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK);move(g,1,0,0,0,ChessPiece.Category.KNIGHT);check(g.getPiece(0,0).isKnight(),"underpromotion");
        g=new ChessGame();move(g,6,5,5,5);move(g,1,4,3,4);move(g,6,6,4,6);move(g,0,3,4,7);check(g.getStatus()==ChessGame.Status.CHECKMATE,"fool's mate checkmate");check(!g.move(7,4,6,4),"game stops after mate");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,4,ChessPiece.Category.KING,ChessPiece.Color.BLACK,3,0,ChessPiece.Category.QUEEN,ChessPiece.Color.WHITE);
        move(g,3,0,3,4);check(g.getStatus()==ChessGame.Status.CHECK&&g.isInCheck(ChessPiece.Color.BLACK),"check is recognized after a legal move");check(g.getTurn()==ChessPiece.Color.BLACK&&g.getLegalMoves(0,4).size()>0,"checked side receives legal responses");
        g=position(ChessPiece.Color.BLACK,0,7,ChessPiece.Category.KING,ChessPiece.Color.BLACK,2,6,ChessPiece.Category.QUEEN,ChessPiece.Color.WHITE,1,5,ChessPiece.Category.KING,ChessPiece.Color.WHITE);check(g.getStatus()==ChessGame.Status.STALEMATE,"stalemate");
        g=new ChessGame();move(g,7,6,5,5);move(g,0,6,2,5);move(g,5,5,7,6);move(g,2,5,0,6);move(g,7,6,5,5);move(g,0,6,2,5);move(g,5,5,7,6);move(g,2,5,0,6);check(g.canClaimThreefoldDraw(),"threefold repetition is claimable");check(g.claimThreefoldDraw()&&g.isGameOver(),"threefold claim ends game");
        g=new ChessGame();for(int i=0;i<4;i++){move(g,7,6,5,5);move(g,0,6,2,5);move(g,5,5,7,6);move(g,2,5,0,6);}check(g.getStatus()==ChessGame.Status.DRAW_FIVEFOLD_REPETITION,"fivefold repetition automatic draw");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,0,ChessPiece.Category.KING,ChessPiece.Color.BLACK,6,1,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE);g.setHalfmoveForTest(99);move(g,6,1,5,1);check(g.canClaimFiftyMoveDraw()&&g.claimFiftyMoveDraw(),"50-move draw is claimable");
        g=position(ChessPiece.Color.WHITE,7,7,ChessPiece.Category.KING,ChessPiece.Color.WHITE,0,0,ChessPiece.Category.KING,ChessPiece.Color.BLACK,6,1,ChessPiece.Category.ROOK,ChessPiece.Color.WHITE);g.setHalfmoveForTest(149);move(g,6,1,5,1);check(g.getStatus()==ChessGame.Status.DRAW_SEVENTY_FIVE_MOVE,"75-move draw automatic");
        ChessClock invalidMoveClock=new ChessClock(10_000,1_000);g=new ChessGame();check(!g.move(6,4,3,4),"invalid move rejected");check(invalidMoveClock.getActive()==ChessPiece.Color.WHITE&&invalidMoveClock.getRemaining(ChessPiece.Color.WHITE)==10_000,"invalid move leaves clock unchanged");
        ChessClock clock=new ChessClock(10_000,2_000);clock.tick(3_000);check(clock.getRemaining(ChessPiece.Color.WHITE)==7_000&&clock.getActive()==ChessPiece.Color.WHITE,"clock counts active player only");clock.moveCompleted();check(clock.getRemaining(ChessPiece.Color.WHITE)==9_000&&clock.getActive()==ChessPiece.Color.BLACK,"clock increments and switches");clock.tick(10_000);check(clock.isFlagged()&&clock.getWinnerOnTime()==ChessPiece.Color.WHITE,"flag fall ends clock with other player winner");clock.moveCompleted();check(clock.getActive()==ChessPiece.Color.BLACK,"flag fall cannot switch clock");
        System.out.println("ChessGameTest: all checks passed");
    }
}
