
package net.sf.gogui.rule;

import java.util.ArrayList;
import java.util.List;
import net.sf.gogui.go.GoPoint; // YR
import net.sf.gogui.go.GoColor;

/**
 *
 * @author Yan-Ru Ju
 */
public class Othello extends Rule
{   
    // private boolean isOpening;
    private int white_count;
    private int black_count;
    private Color currentColor;
    private int boardSize;
    private Color[][] board;
    private boolean gameFinished;
    private String m_EndMoveoptionalMessage;
    private Color winner;
    private int[][] directions = {
            {0, -1}, {0, 1}, {-1, 0}, {1, 0},
            {-1, -1}, {1, -1}, {-1, 1}, {1,1}
            //{x, y}
            //up, down, left, right, up-left, up-right, down-left, down-right
        };
    
    public Othello(int s) 
    {
        reset(s);
    }

    public String gameName() 
    {
        return "Othello";
    }

    public void reset(int size) 
    {
        // this.isOpening = false;
        this.boardSize = size;
        board = new Color[size][size];
        gameFinished = false;
        this.winner = Color.EMPTY;
        for (int i = 0; i < size; i++) 
        {
            for (int j = 0; j < size; j++) 
            {
                board[i][j] = Color.EMPTY;
            }
        }

        board[3][3] = Color.WHITE;
        board[4][3] = Color.BLACK;
        board[3][4] = Color.BLACK;
        board[4][4] = Color.WHITE;
        
    }

    /*public void setEnabled(boolean b){ // YR
        isOpening = b;
        System.out.println(isOpening);
    }*/

    private List<GoPoint> opening;
    public List<GoPoint> getOpening(){ // YR

        opening = new ArrayList<>();
        GoPoint point;
        point = GoPoint.get(3, 4);
        opening.add(point);

        point = GoPoint.get(4, 4);
        opening.add(point);

        point = GoPoint.get(4, 3);
        opening.add(point);

        point = GoPoint.get(3, 3);
        opening.add(point);

        return opening;    
    }

    public boolean isEnd()
    {
        if (gameFinished) 
        {
            m_EndMoveoptionalMessage = "The game is finished. Black: " + black_count + "and White: "+ white_count + ". " + enum2String(this.winner) + " win!";
            return true;
        }    
        return false;
    }
    
    public String getEndMoveoptionalMessage()
    {
        return m_EndMoveoptionalMessage;
    }
    
    private String enum2String(Color color)
    {
        switch(color)
        {
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            default:
                return "EM";
        }
    }

    private boolean isComplementColor(Color this_color, Color nex_color){
        if(this_color!=Color.EMPTY && nex_color!=Color.EMPTY){
            if(this_color == nex_color)
                return false;
            else
                return true;
        }
        return false;   
    }

    private Color complementColor(Color color){
        if(color==Color.BLACK)
            return Color.WHITE;
        else if(color==Color.WHITE)
            return Color.BLACK;
        else
            return Color.EMPTY;
    }

    public boolean isLegalMove(Point p)
    {   
        if((p.x==3 && p.y==3 && p.color==Color.WHITE) ||
            (p.x==4 && p.y==4 && p.color==Color.WHITE) ||
            (p.x==4 && p.y==3 && p.color==Color.BLACK) ||
            (p.x==3 && p.y==4 && p.color==Color.BLACK))
            return true;

        if(board[p.x][p.y] != Color.EMPTY){
            return false;
        }

        // board[3][3] = Color.WHITE;
        // board[4][3] = Color.BLACK;
        // board[3][4] = Color.BLACK;
        // board[4][4] = Color.WHITE;

        // System.out.println(this.enum2String(p.color));
        // System.out.println("This:"+p.x+","+p.y);
        if (p.x < 0 || p.x >= this.boardSize || p.y < 0 || p.y >= this.boardSize) 
        {
            return false;
        }
               
        for(int i=0; i<8; i++){
            int x_dir = directions[i][0];
            int y_dir = directions[i][1];
            int nex_x = p.x + x_dir;
            int nex_y = p.y + y_dir;

            boolean is_prev_color_complement = false;
            while((nex_x < this.boardSize && nex_x >= 0) && (nex_y < this.boardSize && nex_y >= 0)){
                // System.out.println(nex_x+","+nex_y+"|"+this.enum2String(board[nex_x][nex_y]));
                if(board[nex_x][nex_y] != Color.EMPTY){
                    if(isComplementColor(p.color, board[nex_x][nex_y])){
                    //next pos is complement color
                        // System.out.println("board:"+this.enum2String(board[nex_x][nex_y]));
                        is_prev_color_complement = true;
                    }else{
                    //next pos is same color
                        if(is_prev_color_complement){
                           return true; 
                        }
                        break;
                    }
                }else{ //next pos is EMPTY
                    break;
                }

                nex_x = nex_x + x_dir;
                nex_y = nex_y + y_dir;
                
            }
            // System.out.println("/");
        }
        return false;               
    }
    
