
package net.sf.gogui.boardpainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author tylerliu
 */
public class DrawGoBoard extends DrawMeshBoard
{
    public DrawGoBoard(BoardStatus boardStatus, double[] v1, double[] v2)
    {
        super(boardStatus, v1, v2);
        m_grid = new DrawCrossGrid(Color.BLACK, 1);
        m_square = new DrawSquareGrid(Color.BLACK, 1);
    }
    
    private DrawCrossGrid m_grid;
    private DrawSquareGrid m_square;
    
    public int[] getScreenRatio()
    {
        return new int[]{1, 1};
    }
    
    public void draw(Graphics graphics)
    {     
        if (m_boardStatus.getFieldSize() < 2) 
        {
            return;
        }
        for (int y = 1 ; y < m_boardStatus.getSize() - 1; ++y) 
        {
            for (int x = 1; x < m_boardStatus.getSize() - 1; ++x) 
            {
                Point point = m_boardStatus.getCenter(x, y);
                int xPos = point.x;
                int yPos = point.y;
                
                m_grid.draw(graphics, xPos, yPos, m_boardStatus.getFieldSize());
                
            }
        } 
        Point point =m_boardStatus.getCenter(m_boardStatus.getSize() / 2, m_boardStatus.getSize() / 2);
        if(m_boardStatus.getSize() % 2 == 0){
            point.x -= m_boardStatus.getFieldSize() / 2;
            point.y += m_boardStatus.getFieldSize() / 2;
        }
        
        int xPos = point.x;
        int yPos = point.y;
        m_square.draw(graphics, xPos, yPos, m_boardStatus.getFieldSize() * (m_boardStatus.getSize() - 1));
        int r;
            if (m_boardStatus.getFieldSize() <= 7)
                return;
            else if (m_boardStatus.getFieldSize() <= 33)
                r = 1;
            else if (m_boardStatus.getFieldSize() <= 60)
                r = 2;
            else
                r = 3;
            for (int x = 0; x < m_boardStatus.getSize(); ++x)
                if (m_boardStatus.getConstants().isHandicapLine(x))
                    for (int y = 0; y < m_boardStatus.getSize(); ++y)
                        if (m_boardStatus.getConstants().isHandicapLine(y))
                        {
                            point = m_boardStatus.getCenter(x, y);
                            graphics.fillOval(point.x - r, point.y - r, 2 * r + 1, 2 * r + 1);
                        }
    }
}
