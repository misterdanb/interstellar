/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.ui;

import java.awt.*;
import javax.swing.*;

import interstellar.system.*;

/**
 * @author db1992
 */

public class RamViewer extends JFrame
{
    private HexView m_hexView = new HexView();
    private Console m_emuConsole = null;

    public RamViewer(Dimension pos)
    {
        super("interSTELLAr - Debugger: RamViewer");
        setSize(0, 0);
        setLocation((int) pos.getWidth(), (int) pos.getHeight());

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        setupComponents();

        toFront();
        setVisible(false);
    }

    private void setupComponents()
    {
        int[] iArray = new int[128];
        for (int i = 0; i < iArray.length; i++) iArray[i] = 0;

        m_hexView.setData(iArray);
        setSize((int) m_hexView.getMinimumSize().getWidth(), (int) m_hexView.getMinimumSize().getHeight());
        
        add(m_hexView, BorderLayout.CENTER);
    }

    public void setConsole(Console console)
    {
        m_emuConsole = console;
    }

    public void updateRamInformation()
    {
        m_hexView.setData(m_emuConsole.getRIOT().getRam());
        setSize((int) m_hexView.getMinimumSize().getWidth(), (int) m_hexView.getMinimumSize().getHeight());
    }
}
