package net.sf.gogui.rule;

import java.util.ArrayList;
import java.util.List;
import net.sf.gogui.go.BlackWhiteSet;
import net.sf.gogui.go.GoColor;
import net.sf.gogui.go.GoPoint;
import net.sf.gogui.go.PointList;
import net.sf.gogui.go.Marker;
import net.sf.gogui.go.Board;

public class Go extends Rule 
{
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BORDER = 3;

    public Go(int s) 
    {
        reset(s);
    }

    private ArrayList<Integer> board = new ArrayList<Integer>();
    private ArrayList<Integer> oldboard = new ArrayList<Integer>();
    private int size;
    private int NS;
    private int maxpoint = 19;
    public PointList m_killed;
    private final PointList m_checkKillStack = new PointList();
    private GoColor tomove = GoColor.BLACK;
    private GoColor oldmove;

    public void reset(int boardsize) 
    {
        this.size = boardsize;
        this.NS = boardsize + 1;
        m_mark = new Marker(boardsize);
        this.maxpoint = boardsize * boardsize + (3 * (boardsize + 1));
        board.clear();
        captured.set(int2Gocolor(BLACK), 0);
        captured.set(int2Gocolor(WHITE), 0);
        m_koPoint = null;
        for (int i = 0; i < maxpoint; i++) 
        {
            board.add(BORDER);
        }
        for (int row = 1; row <= boardsize; row++) 
        {
            int start = row * this.NS + 1;
            int end = start + size;
            for (int i = start; i < end; i++)
            {
                board.set(i, EMPTY);
            }
        }
    }

    public boolean isEnd()
    {
        if (false) 
        {
            return true;
        }
        return false;
    }

    private int GoColor2int(GoColor c)
    {
        String color = c.getUppercaseLetter();
        if (color.equals("B"))
        {
            return BLACK;
        } 
        else if (color.equals("W"))
        {
            return WHITE;
        }
        return BORDER;
    }

    private ArrayList<GoPoint> getAdjacent(GoPoint point)
    {
        int index = Gopoint2index(point);
        int temp = index;
        ArrayList<GoPoint> list = new ArrayList<GoPoint>();
        if (board.get(temp + this.NS) != BORDER)
        {
            list.add(index2Gopoint(temp + this.NS));
        }
        if (board.get(temp - this.NS) != BORDER)
        {
            list.add(index2Gopoint(temp - this.NS));
        }
        if (board.get(temp - 1) != BORDER) 
        {
            list.add(index2Gopoint(temp - 1));
        }
        if (board.get(temp + 1) != BORDER) 
        {
            list.add(index2Gopoint(temp + 1));
        }
        return list;
    }

    public boolean isKo(GoPoint point)
    {
        return point == m_koPoint;
    }

    private void setStone(GoPoint p, GoColor c)
    {
        int color = GoColor2int(c);
        int point = Gopoint2index(p);
        this.board.set(point, color);
    }

    private Marker m_mark;
    private final PointList m_checkKillStones = new PointList();
    private GoPoint m_koPoint;
    public PointList m_suicide;
    public BlackWhiteSet<Integer> captured
            = new BlackWhiteSet<Integer>(0, 0);
    private Board m_board;
    private String m_illegalMoveMainMessage;
    private String m_illegalMoveoptionalMessage;
    private String m_illegalMoveDestructiveOption;
    private List<Point> killlist;
    private List<Point> oldkilllist;

    public List<Point> play(Point point)
    {
        GoPoint p = RuleUtil.rulePointToGoPoint(point);
        GoColor c = RuleUtil.rulePointToGoColor(point);
        m_mark = new Marker(size);
        m_killed = new PointList();
        m_suicide = new PointList();
        killlist = new ArrayList<Point>();
        setStone(p, c);
        for (GoPoint adj : getAdjacent(p))
        {
            int killedSize = m_killed.size();
            if (board.get(Gopoint2index(adj)) == GoColor2int(c.otherColor()))
            {
                checkKill(adj, m_killed);
            }
            if (m_killed.size() == killedSize + 1) 
            {
                m_koPoint = m_killed.get(killedSize);
            }
        }
        checkKill(p, m_suicide);
        if (this.m_koPoint != null && !isSingleStoneSingleLib(p, c)) 
        {
            this.m_koPoint = null;
        }
        captured.set(c, captured.get(c) + m_suicide.size());
        captured.set(c.otherColor(), captured.get(c.otherColor()) + m_killed.size());
        tomove = c.otherColor();

        return killlist;
    }

    public BlackWhiteSet<Integer> getcaptured() 
    {
        return captured;
    }

    public String gameName() 
    {
        return "Go";
    }

