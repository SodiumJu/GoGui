package net.sf.gogui.boardpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author tylerliu
 */ 
public class DrawHexagonGrid extends DrawBasicGrid
{
    public DrawHexagonGrid(Color color, int thicknese) 
    {
        super(color, thicknese);
    }

    public void draw(Graphics g, double x, double y, int fieldSize) 
    {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        
        int size = (int)(fieldSize/ Math.sqrt(3));

        for (int i = 0; i < 6; i++) 
        {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            xPoints[i] = (int) (x + size * Math.cos(angle));
            yPoints[i] = (int) (y + size * Math.sin(angle));
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(getThicknese())); 
        g2.drawPolygon(xPoints, yPoints, 6);
    }
}