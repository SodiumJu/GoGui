// BoardPainter.java

package net.sf.gogui.boardpainter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.net.URL;
import static net.sf.gogui.go.GoColor.EMPTY;
import net.sf.gogui.go.GoPoint;
import net.sf.gogui.go.BoardConstants;

/** Draws a board. */
public class BoardPainter
{
    public BoardPainter()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("net/sf/gogui/images/wood.png");
        if (url == null)
            m_image = null;
        else
            m_image = loadImage(url);
        m_boardStatus = new BoardStatus(this);
        m_drawBoard = new DrawGoBoard(m_boardStatus, new double[]{1, 0}, new double[]{0, 1});
        screenRatio = m_drawBoard.getScreenRatio();
    }
    public int[] screenRatio;
    
    public BoardStatus m_boardStatus;
    
    public DrawBoard m_drawBoard;

    /** Draw a board into graphics object.
        @param graphics The graphics object.
        @param field The fields.
        @param width The width/height of the image.
        @param showGrid Show grid coordinates. */
    public void draw(Graphics graphics, ConstField[][] field, int width,
                     boolean showGrid)
    {
        this.screenRatio = m_drawBoard.getScreenRatio();
        if (graphics instanceof Graphics2D)
        {
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
        }
        m_width = width;
        m_size = field.length;
        if (m_constants == null || m_constants.getSize() != m_size)
            m_constants = BoardConstants.get(m_size);
        assert m_size <= GoPoint.MAX_SIZE;
        double borderSize;
        if (showGrid)
            borderSize = BORDER_SIZE;
        else
            borderSize = BORDER_SIZE_NOGRID;
        drawBackground(graphics);
        m_fieldSize = m_drawBoard.getFieldSize(borderSize);
        m_fieldOffset = m_drawBoard.getFieldOffset();
        m_drawBoard.draw(graphics);
        if (showGrid)
            drawGridLabels(graphics);
        drawShadows(graphics, field);
        drawFields(graphics, field);
    }
    
    public Point getCenter(int x, int y)
    {
        Point point = getLocation(x, y);
        point.x += m_fieldSize / 2;
        point.y += m_fieldSize / 2;
        return point;
    }

    public int getFieldSize()
    {
        return m_fieldSize;
    }

    public Point getLocation(int x, int y)
    {
        return m_drawBoard.getLocation(x, y);
    }

    public GoPoint getPoint(Point point)
    {
        return m_drawBoard.getPoint(point);
    }

    /** Get preferred board size given a preferred field size.
        The drawer can draw any board size. The border has a variable size
        to ensure that all fields have exactly the same size (in pixels).
        If a preferred field size is known (e.g. from a different board size,
        or from the last settings), then using the board size returned by this
        function will draw the board such that the field size is exactly the
        preferred one. */
    public Dimension getPreferredSize(int preferredFieldSize,
                                             int boardSize, boolean showGrid)
    {
        this.screenRatio = m_drawBoard.getScreenRatio();
        double borderSize;
        if (showGrid)
            borderSize = BORDER_SIZE * preferredFieldSize;
        else
            borderSize = BORDER_SIZE_NOGRID * preferredFieldSize;
        int preferredSize = (preferredFieldSize * boardSize
                             + 2 * Math.round((float)Math.ceil(borderSize)));
        return new Dimension(preferredSize * screenRatio[0] / screenRatio[1], preferredSize);
    }

    public int getShadowOffset()
    {
        return (m_fieldSize  - 2 * Field.getStoneMargin(m_fieldSize)) / 12;
    }

    /** Preferred border size (in fraction of field size) if grid is drawn. */
    private static final double BORDER_SIZE = 0.6;

    /** Preferred border size (in fraction of field size) if grid is drawn. */
    private static final double BORDER_SIZE_NOGRID = 0.2;

    public int m_fieldSize;

    public int m_fieldOffset;

    public boolean m_flipHorizontal = false;

    public boolean m_flipVertical = false;

    public int m_size;

    public int m_width;

    private static int s_cachedFontFieldSize;

    private static final AlphaComposite COMPOSITE_3
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);

    public BoardConstants m_constants;

    private final Color m_gridLabelColor = new Color(96, 96, 96);

    private final Color m_gridColor = new Color(80, 80, 80);

    private static Font s_cachedFont;

    private final Image m_image;

    private void drawBackground(Graphics graphics)
    {
        if (m_image == null)
        {
            graphics.setColor(new Color(212, 167, 102));
            graphics.fillRect(0, 0, m_width, m_width);
        }
        else
            graphics.drawImage(m_image, 0, 0, m_width, m_width, null);
    }

    private void drawFields(Graphics graphics, ConstField field[][])
    {
        assert field.length == m_size;
        for (int x = 0; x < m_size; ++x)
        {
            assert field[x].length == m_size;
            for (int y = 0; y < m_size; ++y)
            {
                Point location = getLocation(x, y);
                field[x][y].draw(graphics, m_fieldSize, location.x,
                                 location.y, m_image, m_width);
            }
        }
    }

    private void drawGrid(Graphics graphics)
    {
        if (m_fieldSize < 2)
            return;
        graphics.setColor(Color.darkGray);
        if (graphics instanceof Graphics2D)
        {
            // Temporarily disable antialiasing, which causes lines to
            // appear too thick with OpenJDK (version 6b09)
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        for (int y = 0; y < m_size; ++y)
        {
            if (y == 0 || y == m_size - 1)
                graphics.setColor(Color.black);
            else
                graphics.setColor(m_gridColor);
            Point left = getCenter(0, y);
            Point right = getCenter(m_size - 1, y);
            graphics.drawLine(left.x, left.y, right.x, right.y);
        }
        for (int x = 0; x < m_size; ++x)
        {
            if (x == 0 || x == m_size - 1)
                graphics.setColor(Color.black);
            else
                graphics.setColor(m_gridColor);
            Point top = getCenter(x, 0);
            Point bottom = getCenter(x, m_size - 1);
            graphics.drawLine(top.x, top.y, bottom.x, bottom.y);
        }
        if (graphics instanceof Graphics2D)
        {
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
        }
        int r;
        if (m_fieldSize <= 7)
            return;
        else if (m_fieldSize <= 33)
            r = 1;
        else if (m_fieldSize <= 60)
            r = 2;
        else
            r = 3;
        for (int x = 0; x < m_size; ++x)
            if (m_constants.isHandicapLine(x))
                for (int y = 0; y < m_size; ++y)
                    if (m_constants.isHandicapLine(y))
                    {
                        Point point = getCenter(x, y);
                        graphics.fillOval(point.x - r, point.y - r,
                                          2 * r + 1, 2 * r + 1);
                    }
    }

    private void drawGridLabels(Graphics graphics)
    {
        if (m_fieldSize < 15)
            return;
        graphics.setColor(m_gridLabelColor);
        setFont(graphics, m_fieldSize);
        int offset = (m_fieldSize + m_fieldOffset) / 2;
        Point point;
        char c = 'A';
        for (int x = 0; x < m_size; ++x)
        {
            String string = Character.toString(c);
            point = getLocation(x, 0);
            if (m_flipHorizontal)
                point.y -= offset;
            else
                point.y += offset;
            drawLabel(graphics, point, string);
            point = getLocation(x, m_size - 1);
            if (m_flipHorizontal)
                point.y += offset;
            else
                point.y -= offset;
            drawLabel(graphics, point, string);
            ++c;
            if (c == 'I')
                ++c;
        }
        for (int y = 0; y < m_size; ++y)
        {
            String string = Integer.toString(y + 1);
            point = getLocation(0, y);
            if (m_flipVertical)
                point.x += offset;
            else
                point.x -= offset;
            drawLabel(graphics, point, string);
            point = getLocation(m_size - 1, y);
            if (m_flipVertical)
                point.x -= offset;
            else
                point.x += offset;
            drawLabel(graphics, point, string);
        }
    }

    private void drawShadows(Graphics graphics, ConstField[][] field)
    {
        if (m_fieldSize <= 5)
            return;
        Graphics2D graphics2D =
            graphics instanceof Graphics2D ? (Graphics2D)graphics : null;
        if (graphics2D == null)
            return;
        graphics2D.setComposite(COMPOSITE_3);
        int size = m_fieldSize - 2 * Field.getStoneMargin(m_fieldSize);
        int offsetX = getShadowOffset() / 2; // Relates to stone gradient
        int offsetY = getShadowOffset();
        for (int x = 0; x < m_size; ++x)
            for (int y = 0; y < m_size; ++y)
            {
                if (field[x][y].getColor() == EMPTY)
                    continue;
                Point location = getCenter(x, y);
                graphics.setColor(Color.black);
                graphics.fillOval(location.x - size / 2 + offsetX,
                                  location.y - size / 2 + offsetY,
                                  size, size);
            }
        graphics.setPaintMode();
    }

    private void drawLabel(Graphics graphics, Point location,
                           String string)
    {
        FontMetrics metrics = graphics.getFontMetrics();
        int stringWidth = metrics.stringWidth(string);
        int stringHeight = metrics.getAscent();
        int x = Math.max((m_fieldSize - stringWidth) / 2, 0);
        int y = stringHeight + (m_fieldSize - stringHeight) / 2;
        graphics.drawString(string, location.x + x, location.y + y);
    }

    private static Image loadImage(URL url)
    {
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        try
        {
            mediaTracker.waitForID(0);
        }
        catch (InterruptedException e)
        {
            return null;
        }
        return image;
    }

    private static void setFont(Graphics graphics, int fieldSize)
    {
        if (s_cachedFont != null && s_cachedFontFieldSize == fieldSize)
        {
            graphics.setFont(s_cachedFont);
            return;
        }
        int fontSize;
        if (fieldSize < 29)
            fontSize = (int)(0.33 * fieldSize);
        else if (fieldSize < 40)
            fontSize = 10;
        else
            fontSize = (int)(10 + 0.1 * (fieldSize - 40));
        s_cachedFont = new Font("SansSerif", Font.PLAIN, fontSize);
        s_cachedFontFieldSize = fieldSize;
        graphics.setFont(s_cachedFont);
    }

    public void setOrientation(boolean flipHorizontal, boolean flipVertical) {
        m_flipHorizontal = flipHorizontal;
        m_flipVertical = flipVertical;
    }
}