    private boolean isSingleStoneSingleLib(GoPoint point, GoColor color) {
        if (board.get(Gopoint2index(point)) != GoColor2int(color))
        {
            return false;
        }
        int lib = 0;
        for (GoPoint adj : getAdjacent(point))
        {
            int adjColor = board.get(Gopoint2index(adj));
            if (adjColor == EMPTY) 
            {
                ++lib;
                if (lib > 1)
                {
                    return false;
                }
            }
            else if (adjColor == GoColor2int(color)) 
            {
                return false;
            }
        }
        return true;
    }

    private void checkKill(GoPoint point, PointList killed)
    {
        int color = board.get(Gopoint2index(point));
        m_checkKillStack.clear();
        m_checkKillStack.add(point);
        m_mark.set(point);
        m_checkKillStones.clear();
        //killlist.clear();
        boolean isDead = true;
        ArrayList<GoPoint> adjacent = new ArrayList<GoPoint>();
        while (isDead && !m_checkKillStack.isEmpty()) 
        {
            GoPoint p = m_checkKillStack.pop();
            m_checkKillStones.add(p);
            adjacent = getAdjacent(p);
            int nuAdjacent = adjacent.size();
            // Don't use an iterator for efficiency
            for (int i = 0; i < nuAdjacent; ++i) 
            {
                GoPoint adj = adjacent.get(i);
                int c = board.get(Gopoint2index(adj));
                if (c == EMPTY) 
                {
                    isDead = false;
                    break;
                }
                if (m_mark.get(adj) || !(c == color))
                {
                    continue;
                }
                m_checkKillStack.add(adj);
                m_mark.set(adj);
            }
        }
        if (isDead)
        {
            killed.addAll(m_checkKillStones);
            int nuKillStones = m_checkKillStones.size();
            // Don't use an iterator for efficiency
            for (int i = 0; i < nuKillStones; ++i)
            {
                board.set(Gopoint2index(m_checkKillStones.get(i)), EMPTY);
                killlist.add(RuleUtil.goPointToRulePoint(m_checkKillStones.get(i), int2Gocolor(EMPTY)));
            }
        }
        m_mark.clear(m_checkKillStack);
        m_mark.clear(m_checkKillStones);
    }

    private boolean isSuicide(GoPoint p) 
    {
        oldmove = tomove;
        oldboard = board;
        oldkilllist = killlist;
        play(RuleUtil.goPointToRulePoint(p, tomove));
        boolean result = (!m_suicide.isEmpty());
        board = oldboard;
        killlist = oldkilllist;
        tomove = oldmove;
        return result;
    }

    public boolean isLegalMove(Point p)
    {
        if (isKo(RuleUtil.rulePointToGoPoint(p)))
        {
            m_illegalMoveMainMessage = "Play illegal Ko move?";
            m_illegalMoveoptionalMessage = "This move violates the Ko rule, because it repeats the previous position for the color to play.";
            m_illegalMoveDestructiveOption = "Play Illegal Ko";
            return false;
        } else if (isSuicide(RuleUtil.rulePointToGoPoint(p))) 
        {
            m_illegalMoveMainMessage = "Play suicide?";
            m_illegalMoveoptionalMessage = "Playing at this point will leave the stone without liberties and it will be immediately captured. Suicide is not allowed under all Go rule sets.";
            m_illegalMoveDestructiveOption = "Play Suicide";
            return false;
        }
        return true;
    }

    public String getIllegalMoveMainMessage()
    {
        return m_illegalMoveMainMessage;
    }

    public String getIllegalMoveoptionalMessage() 
    {
        return m_illegalMoveoptionalMessage;
    }

    public String getIllegalMoveDestructiveOption() 
    {
        return m_illegalMoveDestructiveOption;
    }

    private GoPoint index2Gopoint(int index) 
    {
        Integer row = index / this.NS - 1;
        Integer col = index % this.NS - 1;
        GoPoint point = GoPoint.get(col, row);
        return point;
    }

    private GoColor int2Gocolor(int c)
    {
        GoColor color = GoColor.EMPTY;
        switch (c)
        {
            case 0:
                color = GoColor.EMPTY;
                break;
            case 1:
                color = GoColor.BLACK;
                break;
            case 2:
                color = GoColor.WHITE;
                break;
        }
        return color;
    }

    private int Gopoint2index(GoPoint coordinate) 
    {
        String coor = coordinate.toString();
        int col = coor.charAt(0) - 'A';
        if (col < 8) 
        {
            col++;
        }
        int row = Integer.parseInt(coor.substring(1));
        int point = (this.size + 1) * row + col;
        return point;
    }
}