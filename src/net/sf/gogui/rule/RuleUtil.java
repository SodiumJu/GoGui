/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.sf.gogui.rule;

import net.sf.gogui.go.GoColor;
import net.sf.gogui.go.GoPoint;
import net.sf.gogui.rule.Rule.*;

/**
 *
 * @author tylerliu
 */
public final class RuleUtil {

    public static Point goPointToRulePoint(GoPoint p, GoColor c) {
        Color color = c == GoColor.BLACK ? Color.BLACK : c == GoColor.WHITE ? Color.WHITE : Color.EMPTY;
        Point point = new Point(p.getX(), p.getY(), color);
        return point;
    }

    public static GoPoint rulePointToGoPoint(Point p) {
        return GoPoint.get(p.x, p.y);
    }
    
    public static GoColor rulePointToGoColor(Point p) {
        return p.color == Color.BLACK ? GoColor.BLACK : p.color == Color.WHITE ? GoColor.WHITE : GoColor.EMPTY;
    }
}
