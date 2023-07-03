
package net.sf.gogui.boardpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import net.sf.gogui.go.GoPoint;

/**
 *
 * @author tylerliu
 */
public class DrawHexBoard extends DrawMeshBoard
{
    public DrawHexBoard(BoardStatus boardStatus, double[] v1, double[] v2)
    {
        super(boardStatus, v1, v2);
        m_grid = new DrawHexagonGrid(Color.BLACK, 1);
    }
    
    private DrawHexagonGrid m_grid;
    
    public int[] getScreenRatio()
    {
        return new int[]{70, 45};
    }
    
    public void draw(Graphics graphics)
    {      
        if (m_boardStatus.getFieldSize() < 2) 
        {
            return;
        }
        for (int y = 0 ; y < m_boardStatus.getSize(); ++y) 
        {
            for (int x = 0; x < m_boardStatus.getSize(); ++x) 
            {
                Point point = m_boardStatus.getCenter(x, y);
                int xPos = point.x;
                int yPos = point.y;
                
                m_grid.draw(graphics, xPos, yPos, m_boardStatus.getFieldSize());
                
            }
        }
        drawHexEdge(graphics);        
    }
    
    public int getFieldSize(double borderSize)
    {
        return Math.round((float)Math.floor(m_boardStatus.getWidth() / (m_boardStatus.getSize() * 3 / 2 + 2 * borderSize)));
    }
    
    public int getFieldOffset()
    {
        return (m_boardStatus.getWidth() - (m_boardStatus.getSize() * m_boardStatus.getFieldSize() + (m_boardStatus.getSize() - 1) * m_boardStatus.getFieldSize() / 2)) / 2;
    }
    
    private void drawHexEdge(Graphics graphics)
    {
        // draw top edge
        graphics.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setStroke(new BasicStroke(5)); 
        int pointSize = (m_boardStatus.getSize() - 1) * 3 + 3;
        int[] xPoints = new int[pointSize];
        int[] yPoints = new int[pointSize];
        int index = 0;
        Point point;
        double angle;
        int rt_x = 0;
        int rt_y = 0;
        point = m_boardStatus.getCenter(m_boardStatus.getSize() - 1, m_boardStatus.getSize() - 1);
        for (int i = 0; i < 2; i++) 
        {
            angle = 2 * Math.PI / 6 * ((i + 4)+ 0.5);
            rt_x += (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
            rt_y += (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        }
        rt_x = rt_x / 2;
        rt_y = rt_y / 2;
        for (int j = 0; j < m_boardStatus.getSize() - 1; j++)
        {
            point = m_boardStatus.getCenter(j, (m_boardStatus.getSize() - 1));
            for (int i = 0; i < 3; i++) 
            {
                angle = 2 * Math.PI / 6 * (i + 3 + 0.5);
                xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
                yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
            }
        }
        point = m_boardStatus.getCenter(m_boardStatus.getSize() - 1, (m_boardStatus.getSize() - 1));
        angle = 2 * Math.PI / 6 * 3.5;
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        angle = 2 * Math.PI / 6 * 4.5;
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        xPoints[index] = rt_x;
        yPoints[index++] = rt_y;
        
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
        //  draw the lower edge
        xPoints = new int[pointSize];
        yPoints = new int[pointSize];
        index = 0;
        int ld_x = 0;
        int ld_y = 0;
        point = m_boardStatus.getCenter(0, 0);
        for (int i = 0; i < 2; i++) 
        {
            angle = 2 * Math.PI / 6 * ((i + 1) + 0.5);
            ld_x += (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
            ld_y += (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        }
        ld_x = ld_x / 2;
        ld_y = ld_y / 2;
        xPoints[index] = ld_x;
        yPoints[index++] = ld_y;
        angle = 2 * Math.PI / 6 * (1 + 0.5);
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        angle = 2 * Math.PI / 6 * 0.5;
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        for (int j = 1; j < m_boardStatus.getSize(); j++)
        {
            point = m_boardStatus.getCenter(j, 0);
            for (int i = 2; i >= 0; i--) 
            {
                angle = 2 * Math.PI / 6 * (i + 0.5);
                xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
                yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
            }
        }
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
        
        //  draw left edge
        graphics.setColor(Color.WHITE);
        xPoints = new int[pointSize];
        yPoints = new int[pointSize];
        index = 0;
        for (int j = m_boardStatus.getSize() - 1; j > 0; j--)
        {
            point = m_boardStatus.getCenter(0, j);
            for (int i = 3; i >= 1; i--) 
            {
                angle = 2 * Math.PI / 6 * (i + 0.5);
                xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
                yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
            }
        }
        point = m_boardStatus.getCenter(0, 0);
            for (int i = 3; i >= 2; i--) 
            {
                angle = 2 * Math.PI / 6 * (i + 0.5);
                xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
                yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
            }
        xPoints[index] = ld_x;
        yPoints[index++] = ld_y;
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);

        //  draw right edge
        xPoints = new int[pointSize];
        yPoints = new int[pointSize];
        index = 0;
        xPoints[index] = rt_x;
        yPoints[index++] = rt_y;
        point = m_boardStatus.getCenter(m_boardStatus.getSize() - 1, m_boardStatus.getSize() - 1);
        angle = 2 * Math.PI / 6 * (5 + 0.5);
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        angle = 2 * Math.PI / 6 * (0 + 0.5);
        xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
        yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
        for (int j = m_boardStatus.getSize() - 2; j >= 0; j--)
        {
            point = m_boardStatus.getCenter(m_boardStatus.getSize() - 1, j);
            for (int i = 0; i < 3; i++) 
            {
                angle = 2 * Math.PI / 6 * ((i + 4) % 6 + 0.5);
                xPoints[index] = (int) (point.x + m_boardStatus.getFieldSize() * Math.cos(angle) / Math.sqrt(3));
                yPoints[index++] = (int) (point.y + m_boardStatus.getFieldSize() * Math.sin(angle) / Math.sqrt(3));
            }
        }
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
    }
}