
package net.sf.gogui.boardpainter;

import java.awt.Graphics;

/**
 *
 * @author tylerliu
 */
public abstract class DrawGrid
{
    public abstract void draw(Graphics g, double x, double y, int fieldSize);
}
