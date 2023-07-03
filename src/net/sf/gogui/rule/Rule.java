package net.sf.gogui.rule;

import java.util.List;
import net.sf.gogui.go.GoPoint; // YR

public abstract class Rule {

    public static enum Color {
        BLACK,
        WHITE,
        EMPTY
    }

    public static class Point{
        public int x;
        public int y;
        public Color color;
        
        public Point(int x, int y, Color color){
            this.x=x;
            this.y=y;
            this.color=color;
        }
    }

    public abstract String gameName();

    public abstract void reset(int boardsize);

    public abstract boolean isEnd();

    public abstract boolean isLegalMove(Point p);

    public abstract List<Point> play(Point p);

    //public abstract BlackWhiteSet<Integer> getcaptured();

    public void setEnabled(boolean b){ // YR

    }

    private List<GoPoint> opening;
    public List<GoPoint> getOpening(){ // YR
        return opening;    
    } 

    public String getEndMoveMainMessage() {
        return "Game finished";
    }

    public String getEndMoveoptionalMessage() {
        return "The game is finished.";
    }

    public String getIllegalMoveMainMessage() {
        return "Play illegal move";
    }

    public String getIllegalMoveoptionalMessage() {
        return "This move violates the rule.";
    }

    public String getIllegalMoveDestructiveOption() {
        return "Play illegal";
    }
}
