
package net.sf.gogui.boardpainter;


/**
 *
 * @author tylerliu
 */
public class GoFieldFactory implements FieldFactory
{
    public Field createField()
    {
        return new Field(new DrawGoProperty());
    }
}
