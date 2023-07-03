
package net.sf.gogui.boardpainter;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author tylerliu
 */
public class DrawHexProperty extends DrawProperty
{
    public void fillBackGround(Graphics graphics, int width, int height, Color color)
    {
        int x = width / 2;
        int y = height / 2;
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        int xsize = (int)(width / Math.sqrt(3));
        int ysize = (int)(width / Math.sqrt(3));
        for (int i = 0; i < 6; i++) 
        {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            xPoints[i] = (int) (x + xsize * Math.cos(angle));
            yPoints[i] = (int) (y + ysize * Math.sin(angle));
        }
        graphics.setColor(color);
        graphics.fillPolygon(xPoints, yPoints, 6);
    }
    
    public double getHeightRatio()
    {
        return 2 * Math.sqrt(3) / 3;
    }
}
