package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/** Complete, UI-independent chess rules and position state. Rows increase from Black's side to White's. */
public final class ChessGame {
    public enum Status { ACTIVE, CHECK, CHECKMATE, STALEMATE, DRAW_REPETITION, DRAW_FIFTY_MOVE, DRAW_SEVENTY_FIVE_MOVE, DRAW_FIVEFOLD_REPETITION, DRAW_INSUFFICIENT }
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private ChessPiece.Color turn = ChessPiece.Color.WHITE;
    private boolean whiteKingMoved, blackKingMoved, whiteQueenRookMoved, whiteKingRookMoved, blackQueenRookMoved, blackKingRookMoved;
    private int epRow = -1, epCol = -1, halfmove;
    private final Map<String,Integer> positions = new HashMap<>();
    private Status status = Status.ACTIVE;

    public ChessGame() { reset(); }
    public void reset() {
        for (int r=0;r<8;r++) for(int c=0;c<8;c++) board[r][c]=null;
        ChessPiece.Category[] back={ChessPiece.Category.ROOK,ChessPiece.Category.KNIGHT,ChessPiece.Category.BISHOP,ChessPiece.Category.QUEEN,ChessPiece.Category.KING,ChessPiece.Category.BISHOP,ChessPiece.Category.KNIGHT,ChessPiece.Category.ROOK};
        for(int c=0;c<8;c++){put(0,c,back[c],ChessPiece.Color.BLACK);put(1,c,ChessPiece.Category.PAWN,ChessPiece.Color.BLACK);put(6,c,ChessPiece.Category.PAWN,ChessPiece.Color.WHITE);put(7,c,back[c],ChessPiece.Color.WHITE);}
        turn=ChessPiece.Color.WHITE; whiteKingMoved=blackKingMoved=whiteQueenRookMoved=whiteKingRookMoved=blackQueenRookMoved=blackKingRookMoved=false;
        epRow=epCol=-1; halfmove=0; positions.clear(); positions.put(key(),1); status=Status.ACTIVE;
    }
    void setupForTest(ChessPiece.Color side, Object... pieces) {
        for(int r=0;r<8;r++)for(int c=0;c<8;c++)board[r][c]=null;
        for(int i=0;i<pieces.length;i+=4)put((Integer)pieces[i],(Integer)pieces[i+1],(ChessPiece.Category)pieces[i+2],(ChessPiece.Color)pieces[i+3]);
        turn=side;whiteKingMoved=blackKingMoved=whiteQueenRookMoved=whiteKingRookMoved=blackQueenRookMoved=blackKingRookMoved=false;epRow=epCol=-1;halfmove=0;positions.clear();positions.put(key(),1);refreshStatus();
    }
    void setHalfmoveForTest(int value){halfmove=value;refreshStatus();}
    private void put(int r,int c,ChessPiece.Category cat,ChessPiece.Color color){ChessPiece p=new ChessPiece(cat,color,imageFor(cat,color));board[r][c]=p;p.setPosition(new Spot(r,c));}
    private String imageFor(ChessPiece.Category cat,ChessPiece.Color color){return "resources/images/"+color.name().toLowerCase(Locale.ROOT)+cat.name().substring(0,1)+cat.name().substring(1).toLowerCase()+".png";}
    public ChessPiece getPiece(int r,int c){return inside(r,c)?board[r][c]:null;}
    public ChessPiece.Color getTurn(){return turn;}
    public Status getStatus(){return status;}
    public boolean canClaimThreefoldDraw(){return !isGameOver()&&positions.getOrDefault(key(),0)>=3;}
    public boolean canClaimFiftyMoveDraw(){return !isGameOver()&&halfmove>=100;}
    public boolean claimThreefoldDraw(){if(!canClaimThreefoldDraw())return false;status=Status.DRAW_REPETITION;return true;}
    public boolean claimFiftyMoveDraw(){if(!canClaimFiftyMoveDraw())return false;status=Status.DRAW_FIFTY_MOVE;return true;}
    public boolean isGameOver(){return status!=Status.ACTIVE&&status!=Status.CHECK;}
    public boolean isInCheck(ChessPiece.Color side){int kr=-1,kc=-1;for(int r=0;r<8;r++)for(int c=0;c<8;c++){ChessPiece p=board[r][c];if(p!=null&&p.isKing()&&p.getColor()==side){kr=r;kc=c;}}return kr<0||attacked(kr,kc,opposite(side));}
    public boolean isLegalMove(int fr,int fc,int tr,int tc){return legal(fr,fc,tr,tc);}
    public List<Spot> getLegalMoves(int row,int col){
        List<Spot> moves=new ArrayList<>();
        if(!inside(row,col)||board[row][col]==null||board[row][col].getColor()!=turn)return moves;
        for(int targetRow=0;targetRow<8;targetRow++)for(int targetCol=0;targetCol<8;targetCol++)
            if(legal(row,col,targetRow,targetCol))moves.add(new Spot(targetRow,targetCol));
        return moves;
    }
    public boolean move(int fr,int fc,int tr,int tc){return move(fr,fc,tr,tc,ChessPiece.Category.QUEEN);}
    public boolean move(int fr,int fc,int tr,int tc,ChessPiece.Category promotion){
        if(isGameOver()||!legal(fr,fc,tr,tc))return false;
        ChessPiece p=board[fr][fc], captured=board[tr][tc]; boolean pawn=p.isPawn();
        if(pawn&&tc!=fc&&captured==null&&tr==epRow&&tc==epCol){captured=board[fr][tc];board[fr][tc]=null;}
        if(p.isKing()&&Math.abs(tc-fc)==2){int rookFrom=tc>fc?7:0,rookTo=tc>fc?tc-1:tc+1;board[tr][rookTo]=board[tr][rookFrom];board[tr][rookFrom]=null;board[tr][rookTo].setPosition(new Spot(tr,rookTo));}
        board[tr][tc]=p;board[fr][fc]=null;p.setPosition(new Spot(tr,tc));
        if(pawn&&(tr==0||tr==7)){if(promotion!=ChessPiece.Category.QUEEN&&promotion!=ChessPiece.Category.ROOK&&promotion!=ChessPiece.Category.BISHOP&&promotion!=ChessPiece.Category.KNIGHT)promotion=ChessPiece.Category.QUEEN;p.setCategory(promotion);p.setImage(imageFor(promotion,p.getColor()));}
        updateRights(p,fr,fc,captured,tr,tc);
        epRow=epCol=-1;if(pawn&&Math.abs(tr-fr)==2){epRow=(tr+fr)/2;epCol=fc;}
        halfmove=(pawn||captured!=null)?0:halfmove+1;
        turn=opposite(turn);String k=key();positions.put(k,positions.getOrDefault(k,0)+1);refreshStatus();return true;
    }
    private void updateRights(ChessPiece p,int fr,int fc,ChessPiece captured,int tr,int tc){if(p.isKing()){if(p.getColor()==ChessPiece.Color.WHITE)whiteKingMoved=true;else blackKingMoved=true;}if(p.isRook()){if(fr==7&&fc==0)whiteQueenRookMoved=true;if(fr==7&&fc==7)whiteKingRookMoved=true;if(fr==0&&fc==0)blackQueenRookMoved=true;if(fr==0&&fc==7)blackKingRookMoved=true;}if(captured!=null&&captured.isRook()){if(tr==7&&tc==0)whiteQueenRookMoved=true;if(tr==7&&tc==7)whiteKingRookMoved=true;if(tr==0&&tc==0)blackQueenRookMoved=true;if(tr==0&&tc==7)blackKingRookMoved=true;}}
    private boolean legal(int fr,int fc,int tr,int tc){if(!inside(fr,fc)||!inside(tr,tc)||(fr==tr&&fc==tc))return false;ChessPiece p=board[fr][fc];if(p==null||p.getColor()!=turn)return false;ChessPiece dest=board[tr][tc];if(dest!=null&&(dest.getColor()==p.getColor()||dest.isKing()))return false;if(!pseudo(fr,fc,tr,tc,true))return false;
        ChessPiece old=board[tr][tc];int capr=-1,capc=-1;ChessPiece ep=null;if(p.isPawn()&&tc!=fc&&old==null){capr=fr;capc=tc;ep=board[capr][capc];board[capr][capc]=null;}
        int rookFrom=-1,rookTo=-1;if(p.isKing()&&Math.abs(tc-fc)==2){rookFrom=tc>fc?7:0;rookTo=tc>fc?tc-1:tc+1;board[tr][rookTo]=board[tr][rookFrom];board[tr][rookFrom]=null;}
        board[tr][tc]=p;board[fr][fc]=null;boolean safe=!isInCheck(p.getColor());board[fr][fc]=p;board[tr][tc]=old;if(ep!=null)board[capr][capc]=ep;if(rookFrom>=0){board[tr][rookFrom]=board[tr][rookTo];board[tr][rookTo]=null;}return safe;}
    private boolean pseudo(int fr,int fc,int tr,int tc,boolean castles){ChessPiece p=board[fr][fc];int dr=tr-fr,dc=tc-fc,ar=Math.abs(dr),ac=Math.abs(dc);ChessPiece dest=board[tr][tc];switch(p.getCategory()){
        case KNIGHT:return ar*ac==2;
        case KING:if(Math.max(ar,ac)==1)return true;if(castles&&dr==0&&ac==2)return canCastle(fr,fc,tc);return false;
        case ROOK:if(dr!=0&&dc!=0)return false;return clear(fr,fc,tr,tc);
        case BISHOP:if(ar!=ac)return false;return clear(fr,fc,tr,tc);
        case QUEEN:if(!(dr==0||dc==0||ar==ac))return false;return clear(fr,fc,tr,tc);
        case PAWN:int dir=p.getColor()==ChessPiece.Color.WHITE?-1:1,start=p.getColor()==ChessPiece.Color.WHITE?6:1;if(dc==0){if(dest!=null)return false;if(dr==dir)return true;return fr==start&&dr==2*dir&&board[fr+dir][fc]==null;}if(ac==1&&dr==dir){if(dest!=null)return dest.getColor()!=p.getColor();return tr==epRow&&tc==epCol&&board[fr][tc]!=null&&board[fr][tc].isPawn()&&board[fr][tc].getColor()!=p.getColor();}return false;
        default:return false;}}
    private boolean canCastle(int r,int c,int tc){ChessPiece.Color side=turn;boolean kingSide=tc>c;int home=side==ChessPiece.Color.WHITE?7:0;ChessPiece rook=board[home][kingSide?7:0];if(r!=home||c!=4||rook==null||!rook.isRook()||rook.getColor()!=side)return false;if(side==ChessPiece.Color.WHITE?(whiteKingMoved||(kingSide?whiteKingRookMoved:whiteQueenRookMoved)):(blackKingMoved||(kingSide?blackKingRookMoved:blackQueenRookMoved)))return false;if(kingSide?(board[home][5]!=null||board[home][6]!=null):(board[home][1]!=null||board[home][2]!=null||board[home][3]!=null))return false;ChessPiece.Color enemy=opposite(side);return !attacked(home,4,enemy)&&!attacked(home,kingSide?5:3,enemy)&&!attacked(home,kingSide?6:2,enemy);}
    private boolean clear(int fr,int fc,int tr,int tc){int sr=Integer.compare(tr,fr),sc=Integer.compare(tc,fc);for(int r=fr+sr,c=fc+sc;r!=tr||c!=tc;r+=sr,c+=sc)if(board[r][c]!=null)return false;return true;}
    private boolean attacked(int r,int c,ChessPiece.Color by){for(int x=0;x<8;x++)for(int y=0;y<8;y++){ChessPiece p=board[x][y];if(p==null||p.getColor()!=by)continue;int dr=r-x,dc=c-y,ar=Math.abs(dr),ac=Math.abs(dc);switch(p.getCategory()){case PAWN:if(dr==(by==ChessPiece.Color.WHITE?-1:1)&&ac==1)return true;break;case KNIGHT:if(ar*ac==2)return true;break;case KING:if(Math.max(ar,ac)==1)return true;break;case ROOK:if((dr==0||dc==0)&&clear(x,y,r,c))return true;break;case BISHOP:if(ar==ac&&clear(x,y,r,c))return true;break;case QUEEN:if((dr==0||dc==0||ar==ac)&&clear(x,y,r,c))return true;break;}}return false;}
    private boolean hasMove(){for(int r=0;r<8;r++)for(int c=0;c<8;c++){ChessPiece p=board[r][c];if(p!=null&&p.getColor()==turn)for(int tr=0;tr<8;tr++)for(int tc=0;tc<8;tc++)if(legal(r,c,tr,tc))return true;}return false;}
    private void refreshStatus(){if(!hasMove()){status=isInCheck(turn)?Status.CHECKMATE:Status.STALEMATE;return;}if(isInCheck(turn)){status=Status.CHECK;return;}if(positions.getOrDefault(key(),0)>=5)status=Status.DRAW_FIVEFOLD_REPETITION;else if(halfmove>=150)status=Status.DRAW_SEVENTY_FIVE_MOVE;else if(insufficient())status=Status.DRAW_INSUFFICIENT;else status=Status.ACTIVE;}
    private boolean insufficient(){int minors=0;boolean bishopsOnly=true;int color=-1;for(int r=0;r<8;r++)for(int c=0;c<8;c++){ChessPiece p=board[r][c];if(p==null||p.isKing())continue;if(p.isPawn()||p.isRook()||p.isQueen())return false;minors++;if(p.isKnight())bishopsOnly=false;else {int sq=(r+c)&1;if(color<0)color=sq;else if(color!=sq)bishopsOnly=false;}}return minors<=1||(bishopsOnly&&minors>0);}
    private String key(){StringBuilder s=new StringBuilder();for(int r=0;r<8;r++)for(int c=0;c<8;c++){ChessPiece p=board[r][c];s.append(p==null?'.':(p.getColor()==ChessPiece.Color.WHITE?"w":"b")+pieceCode(p.getCategory()));}s.append(turn);if(hasEnPassantCapture())s.append(epRow).append(',').append(epCol);else s.append("--");s.append(whiteKingMoved?1:0).append(blackKingMoved?1:0).append(whiteQueenRookMoved?1:0).append(whiteKingRookMoved?1:0).append(blackQueenRookMoved?1:0).append(blackKingRookMoved?1:0);return s.toString();}
    private boolean hasEnPassantCapture(){if(epRow<0)return false;int pawnRow=epRow+(turn==ChessPiece.Color.WHITE?1:-1);for(int c:new int[]{epCol-1,epCol+1})if(inside(pawnRow,c)){ChessPiece p=board[pawnRow][c];if(p!=null&&p.isPawn()&&p.getColor()==turn)return true;}return false;}
    private char pieceCode(ChessPiece.Category category){switch(category){case PAWN:return 'p';case KING:return 'k';case QUEEN:return 'q';case ROOK:return 'r';case KNIGHT:return 'n';default:return 'b';}}
    private boolean inside(int r,int c){return r>=0&&r<8&&c>=0&&c<8;}
    private ChessPiece.Color opposite(ChessPiece.Color c){return c==ChessPiece.Color.WHITE?ChessPiece.Color.BLACK:ChessPiece.Color.WHITE;}
}
