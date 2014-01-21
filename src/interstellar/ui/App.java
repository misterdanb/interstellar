/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

package interstellar.ui;

import java.io.*;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;

import javax.swing.filechooser.FileFilter;
import javax.swing.*;

import interstellar.system.Console;
import interstellar.system.tia.*;
import static interstellar.system.tia.constants.Video.*;

public class App extends JFrame
{
    // <editor-fold defaultstate="collapsed" desc="members">
    // <editor-fold defaultstate="collapsed" desc="members: window">
    private static final String mTitle = "interSTELLAr - Atari 2600 Emulator";
    private static final Dimension m_winSize = new Dimension(640, 600);
    private final Image mplashImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/splash.png"));
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: console">
    private Console m_emuConsole = null;

    private File m_rom = null;
    private File mtate = null;

    private boolean mRunningGameHasSaveState = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components">
    // <editor-fold defaultstate="collapsed" desc="members: components: menu">
    private JMenuBar m_uiMenuBar = new JMenuBar();

    private JMenu m_uiFile = new JMenu("File");
    private JMenu m_uiConsole = new JMenu("Console");
    private JMenu m_uiOptions = new JMenu("Options");
    private JMenu m_uiDebug = new JMenu("Debug");
    private JMenu m_uiTestOperations = new JMenu("Test operations");

    private JMenuItem m_uiLoadRom = new JMenuItem("Load rom");
    private JMenuItem m_uiSaveState = new JMenuItem("Save state");
    private JMenuItem m_uiSaveStateAs = new JMenuItem("Save state as ...");
    private JMenuItem m_uiLoadState = new JMenuItem("Load state");
    private JMenuItem m_uiAbout = new JMenuItem("About");
    private JMenuItem m_uiQuit = new JMenuItem("Quit");

    private JMenuItem m_uiReset = new JMenuItem("Reset");
    private JMenuItem m_uiSelect = new JMenuItem("Select");
    private JMenu m_uiPlayerOne = new JMenu("Player one");
    private JCheckBoxMenuItem m_uiPlayerOneAmateur = new JCheckBoxMenuItem("Amateur", true);
    private JCheckBoxMenuItem m_uiPlayerOneProfessional = new JCheckBoxMenuItem("Professional");
    private JMenu m_uiPlayerTwo = new JMenu("Player two");
    private JCheckBoxMenuItem m_uiPlayerTwoAmateur = new JCheckBoxMenuItem("Amateur", true);
    private JCheckBoxMenuItem m_uiPlayerTwoProfessional = new JCheckBoxMenuItem("Professional");
    private JMenu m_uiTelevisionMode = new JMenu("Television mode");
    private JCheckBoxMenuItem m_uiColor = new JCheckBoxMenuItem("Color", true);
    private JCheckBoxMenuItem m_uiBW = new JCheckBoxMenuItem("B/W");

    private JMenuItem m_uiControls = new JMenuItem("Controls");

    private JMenuItem m_uiShowDisassembly = new JMenuItem("Open Debugger");

    private JMenuItem m_uiOperation0 = new JMenuItem("Draw some lines and set TV-Signal true");
    private JMenuItem m_uiOperation1 = new JMenuItem("NEXT");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components: toolbar">
    private JToolBar m_uiToolBar = new JToolBar("Tools");
    private JButton m_uiToolsLoad = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/open.png"))));
    private JButton m_uiToolsSave = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/save.png"))));

    private ImageIcon m_uiPlayIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/play.png")));
    private ImageIcon m_uiPauseIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/pause.png")));
    private JButton m_uiToolsPlayPause = new JButton(m_uiPlayIcon);

    private JButton m_uiToolsStop = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/stop.png"))));
    private JButton m_uiToolsDisAssembler = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/asm.png"))));
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components: dialogs">
    private JFileChooser m_romFileDialog = new JFileChooser();
    private JFileChooser mtateFileDialog = new JFileChooser();

    private static FileFilter m_romFileFilter = new FileFilter()
    {
        public boolean accept(File aFile)
        {
            if (aFile.isDirectory()) return true;
            return aFile.getName().toLowerCase().endsWith(".bin");
        }

        public String getDescription () { return "Atari 2600 ROMs (*.BIN)"; }
    };

    private static FileFilter mtateFileFilter = new FileFilter()
    {
        public boolean accept(File aFile)
        {
            if (aFile.isDirectory()) return true;
            return aFile.getName().toLowerCase().endsWith(".sav");
        }

        public String getDescription () { return "interSTELLAr Gamestates (*.SAV)"; }
    };
    // </editor-fold>

