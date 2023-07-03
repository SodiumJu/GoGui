package net.sf.gogui.boardpainter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Yan-Ru Ju
 */ 
public class DrawOthelloGrid extends DrawBasicGrid
{
    public DrawOthelloGrid(Color color, int thicknese) 
    {
        super(color, thicknese);
    }

    public void draw(Graphics g, double x, double y, int fieldSize) 
    {
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        int size = (int) (fieldSize/2);

        xPoints[0] = (int) (x+size);
        yPoints[0] = (int) (y+size);

        xPoints[1] = (int) (x-size);
        yPoints[1] = (int) (y+size);

        xPoints[2] = (int) (x-size);
        yPoints[2] = (int) (y-size);

        xPoints[3] = (int) (x+size);
        yPoints[3] = (int) (y-size);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(getThicknese())); 
        g2.drawPolygon(xPoints, yPoints, 4);
    }
}