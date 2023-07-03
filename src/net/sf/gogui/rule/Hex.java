
package net.sf.gogui.rule;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tylerliu
 */
public class Hex extends Rule
{
    private int boardSize;
    private Color[][] board;
    private boolean gameFinished;
    private String m_EndMoveoptionalMessage;
    private Color winner;
    
    public Hex(int s) 
    {
        reset(s);
    }

    public String gameName() 
    {
        return "Hex";
    }

    public void reset(int size) 
    {
        this.boardSize = size;
        board = new Color[size][size];
        gameFinished = false;
        this.winner = Color.EMPTY;
        for (int i = 0; i < size; i++) 
        {
            for (int j = 0; j < size; j++) 
            {
                board[i][j] = Color.EMPTY;
            }
        }
    }

    public boolean isEnd()
    {
        if (gameFinished) 
        {
            m_EndMoveoptionalMessage = "The game is finished. " + enum2String(this.winner) + " win!";
            return true;
        }    
        return false;
    }
    
    public String getEndMoveoptionalMessage()
    {
        return m_EndMoveoptionalMessage;
    }
    
    private String enum2String(Color color)
    {
        switch(color)
        {
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            default:
                return "";
        }
    }

    public boolean isLegalMove(Point p)
    {
        if (p.x < 0 || p.x >= boardSize || p.y < 0 || p.y >= boardSize) 
        {
            return false;
        }
        return board[p.x][p.y] == Color.EMPTY;
    }
    
    private List<Point> result = new ArrayList<>();

    public List<Point> play(Point p) 
    {
        if (!isLegalMove(p))
        {
            throw new IllegalArgumentException("Illegal move");
        }

        board[p.x][p.y] = p.color;
       
        if (hasWon(p))
        {
            gameFinished = true;
        }else{
            gameFinished = false;
        }

        return result;
    }
    
    private boolean hasWon(Point p)
    {
        if (p.color == Color.EMPTY) 
        {
            return false;
        }

        boolean[][] visited = new boolean[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++) 
            {
                visited[i][j] = false;
            }
        }
        
        boolean[] frontEnd = {false, false};
        frontEnd = dfs(p.x, p.y, p.color, visited, frontEnd);
        return (frontEnd[0] && frontEnd[1]);
    }
    
    private boolean[] dfs(int x, int y, Color color, boolean[][] visited, boolean[] frontEnd) 
    {
        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize || visited[x][y] || board[x][y] != color)
        {
            return frontEnd;
        }
        
        if (color == Color.BLACK) 
        {
            if (y == 0)
            {
                frontEnd[0] = true;
            }
            if (y == boardSize - 1)
            {
                frontEnd[1] = true;
            }
        } 
        else 
        {
            if (x == 0)
            {
                frontEnd[0] = true;
            }
            if (x == boardSize - 1)
            {
                frontEnd[1] = true;
            }
        }

        visited[x][y] = true;

        if (color == Color.BLACK && frontEnd[0] && frontEnd[1])
        { 
            this.winner = Color.BLACK;
            return frontEnd;
        }
        if (color == Color.WHITE && frontEnd[0] && frontEnd[1])
        {
            this.winner = Color.WHITE;
            return frontEnd;
        }

        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}
        };

        for (int[] direction : directions) 
        {
            int newX = x + direction[0];
            int newY = y + direction[1];
            frontEnd = dfs(newX, newY, color, visited, frontEnd);
            if (frontEnd[0] && frontEnd[1]) 
            {
                return frontEnd;
            }
        }

        return frontEnd;
    }
}