    // Display Panel
    private Display m_uiDisplay = new Display();

    // Debugger Window
    private Debugger m_uiDebugger = new Debugger(new Dimension(0, 0), m_winSize);
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods">
    // <editor-fold defaultstate="collapsed" desc="methods: constructor">
    public App()
    {
        super(mTitle);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int) m_winSize.getWidth(), (int) m_winSize.getHeight());
        setLocation((int) (screenSize.getWidth() / 2 - m_winSize.getWidth() / 2),
                    (int) (screenSize.getHeight() / 2 - m_winSize.getHeight() / 2));

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupSplashFrame();
        setupEmulation();

        toFront();
        setVisible(true);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: setup">
    private void setupSplashFrame()
    {
        final SplashScreen splash = SplashScreen.getSplashScreen();

        if (splash == null)
        {
            setupComponents();
            return;
        }

        Graphics2D g = splash.createGraphics();

        if (g == null)
        {
            setupComponents();
            return;
        }

        renderSplashFrame(g, "Initializing Components", 0);
        splash.update();

        setupComponents();

        try { Thread.sleep(1000); }
        catch(InterruptedException e) {}

        splash.close();
    }

    private void renderSplashFrame(Graphics2D g, String exercise, int aFrame)
    {
        g.setPaintMode();

        g.drawImage(mplashImage, 0, 0, rootPane);

        g.setColor(Color.WHITE);
        g.drawString(exercise, 7, 18);
    }

    private void setupComponents()
    {
        setupMenuBar();
        setupToolBar();

        m_uiDisplay.start();

        add(m_uiDisplay, BorderLayout.CENTER);
    }

    private void setupMenuBar()
    {
        // Actions
        m_uiLoadRom.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiLoadRom_actionPerformed(e);
            }
        });

        m_uiSaveState.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiSaveState_actionPerformed(e);
            }
        });

        m_uiSaveStateAs.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiSaveStateAs_actionPerformed(e);
            }
        });

        m_uiLoadState.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiLoadState_actionPerformed(e);
            }
        });

        m_uiAbout.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiAbout_actionPerformed(e);
            }
        });

        m_uiQuit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiQuit_actionPerformed(e);
            }
        });

        m_uiReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiReset_actionPerformed(e);
            }
        });

        m_uiSelect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiSelect_actionPerformed(e);
            }
        });

        m_uiPlayerOneAmateur.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiPlayerOneAmateur_actionPerformed(e);
            }
        });

        m_uiPlayerOneProfessional.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiPlayerOneProfessional_actionPerformed(e);
            }
        });

        m_uiPlayerTwoAmateur.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiPlayerTwoAmateur_actionPerformed(e);
            }
        });

        m_uiPlayerOneProfessional.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiPlayerTwoProfessional_actionPerformed(e);
            }
        });

        m_uiControls.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiControls_actionPerformed(e);
            }
        });

        m_uiColor.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiColor_actionPerformed(e);
            }
        });

        m_uiBW.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiBW_actionPerformed(e);
            }
        });

        m_uiShowDisassembly.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiShowDisassembly_actionPerformed(e);
            }
        });

        // Test
        m_uiOperation0.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiOperation0_actionPerformed(e);
            }
        });

        m_uiOperation1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiOperation1_actionPerformed(e);
            }
        });

        // Add everything
        m_uiFile.add(m_uiLoadRom);
        m_uiFile.addSeparator();
        m_uiFile.add(m_uiSaveState);
        m_uiFile.add(m_uiSaveStateAs);
        m_uiFile.add(m_uiLoadState);
        m_uiFile.addSeparator();
        m_uiFile.add(m_uiAbout);
        m_uiFile.addSeparator();
        m_uiFile.add(m_uiQuit);

        m_uiPlayerOne.add(m_uiPlayerOneAmateur);
        m_uiPlayerOne.add(m_uiPlayerOneProfessional);
        m_uiPlayerTwo.add(m_uiPlayerTwoAmateur);
        m_uiPlayerTwo.add(m_uiPlayerTwoProfessional);
        m_uiTelevisionMode.add(m_uiColor);
        m_uiTelevisionMode.add(m_uiBW);

        m_uiConsole.add(m_uiReset);
        m_uiConsole.add(m_uiSelect);
        m_uiConsole.addSeparator();
        m_uiConsole.add(m_uiPlayerOne);
        m_uiConsole.add(m_uiPlayerTwo);
        m_uiConsole.addSeparator();
        m_uiConsole.add(m_uiTelevisionMode);

        m_uiOptions.add(m_uiControls);

        m_uiDebug.add(m_uiShowDisassembly);

        m_uiTestOperations.add(m_uiOperation0);
        m_uiTestOperations.add(m_uiOperation1);

        m_uiMenuBar.add(m_uiFile);
        m_uiMenuBar.add(m_uiConsole);
        m_uiMenuBar.add(m_uiOptions);
        m_uiMenuBar.add(m_uiDebug);
        m_uiMenuBar.add(m_uiTestOperations);

        //add(m_uiMenuBar, BorderLayout.NORTH);
        setJMenuBar(m_uiMenuBar);
    }

    private void setupToolBar()
    {
        m_uiToolBar.setFloatable(false);
        //m_uiToolBar.setPreferredSize(new Dimension(400, 32));
        m_uiToolBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        int iIconSize = 28;

        m_uiToolsLoad.setMinimumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsLoad.setMaximumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsLoad.setPreferredSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsLoad.setFocusable(false);

        m_uiToolsSave.setMinimumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsSave.setMaximumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsSave.setPreferredSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsSave.setFocusable(false);

        m_uiToolsPlayPause.setMinimumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsPlayPause.setMaximumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsPlayPause.setPreferredSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsPlayPause.setFocusable(false);

        m_uiToolsStop.setMinimumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsStop.setMaximumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsStop.setPreferredSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsStop.setFocusable(false);

        m_uiToolsDisAssembler.setMinimumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsDisAssembler.setMaximumSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsDisAssembler.setPreferredSize(new Dimension(iIconSize, iIconSize));
        m_uiToolsDisAssembler.setFocusable(false);

        m_uiToolsLoad.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsLoad_actionPerformed(e);
            }
        });

        m_uiToolsSave.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsSave_actionPerformed(e);
            }
        });

        m_uiToolsPlayPause.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsPlayPause_actionPerformed(e);
            }
        });

        m_uiToolsStop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsStop_actionPerformed(e);
            }
        });

        m_uiToolsDisAssembler.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsDisAssembler_actionPerformed(e);
            }
        });

        m_uiToolBar.add(m_uiToolsLoad);
        m_uiToolBar.add(m_uiToolsSave);
        m_uiToolBar.addSeparator();
        m_uiToolBar.add(m_uiToolsPlayPause);
        m_uiToolBar.add(m_uiToolsStop);
        m_uiToolBar.addSeparator();
        m_uiToolBar.add(m_uiToolsDisAssembler);

        add(m_uiToolBar, BorderLayout.PAGE_START);
    }

    private void setupEmulation()
    {
        m_emuConsole = new Console();
        m_uiDisplay.setConsole(m_emuConsole);
        m_uiDebugger.setConsole(m_emuConsole);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: actions">
    private void m_uiLoadRom_actionPerformed(ActionEvent e)
    {
        loadRomOrState();
    }

    private void m_uiSaveState_actionPerformed(ActionEvent e)
    {
        if (mRunningGameHasSaveState) saveState(mtate); // all prototyply
        else saveStateAs();
    }

    private void m_uiSaveStateAs_actionPerformed(ActionEvent e)
    {
        saveStateAs();
    }

    private void m_uiLoadState_actionPerformed(ActionEvent e)
    {
        loadRomOrState();
    }

    private void m_uiAbout_actionPerformed(ActionEvent e)
    {
        // TODO
    }

    private void m_uiQuit_actionPerformed(ActionEvent e)
    {
        System.exit(0);
    }

    private void m_uiReset_actionPerformed(ActionEvent e)
    {
        // TODO
    }

    private void m_uiSelect_actionPerformed(ActionEvent e)
    {
        // TODO
    }

    private void m_uiColor_actionPerformed(ActionEvent e)
    {
        m_uiColor.setSelected(true);
        m_uiBW.setSelected(false);

        // TODO
    }

    private void m_uiBW_actionPerformed(ActionEvent e)
    {
        m_uiColor.setSelected(false);
        m_uiBW.setSelected(true);

        // TODO
    }

    private void m_uiPlayerOneAmateur_actionPerformed(ActionEvent e)
    {
        m_uiPlayerOneAmateur.setSelected(true);
        m_uiPlayerOneProfessional.setSelected(false);

        // TODO
    }

    private void m_uiPlayerOneProfessional_actionPerformed(ActionEvent e)
    {
        m_uiPlayerOneAmateur.setSelected(false);
        m_uiPlayerOneProfessional.setSelected(true);

        // TODO
    }

    private void m_uiPlayerTwoAmateur_actionPerformed(ActionEvent e)
    {
        m_uiPlayerTwoAmateur.setSelected(true);
        m_uiPlayerTwoProfessional.setSelected(false);

        // TODO
    }

    private void m_uiPlayerTwoProfessional_actionPerformed(ActionEvent e)
    {
        m_uiPlayerTwoAmateur.setSelected(false);
        m_uiPlayerTwoProfessional.setSelected(true);

        // TODO
    }

    private void m_uiControls_actionPerformed(ActionEvent e)
    {
        // TODO
    }

    private void m_uiShowDisassembly_actionPerformed(ActionEvent e)
    {
        showDebugger();
    }

    private void m_uiOperation0_actionPerformed(ActionEvent e)
    {
        for (int i = 0; i < 10; i++)
        {
            m_uiDisplay.getDisplayGraphics().drawLine(8 + i * 10, 10, 16 + i * 10, 160);
        }

        m_uiDisplay.setSignal(true);
        m_uiDisplay.updateDisplayGraphics();
    }

    private void m_uiOperation1_actionPerformed(ActionEvent e)
    {
        //m_emuConsole.mStep = true;
        //m_uiDisplay.setTVSignal(false);
        //m_uiDisplay.updateDisplayGraphics();

        showDebugger();
    }

    private void m_uiToolsLoad_actionPerformed(ActionEvent e)
    {
        loadRomOrState();
    }

    private void m_uiToolsSave_actionPerformed(ActionEvent e)
    {
        if (mRunningGameHasSaveState) saveState(mtate); // all prototyply
        else saveStateAs();
    }

    private void m_uiToolsPlayPause_actionPerformed(ActionEvent e)
    {
        if (m_emuConsole.getSystemHalted())
        {
            // these conditions should be replaced by a more
            // consistent method of Console
            if (m_rom == null) loadRomOrState();
            
            if (m_rom != null)
            {
                m_emuConsole.setSystemHalted(false);
                m_uiToolsPlayPause.setIcon(m_uiPauseIcon);
            }

            //if (m_rom != null && !m_uiDisplay.isRunning()) m_uiDisplay.start();
        }
        else
        {
            m_emuConsole.setSystemHalted(true);
            m_uiToolsPlayPause.setIcon(m_uiPlayIcon);
        }

        if (m_rom != null) m_uiDisplay.setSignal(true);
    }

    private void m_uiToolsStop_actionPerformed(ActionEvent e)
    {
        // to be implemented
        //m_emuConsole.reset();

        m_emuConsole.setSystemHalted(true);
        m_uiToolsPlayPause.setIcon(m_uiPlayIcon);
        m_uiDisplay.setSignal(false);
    }

    private void m_uiToolsDisAssembler_actionPerformed(ActionEvent e)
    {
        showDebugger();
    }

    private void showDebugger()
    {
        if (m_rom != null)
        {
            m_uiDebugger.updateDisAssembly();
            m_uiDebugger.setVisible(true);
            m_uiDisplay.setSignal(true);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: save and load">
    private void loadRomOrState()
    {
        m_romFileDialog.setMultiSelectionEnabled(false);
        m_romFileDialog.addChoosableFileFilter(m_romFileFilter);

        if (m_romFileDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            m_rom = m_romFileDialog.getSelectedFile();
            m_emuConsole.getCardridge().setCartridge(m_rom);

            m_emuConsole.getCPU().reset();
            m_emuConsole.getTIA().reset();
            m_emuConsole.getRIOT().reset();

            // thats all a little crappy... to be cleaned up
            m_emuConsole.setSystemHalted(true);
            m_uiToolsPlayPause.setIcon(m_uiPlayIcon);
            m_uiDisplay.setSignal(false);

            // FOR TESTING PURPOSES

            m_emuConsole.getTIA().getVideo().setVisibleHeight(VideoMode.NTSC, 318);
            m_uiDisplay.updateDisplayResolution();

            //m_uiDebugger.updateDisAssembly();

            // --------------------

            //m_uiDisplay.start();
        }
    }

    private void saveStateAs()
    {
        mtateFileDialog.setMultiSelectionEnabled(false);
        mtateFileDialog.addChoosableFileFilter(mtateFileFilter);

        if (mtateFileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File chosenFile = mtateFileDialog.getSelectedFile();
            saveState(chosenFile);
        }
    }

    private void saveState(File f)
    {
        // ...
    }
    // </editor-fold>
    // </editor-fold>
}