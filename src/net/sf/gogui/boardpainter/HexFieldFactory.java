
package net.sf.gogui.boardpainter;

/**
 *
 * @author tylerliu
 */
public class HexFieldFactory implements FieldFactory
{
    public Field createField()
    {
        return new Field(new DrawHexProperty());
    }
}