    private void checkFlip(GoPoint point){
        Point tmp;
        ArrayList<GoPoint> flipGoPoint = new ArrayList<GoPoint>();
        ArrayList<GoPoint> dirGoPoint = new ArrayList<GoPoint>();
        for(int i=0; i<8; i++){
            int x_dir = directions[i][0];
            int y_dir = directions[i][1];
            int nex_x = point.getX() + x_dir;
            int nex_y = point.getY() + y_dir;

            dirGoPoint.clear();

            boolean is_prev_color_complement = false;
            while((nex_x < this.boardSize && nex_x >= 0) && (nex_y < this.boardSize && nex_y >= 0)){
                // System.out.println(nex_x+","+nex_y+"|"+this.enum2String(board[nex_x][nex_y]));
                if(board[nex_x][nex_y] != Color.EMPTY){
                    if(isComplementColor(currentColor, board[nex_x][nex_y])){
                    //next pos is complement color
                        // System.out.println("board:"+this.enum2String(board[nex_x][nex_y]));
                        dirGoPoint.add(GoPoint.get(nex_x, nex_y));
                        is_prev_color_complement = true;
                    }else{
                    //next pos is same color
                        if(is_prev_color_complement){
                            for (GoPoint element : dirGoPoint){
                                // result.add(RuleUtil.goPointToRulePoint(element, Color2Gocolor(complementColor(currentColor))));
                                tmp = new Point(element.getX(), element.getY(), currentColor);
                                // System.out.println("Flip:"+tmp.color);
                                result.add(tmp);
                                flipGoPoint.add(element);   
                            }
                        }
                        break;
                    }
                }else{ //next pos is EMPTY
                    break;
                }

                nex_x = nex_x + x_dir;
                nex_y = nex_y + y_dir;
                
            }

        }

        for(GoPoint element : flipGoPoint){
            board[element.getX()][element.getY()] = currentColor;
        }
        // 
    }
    
    private void printBoard(){
        for(int y_p = boardSize-1; y_p >= 0; y_p--){
            for(int x_p = 0; x_p < boardSize; x_p++)
                if(board[x_p][y_p] == Color.EMPTY)
                    System.out.print("_____ ");
                else
                    System.out.print(board[x_p][y_p]+" ");
            System.out.print("\n");
        }
        System.out.println();    
    }

    private GoColor Color2Gocolor(Color c)
    {
        GoColor color = GoColor.EMPTY;
        switch (c)
        {
            case EMPTY:
                color = GoColor.EMPTY;
                break;
            case BLACK:
                color = GoColor.BLACK;
                break;
            case WHITE:
                color = GoColor.WHITE;
                break;
        }
        return color;
    }

    private boolean isNextCanPlay(Color color){
        for(int x_p = 0; x_p < boardSize; x_p++){
            for(int y_p = 0; y_p < boardSize; y_p++){
                if(board[x_p][y_p] == Color.EMPTY){
                    Point check_p = new Point(x_p, y_p, color);
                    if(isLegalMove(check_p)){
                        return true;
                    }
                }
                
            }
        }
        return false;    
    }

    private List<Point> result;
    public List<Point> play(Point point) 
    {
        this.result = new ArrayList<>();
        currentColor = point.color;
        GoPoint p = RuleUtil.rulePointToGoPoint(point);
        // System.out.println("Transform:"+p.getX()+","+p.getY());
        checkFlip(p);
        board[p.getX()][p.getY()] = currentColor;
        // printBoard();

        // System.out.println("List:"+this.result.size());
            
        
        

        // board[p.x][p.y] = p.color;
        
        // Color next_color;
        // if(p.color == Color.BLACK){
        //     next_color = Color.WHITE;        
        // }else{
        //     next_color = Color.BLACK;
        // }
       
        // gameFinished = false;

        if(!isNextCanPlay(complementColor(currentColor))){
            if(!isNextCanPlay(currentColor)){
                gameFinished = true;
                this.winner = countBoard();
            }else{
                gameFinished = false;
                System.out.println("U have to PASS (F2)"); 
            }   
        }
        // this.winner = Color.BLACK;
        return result;
    }
    
    private Color countBoard(){
        white_count = 0;
        black_count = 0;

        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(board[i][j] == Color.WHITE){
                    white_count++;
                }else if(board[i][j] == Color.BLACK){
                    black_count++;
                }
            }
        }

        if(white_count > black_count){
            return Color.WHITE;
        }else if(white_count < black_count){
            return Color.BLACK;
        }else{
            return Color.EMPTY;
        }
    }

}
