
package net.sf.gogui.boardpainter;

import java.awt.Point;
import net.sf.gogui.go.BoardConstants;

/**
 *
 * @author tylerliu
 */
public class BoardStatus 
{
    public BoardStatus(BoardPainter boardpainter)
    {
        m_painter = boardpainter;
    }
    
    public int getFieldSize()
    {
        return m_painter.m_fieldSize;
    }
    
    public int getFieldOffset()
    {
        return m_painter.m_fieldOffset;
    }
    
    public boolean getFlipVertical()
    {
        return m_painter.m_flipVertical;
    }
    
    public boolean getFlipHorizontal()
    {
        return m_painter.m_flipHorizontal;
    }
    
    public int getSize()
    {
        return m_painter.m_size;
    }
    
    public int getWidth()
    {
        return m_painter.m_width;
    }
    
    public Point getCenter(int x, int y)
    {
        return m_painter.getCenter(x, y);
    }
    
    public BoardConstants getConstants()
    {
        return m_painter.m_constants;
    }
    
    private BoardPainter m_painter;
}
