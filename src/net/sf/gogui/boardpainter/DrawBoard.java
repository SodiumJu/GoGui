
package net.sf.gogui.boardpainter;

import java.awt.Graphics;
import java.awt.Point;
import net.sf.gogui.go.GoPoint;

/**
 *
 * @author tylerliu
 */
public abstract class DrawBoard
{
    public DrawBoard(BoardStatus boardStatus)
    {
        m_boardStatus = boardStatus;
    }
    
    public BoardStatus m_boardStatus;
    
    public abstract void draw(Graphics graphics);
    
    public abstract GoPoint getPoint(Point point);
    
    public abstract Point getLocation(int x, int y);
    
    public int[] getScreenRatio()
    {
        return new int[]{1, 1};
    }
    
    public int getFieldSize(double borderSize)
    {
        return Math.round((float)Math.floor(m_boardStatus.getWidth() / (m_boardStatus.getSize() + 2 * borderSize)));
    }
    
    public int getFieldOffset()
    {
        return (m_boardStatus.getWidth() - m_boardStatus.getSize() * m_boardStatus.getFieldSize()) / 2;
    }
}
