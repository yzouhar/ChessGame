package Model;

/** Deterministic two-player clock; callers supply elapsed milliseconds (normally from a Swing timer). */
public final class ChessClock {
    private final long[] remaining = new long[2];
    private final long increment;
    private ChessPiece.Color active = ChessPiece.Color.WHITE;
    private ChessPiece.Color flagFall;
    public ChessClock(long startingMillis,long incrementMillis){if(startingMillis<=0||incrementMillis<0)throw new IllegalArgumentException("Invalid time control");remaining[0]=remaining[1]=startingMillis;increment=incrementMillis;}
    public ChessPiece.Color getActive(){return active;}
    public long getRemaining(ChessPiece.Color color){return remaining[index(color)];}
    public boolean isFlagged(){return flagFall!=null;}
    public ChessPiece.Color getWinnerOnTime(){return flagFall==null?null:opposite(flagFall);}
    public void tick(long elapsedMillis){if(isFlagged()||elapsedMillis<=0)return;int i=index(active);remaining[i]=Math.max(0,remaining[i]-elapsedMillis);if(remaining[i]==0)flagFall=active;}
    /** Invoke exactly once, and only after a legal chess move was accepted. */
    public void moveCompleted(){if(isFlagged())return;remaining[index(active)]+=increment;active=opposite(active);}
    private int index(ChessPiece.Color color){return color==ChessPiece.Color.WHITE?0:1;}
    private ChessPiece.Color opposite(ChessPiece.Color color){return color==ChessPiece.Color.WHITE?ChessPiece.Color.BLACK:ChessPiece.Color.WHITE;}
}
