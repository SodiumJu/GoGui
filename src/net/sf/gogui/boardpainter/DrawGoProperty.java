
package net.sf.gogui.boardpainter;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author tylerliu
 */
public class DrawGoProperty extends DrawProperty
{
    public void fillBackGround(Graphics graphics, int width, int height, Color color)
    {
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
    }
}
