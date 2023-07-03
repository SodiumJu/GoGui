package net.sf.gogui.boardpainter;

import java.awt.Color;

/**
 *
 * @author tylerliu
 */
public abstract class DrawBasicGrid extends DrawGrid
{
    private Color m_color;
    private int m_thicknese;

    public DrawBasicGrid(Color color, int thicknese)
    {
        m_color = color;
        m_thicknese = thicknese;
    }

    public Color getColor()
    {
        return m_color;
    }

    public void setColor(Color color)
    {
        m_color = color;
    }

    public int getThicknese()
    {
        return m_thicknese;
    }

    public void setThicknese(int thicknese)
    {
        m_thicknese = thicknese;
    }
    
}