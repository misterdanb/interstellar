/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.ui;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * @author db1992
 */

public class HexView extends JComponent implements MouseListener, KeyListener
{
    private static Font s_hexFont = new Font("Monospaced", 0, 14);
    private static Color s_fontColor = Color.WHITE;
    private static Color s_backColor = Color.BLACK;
    
    private static int s_iMarginLeft = 8;
    private static int s_iMarginRight = 8;
    private static int s_iMarginTop = 8;
    private static int s_iMarginBottom = 8;

    private static int s_iRowNumberWidth = 64;
    private static int s_iColumnNumberHeight = 24;

    private static int s_iTextTopOffset = 14;

    private static int s_iByteRectWidth = 16;
    private static int s_iByteRectHeight = 18;

    private static int s_iByteRectHorizontalSpacing = 6;

    private static int s_iDigitWidth = 8;

    private int[] m_hexData = new int[128];
    private int mMarkedByte = 0;

    public HexView()
    {
        addMouseListener(this);
        addKeyListener(this);

        repaint();
    }

    public HexView(int[] hexData)
    {
        m_hexData = hexData;

        addMouseListener(this);
        addKeyListener(this);
        
        repaint();
    }

    public void setData(int[] hexData) { m_hexData = hexData; repaint(); }
    public int[] getData() { return m_hexData; }

    private int getTotalMarginLeft() { return s_iMarginLeft + s_iRowNumberWidth; }
    private int getTotalMarginTop() { return s_iMarginTop + s_iColumnNumberHeight; }

    @Override
    public void paintComponent(Graphics g)
    {
        g.clearRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());
        g.setColor(s_backColor);
        g.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());

        // paint the row numbers
        for (int i = 0; i < m_hexData.length / (int) 16; i++)
        {
            int xRow = s_iMarginLeft;
            int yRow = getTotalMarginTop() + i * s_iByteRectHeight;

            String sDigitOne = Integer.toHexString(((i * 16) >>> 12) & 0x0F).toUpperCase();
            String sDigitTwo = Integer.toHexString(((i * 16) >>> 8) & 0x0F).toUpperCase();
            String sDigitThree = Integer.toHexString(((i * 16) >>> 4) & 0x0F).toUpperCase();
            String sDigitFour = Integer.toHexString((i * 16) & 0x0F).toUpperCase();

            g.setColor(s_fontColor);
            g.setFont(s_hexFont);

            g.drawString("0x", xRow, yRow + s_iTextTopOffset);
            g.drawString(sDigitOne, xRow + s_iDigitWidth * 2, yRow + s_iTextTopOffset);
            g.drawString(sDigitTwo, xRow + s_iDigitWidth * 3, yRow + s_iTextTopOffset);
            g.drawString(sDigitThree, xRow + s_iDigitWidth * 4, yRow + s_iTextTopOffset);
            g.drawString(sDigitFour, xRow + s_iDigitWidth * 5, yRow + s_iTextTopOffset);
        }

        // paint the column numbers
        for (int i = 0; i < 16; i++)
        {
            int xColumn = getTotalMarginLeft() + i * (s_iByteRectHorizontalSpacing + s_iByteRectWidth);
            int yColumn = s_iMarginTop;

            String sDigitOne = Integer.toHexString((i >>> 4) & 0x0F).toUpperCase();
            String sDigitTwo = Integer.toHexString(i & 0x0F).toUpperCase();

            g.drawString(sDigitOne, xColumn, yColumn + s_iTextTopOffset);
            g.drawString(sDigitTwo, xColumn + s_iDigitWidth, yColumn + s_iTextTopOffset);
        }

        // paint the hex data
        for (int i = 0; i < m_hexData.length; i++)
        {
            int x = i % (int) 16;
            int y = i / (int) 16;

            int xRect = getTotalMarginLeft() + x * (s_iByteRectHorizontalSpacing + s_iByteRectWidth);
            int yRect = getTotalMarginTop() + y * s_iByteRectHeight;

            String sDigitOne = Integer.toHexString((m_hexData[i] >>> 4) & 0x0F).toUpperCase();
            String sDigitTwo = Integer.toHexString(m_hexData[i] & 0x0F).toUpperCase();

            if (i == mMarkedByte)
            {
                g.setColor(Color.BLUE);
                g.fillRect(xRect, yRect, s_iByteRectWidth, s_iByteRectHeight);
                g.setColor(Color.YELLOW);
            }
            else
            {
                g.setColor(s_fontColor);
            }

            g.setFont(s_hexFont);
            g.drawString(sDigitOne, xRect, yRect + s_iTextTopOffset);
            g.drawString(sDigitTwo, xRect + s_iDigitWidth, yRect + s_iTextTopOffset);
        }
    }

    private void markByte(int xMouse, int yMouse)
    {
        for (int i = 0; i < m_hexData.length; i++)
        {
            int x = i % (int) 16;
            int y = i / (int) 16;

            int xRect = getTotalMarginLeft() + x * s_iByteRectHorizontalSpacing + x * s_iByteRectWidth;
            int yRect = getTotalMarginTop() + y * s_iByteRectHeight;

            if (xMouse >= xRect && xMouse < xRect + s_iByteRectWidth &&
                yMouse >= yRect && yMouse < yRect + s_iByteRectHeight) mMarkedByte = i;
        }

        repaint();
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        //markByte(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e)
    {
        markByte(e.getX(), e.getY());
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) mMarkedByte -= 1;
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) mMarkedByte += 1;
        else if(e.getKeyCode() == KeyEvent.VK_UP) mMarkedByte -= 16;
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) mMarkedByte += 16;

        if (mMarkedByte < 0) mMarkedByte = 0;
        else if (mMarkedByte >= m_hexData.length) mMarkedByte = m_hexData.length - 1;

        repaint();
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public boolean isFocusTraversable()
    {
        return true;
    }

    @Override
    public Dimension getMinimumSize()
    {
        return new Dimension(getTotalMarginLeft() + 16 * (s_iByteRectWidth + s_iByteRectHorizontalSpacing) + s_iMarginRight,
                             getTotalMarginTop() + (m_hexData.length / (int) 16) * s_iByteRectHeight + s_iMarginBottom + 28);
    }
}