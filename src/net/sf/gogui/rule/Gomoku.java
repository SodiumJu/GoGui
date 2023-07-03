package net.sf.gogui.rule;

import java.util.ArrayList;
import java.util.List;
import net.sf.gogui.go.BlackWhiteSet;
import net.sf.gogui.go.GoColor;
import net.sf.gogui.go.GoPoint;
import net.sf.gogui.gui.MessageDialogs;

public class Gomoku extends Rule {

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BORDER = 3;

    public Gomoku(int s) {
        reset(s);
    }
    
    private int size;
    private int NS;
    private int maxpoint = 19;
    private int winner;
    private ArrayList<Integer> board = new ArrayList<Integer>();
    private final MessageDialogs m_messageDialogs = new MessageDialogs();
    private String m_EndMoveoptionalMessage;
    private List<Point> list;

    public void reset(int boardsize) 
    {
        this.size = boardsize;
        this.NS = boardsize + 1;
        this.winner = 0;
        this.maxpoint = boardsize * boardsize + (3 * (boardsize + 1));
        board.clear();
        for (int i = 0; i < maxpoint; i++)
            board.add(BORDER);
        for (int row = 1; row <= boardsize; row++) 
        {
            int start = row * this.NS + 1;
            int end = start + size;
            for (int i = start; i < end; i++)
                board.set(i, EMPTY);
        }
    }
    
    private GoColor int2Gocolor(int c)
    {
        GoColor color=GoColor.EMPTY;
        switch(c)
        {
            case 0:
                color=GoColor.EMPTY;
                break;
            case 1:
                color=GoColor.BLACK;
                break;
            case 2:
                color=GoColor.WHITE;
                break;
        }
        return color;
    }

    public boolean isEnd() 
    {
        if (this.winner != EMPTY) 
        {
            m_EndMoveoptionalMessage="The game is finished. "+int2Gocolor(this.winner).toString()+" win!";
            return true;
        }    
        return false;
    }
    
    public String getEndMoveoptionalMessage()
    {
        return m_EndMoveoptionalMessage;
    }

    private int GoColor2int(GoColor c) 
    {
        String color = c.getUppercaseLetter();
        if (color.equals("B")) 
            return BLACK;
        else if (color.equals("W")) 
            return WHITE;
        return BORDER;
    }
    
    private BlackWhiteSet<Integer> captured
        = new BlackWhiteSet<Integer>(0, 0);
    
    public BlackWhiteSet<Integer> getcaptured()
    {
        return captured;
    }

    public List<Point> play(Point point) 
    {
        GoPoint p = RuleUtil.rulePointToGoPoint(point);
        GoColor c = RuleUtil.rulePointToGoColor(point);
        if (!isLegalMove(point) || this.winner != 0) 
            return list;
        int color = GoColor2int(c);
        board.set(Gopoint2index(p), color);
        if (search_for_five(p, c)) 
            this.winner = color;
        return list;
    }
    
    public String gameName()
    {
        return "Gomoku";
    }

    public boolean isLegalMove(Point point) 
    {
        GoPoint p = RuleUtil.rulePointToGoPoint(point);
        if (board.get(Gopoint2index(p)) == EMPTY) 
            return true;
        return false;
    }

    private int Gopoint2index(GoPoint coordinate) 
    {
        String coor = coordinate.toString();
        int col = coor.charAt(0) - 'A';
        if (col < 8) 
            col++;
        int row = Integer.parseInt(coor.substring(1));
        int point = (this.size + 1) * row + col;
        return point;
    }

    private boolean search_for_five(GoPoint point, GoColor color) 
    {
        int p = Gopoint2index(point);
        int c = GoColor2int(color);
        int trace1 = p - 1;
        int trace2 = p + 1;
        int count = 1;
        while ((this.board.get(trace1) == c) || (this.board.get(trace2) == c)) 
        {
            if (this.board.get(trace1) == c) 
            {
                count++;
                trace1--;
            }
            if (this.board.get(trace2) == c) 
            {
                count++;
                trace2++;
            }
        }
        if (count >= 5) 
            return true;
        trace1 = p - this.NS;
        trace2 = p + this.NS;
        count = 1;
        while ((this.board.get(trace1) == c) || (this.board.get(trace2) == c)) 
        {
            if (this.board.get(trace1) == c) 
            {
                count++;
                trace1 -= this.NS;
            }
            if (this.board.get(trace2) == c) 
            {
                count++;
                trace2 += this.NS;
            }
        }
        if (count >= 5) 
            return true;
        trace1 = p + this.NS - 1;
        trace2 = p - this.NS + 1;
        count = 1;
        while ((this.board.get(trace1) == c) || (this.board.get(trace2) == c)) 
        {
            if (this.board.get(trace1) == c) 
            {
                count++;
                trace1 += this.NS - 1;
            }
            if (this.board.get(trace2) == c) 
            {
                count++;
                trace2 = trace2 - this.NS + 1;
            }
        }
        if (count >= 5)
            return true;
        trace1 = p + this.NS + 1;
        trace2 = p - this.NS - 1;
        count = 1;
        while ((this.board.get(trace1) == c) || (this.board.get(trace2) == c)) 
        {
            if (this.board.get(trace1) == c) 
            {
                count++;
                trace1 += this.NS + 1;
            }
            if (this.board.get(trace2) == c) 
            {
                count++;
                trace2 = trace2 - this.NS - 1;
            }
        }
        if (count >= 5)
            return true;
        return false;
    }
}
