
package net.sf.gogui.boardpainter;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author tylerliu
 */
public abstract class DrawProperty
{
    public abstract void fillBackGround(Graphics graphics, int width, int height, Color color);
    
    public double getHeightRatio()
    {
        return 1.0;
    }
    
    public double getWidthRatio()
    {
        return 1.0;
    }
}
