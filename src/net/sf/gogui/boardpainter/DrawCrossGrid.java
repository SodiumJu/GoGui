
package net.sf.gogui.boardpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author tylerliu
 */
public class DrawCrossGrid extends DrawBasicGrid
{
    public DrawCrossGrid(Color color, int thicknese) 
    {
        super(color, thicknese);
    }
    public void draw(Graphics g, double x, double y, int fieldSize)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(getThicknese())); 
        g2.drawLine((int)(x - fieldSize), (int)y, (int)(x + fieldSize), (int)y);
        g2.drawLine((int)x, (int)(y - fieldSize), (int)x, (int)(y + fieldSize));
    }
}
