/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

/*
 * You draw on the Graphics of m_tubeImage,
 * so this class will draw it as GUI in the
 * overridden paintComponent-Method.
 * This is necessary because otherwise the
 * graphics will disappear imemdiatly.
 */

package interstellar.ui;

import interstellar.system.Console;
import interstellar.system.tia.video.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.awt.Toolkit;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Display extends JPanel implements ActionListener, VideoAdapter
{
    private BufferedImage m_tubeImage;
    private Image m_testPatternImage;
    private Timer m_emuTimer = new Timer(17, this);

    private boolean mTVSignal = false;

    private Console m_emuConsole = null;

    public Display()
    {
        m_tubeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        m_testPatternImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/testpattern.png"));

        updateDisplayGraphics();
    }

    public Graphics2D getDisplayGraphics()
    {
        return (Graphics2D) m_tubeImage.getGraphics();
    }

    public void updateDisplayGraphics()
    {
        updateUI();
    }

    public void updateDisplayResolution()
    {
        m_tubeImage = new BufferedImage(m_emuConsole.getTIA().getVideo().getLiveFrame().getWidth(),
                                        m_emuConsole.getTIA().getVideo().getLiveFrame().getHeight(),
                                        BufferedImage.TYPE_INT_RGB);
    }

    // method that is triggered when frame finished
    public void draw(Frame frame)
    {
        int[] iBuffer = frame.getData();
        m_tubeImage.flush();
        int[][] bankdata = ((DataBufferInt) (m_tubeImage.getRaster().getDataBuffer())).getBankData();
        System.arraycopy(iBuffer, 0, bankdata[0], 0, iBuffer.length);

        updateDisplayGraphics();
    }

    public void setConsole(Console console)
    {
        m_emuConsole = console;
        m_emuConsole.getTIA().getVideo().registerAdapter(this);

        updateDisplayResolution();
        updateDisplayGraphics();
    }

    public void setSignal(boolean value)
    {
        mTVSignal = value;
    }

    public boolean getSignal()
    {
        return mTVSignal;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (!mTVSignal)
        {
            g.drawImage(m_testPatternImage, 0, 0, getWidth(), getHeight(), this);
        }
        else
        {
            g.drawImage(Toolkit.getDefaultToolkit().createImage(m_tubeImage.getSource()), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void start(){
        m_emuTimer.start();
    }

    public void stop(){
        m_emuTimer.stop();
    }

    public boolean isRunning(){
        return m_emuTimer.isRunning();
    }

    public void actionPerformed(ActionEvent e){
        if (mTVSignal){
            m_emuConsole.updateFrame();
        }

        updateDisplayGraphics();
    }
}
