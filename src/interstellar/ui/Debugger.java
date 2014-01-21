/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.ui;

import interstellar.system.mos6502.disasm.Disassembler;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import static interstellar.system.tia.constants.Addresses.*;
import interstellar.system.*;

/**
 * @author db1992
 */

public class Debugger extends JFrame
{
    // <editor-fold defaultstate="collapsed" desc="members">
    // <editor-fold defaultstate="collapsed" desc="memebers: console and disassembler">
    private Disassembler misassembler = new Disassembler();

    private Console m_emuConsole = null;

    private String[] mDisAssembly = null;
    private int[] mInstructionPointers = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components">
    // <editor-fold defaultstate="collapsed" desc="members: components: toolbar">
    private JToolBar m_uiToolBar = new JToolBar();

    private final ImageIcon m_uiRegisterIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/tree_leaf.png")));
    private final ImageIcon m_uiOpenIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/tree_open.png")));
    private final ImageIcon m_uiClosedIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/tree_closed.png")));

    private JTextField m_uiToolsClockAmount = new JTextField("0");
    private JButton m_uiToolsRefreshClocks = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/refresh.png"))));
    private JButton m_uiToolsOpenRamViewer = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/register.png"))));
    private JButton m_uiToolsClose = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("content/icons/close.png"))));
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane">
    private JPanel m_uiSplitPaneLeft = new JPanel();
    private JPanel m_uiSplitPaneRight = new JPanel();
    private JSplitPane m_uiSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_uiSplitPaneLeft, m_uiSplitPaneRight);

    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree">
    private JScrollPane m_uiTreeScroller = new JScrollPane();
    private DefaultMutableTreeNode m_uiInfoTreeSystem = new DefaultMutableTreeNode("Atari 2600");

    private JTree m_uiInfoTree = new JTree(m_uiInfoTreeSystem);
    private DefaultTreeCellRenderer m_uiInfoTreeCellRenderer = new DefaultTreeCellRenderer();

    private DefaultMutableTreeNode m_uiInfoTreeCPU = new DefaultMutableTreeNode("CPU");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: cpu">
    private DefaultMutableTreeNode m_uiInfoTreeCPURegisters = new DefaultMutableTreeNode("Registers");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: cpu: registers">
    private final String mARegPrefix = "A: ";
    private DefaultMutableTreeNode m_uiInfoTreeAReg = new DefaultMutableTreeNode(mARegPrefix + "0x0");
    private final String mXRegPrefix = "X: ";
    private DefaultMutableTreeNode m_uiInfoTreeXReg = new DefaultMutableTreeNode(mXRegPrefix + "0x0");
    private final String mYRegPrefix = "Y: ";
    private DefaultMutableTreeNode m_uiInfoTreeYReg = new DefaultMutableTreeNode(mYRegPrefix + "0x0");
    private final String mSPRegPrefix = "SP: ";
    private DefaultMutableTreeNode m_uiInfoTreeSPReg = new DefaultMutableTreeNode(mSPRegPrefix + "0x0");
    private final String mPCRegPrefix = "PC: ";
    private DefaultMutableTreeNode m_uiInfoTreePCReg = new DefaultMutableTreeNode(mPCRegPrefix + "0x0");
    private final String mLPCRegPrefix = "LPC: ";
    private DefaultMutableTreeNode m_uiInfoTreeLPCReg = new DefaultMutableTreeNode(mLPCRegPrefix + "0x0");
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeCPUFlags = new DefaultMutableTreeNode("Flags");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: cpu: flags">
    private final String mNFlagPrefix = "N: ";
    private DefaultMutableTreeNode m_uiInfoTreeNFlag = new DefaultMutableTreeNode(mNFlagPrefix + "false");
    private final String mVFlagPrefix = "V: ";
    private DefaultMutableTreeNode m_uiInfoTreeVFlag = new DefaultMutableTreeNode(mVFlagPrefix + "false");
    private final String mBFlagPrefix = "B: ";
    private DefaultMutableTreeNode m_uiInfoTreeBFlag = new DefaultMutableTreeNode(mBFlagPrefix + "false");
    private final String mDFlagPrefix = "D: ";
    private DefaultMutableTreeNode m_uiInfoTreeDFlag = new DefaultMutableTreeNode(mDFlagPrefix + "false");
    private final String mIFlagPrefix = "I: ";
    private DefaultMutableTreeNode m_uiInfoTreeIFlag = new DefaultMutableTreeNode(mIFlagPrefix + "false");
    private final String mZFlagPrefix = "Z: ";
    private DefaultMutableTreeNode m_uiInfoTreeZFlag = new DefaultMutableTreeNode(mZFlagPrefix + "false");
    private final String mCFlagPrefix = "C: ";
    private DefaultMutableTreeNode m_uiInfoTreeCFlag = new DefaultMutableTreeNode(mCFlagPrefix + "false");
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeCPUEmulation = new DefaultMutableTreeNode("Emulation");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: cpu: emulation">
    private final String mCycleCounterPrefix = "Remaining Cycles: ";
    private DefaultMutableTreeNode m_uiInfoTreeCycleCounter = new DefaultMutableTreeNode(mCycleCounterPrefix + "0");
    private final String mTotalCycleCounterPrefix = "Total Cycles: ";
    private DefaultMutableTreeNode m_uiInfoTreeTotalCycleCounter = new DefaultMutableTreeNode(mTotalCycleCounterPrefix + "0");
    // </editor-fold>
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIA = new DefaultMutableTreeNode("TIA");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia">
    private DefaultMutableTreeNode m_uiInfoTreeTIARegisters = new DefaultMutableTreeNode("Registers");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: registers">
    private DefaultMutableTreeNode m_uiInfoTreeTIACollisionRegisters = new DefaultMutableTreeNode("Collision");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: registers: collision">
    private final String mCXM0PPrefix = "CXM0P: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXM0P = new DefaultMutableTreeNode(mCXM0PPrefix + "0b0");
    private final String mCXM1PPrefix = "CXM1P: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXM1P = new DefaultMutableTreeNode(mCXM1PPrefix + "0b0");
    private final String mCXP0FBPrefix = "CXP0FB: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXP0FB = new DefaultMutableTreeNode(mCXP0FBPrefix + "0b0");
    private final String mCXP1FBPrefix = "CXP1FB: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXP1FB = new DefaultMutableTreeNode(mCXP1FBPrefix + "0b0");
    private final String mCXM0FBPrefix = "CXM0FB: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXM0FB = new DefaultMutableTreeNode(mCXM0FBPrefix + "0b0");
    private final String mCXM1FBPrefix = "CXM1FB: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXM1FB = new DefaultMutableTreeNode(mCXM1FBPrefix + "0b0");
    private final String mCXBLPFPrefix = "CXBLPF: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXBLPF = new DefaultMutableTreeNode(mCXBLPFPrefix + "0b0");
    private final String mCXPPMMPrefix = "CXPPMM: ";
    private DefaultMutableTreeNode m_uiInfoTreeCXPPMM = new DefaultMutableTreeNode(mCXPPMMPrefix + "0b0");
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIAInputRegisters = new DefaultMutableTreeNode("Inputs");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: registers: inputs">
    private final String mINPT0Prefix = "INPT0: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT0 = new DefaultMutableTreeNode(mINPT0Prefix + "0b0");
    private final String mINPT1Prefix = "INPT1: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT1 = new DefaultMutableTreeNode(mINPT1Prefix + "0b0");
    private final String mINPT2Prefix = "INPT2: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT2 = new DefaultMutableTreeNode(mINPT2Prefix + "0b0");
    private final String mINPT3Prefix = "INPT3: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT3 = new DefaultMutableTreeNode(mINPT3Prefix + "0b0");
    private final String mINPT4Prefix = "INPT4: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT4 = new DefaultMutableTreeNode(mINPT4Prefix + "0b0");
    private final String mINPT5Prefix = "INPT5: ";
    private DefaultMutableTreeNode m_uiInfoTreeINPT5 = new DefaultMutableTreeNode(mINPT5Prefix + "0b0");

    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIAGraphicsRegisters = new DefaultMutableTreeNode("Graphics");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: registers: graphics">
    private final String mGRP0Prefix = "GRP0: ";
    private DefaultMutableTreeNode m_uiInfoTreeGRP0 = new DefaultMutableTreeNode(mGRP0Prefix + "0");
    private final String mGRP1Prefix = "GRP1: ";
    private DefaultMutableTreeNode m_uiInfoTreeGRP1 = new DefaultMutableTreeNode(mGRP1Prefix + "0");
    private final String mPF0Prefix = "PF0: ";
    private DefaultMutableTreeNode m_uiInfoTreePF0 = new DefaultMutableTreeNode(mPF0Prefix + "0");
    private final String mPF1Prefix = "PF1: ";
    private DefaultMutableTreeNode m_uiInfoTreePF1 = new DefaultMutableTreeNode(mPF1Prefix + "0");
    private final String mPF2Prefix = "PF2: ";
    private DefaultMutableTreeNode m_uiInfoTreePF2 = new DefaultMutableTreeNode(mPF2Prefix + "0");
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIAInternalRegisters = new DefaultMutableTreeNode("Internal");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: registers: internal">
    private final String mPOSP0Prefix = "POSP0: ";
    private DefaultMutableTreeNode m_uiInfoTreePOSP0 = new DefaultMutableTreeNode(mPOSP0Prefix + "0");
    private final String mPOSP1Prefix = "POSP1: ";
    private DefaultMutableTreeNode m_uiInfoTreePOSP1 = new DefaultMutableTreeNode(mPOSP1Prefix + "0");
    private final String mPOSM0Prefix = "POSM0: ";
    private DefaultMutableTreeNode m_uiInfoTreePOSM0 = new DefaultMutableTreeNode(mPOSM0Prefix + "0");
    private final String mPOSM1Prefix = "POSM1: ";
    private DefaultMutableTreeNode m_uiInfoTreePOSM1 = new DefaultMutableTreeNode(mPOSM1Prefix + "0");
    private final String mPOSBLPrefix = "POSBL: ";
    private DefaultMutableTreeNode m_uiInfoTreePOSBL = new DefaultMutableTreeNode(mPOSBLPrefix + "0");
    // </editor-fold>
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIAEmulation = new DefaultMutableTreeNode("Emulation");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: emulation">
    private final String mRDYPinPrefix = "RDY-Pin: ";
    private DefaultMutableTreeNode m_uiInfoTreeRDYPin = new DefaultMutableTreeNode(mRDYPinPrefix + "false");

    private DefaultMutableTreeNode m_uiInfoTreeTIAPicture = new DefaultMutableTreeNode("Picture");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: emulation: picture">
    private DefaultMutableTreeNode m_uiInfoTreeTIAHorizontal = new DefaultMutableTreeNode("Horizontal");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: emulation: picture: horizontal">
    private final String mHCounterPrefix = "Total: ";
    private DefaultMutableTreeNode m_uiInfoTreeHCounter = new DefaultMutableTreeNode(mHCounterPrefix + "0");
    private final String mHBlankCounterPrefix = "Blank: ";
    private DefaultMutableTreeNode m_uiInfoTreeHBlankCounter = new DefaultMutableTreeNode(mHBlankCounterPrefix + "0");
    private final String mHPictureCounterPrefix = "Picture: ";
    private DefaultMutableTreeNode m_uiInfoTreeHPictureCounter = new DefaultMutableTreeNode(mHPictureCounterPrefix + "0");
    // </editor-fold>

    private DefaultMutableTreeNode m_uiInfoTreeTIAVertical = new DefaultMutableTreeNode("Vertical");
    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: system tree: tia: emulation: picture: vertical">
    private final String mVCounterPrefix = "Total: ";
    private DefaultMutableTreeNode m_uiInfoTreeVCounter = new DefaultMutableTreeNode(mVCounterPrefix + "0");
    private final String mVPictureCounterPrefix = "Picture: ";
    private DefaultMutableTreeNode m_uiInfoTreeVPictureCounter = new DefaultMutableTreeNode(mVPictureCounterPrefix + "0");
    private final String mVOverscanCounterPrefix = "Overscan: ";
    private DefaultMutableTreeNode m_uiInfoTreeVOverscanCounter = new DefaultMutableTreeNode(mVOverscanCounterPrefix + "0");
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: components: splitpane: instruction list">
    private JScrollPane m_uiListScroller = new JScrollPane();
    private JList m_uiAsmList = new JList();
    // </editor-fold>
    // </editor-fold>

    // RamViewer Window
    private RamViewer m_uiRamViewer = new RamViewer(new Dimension(0, 0));
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods">
    // <editor-fold defaultstate="collapsed" desc="methods: constructor">
    public Debugger(Dimension pos, Dimension size)
    {
        super("interSTELLAr - Debugger");

        setSize((int) size.getWidth(), (int) size.getHeight());
        setLocation((int) pos.getWidth(), (int) pos.getHeight());

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupComponents();

        toFront();
        setVisible(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: accessors">
    public void setConsole(Console console)
    {
        m_emuConsole = console;
        m_uiRamViewer.setConsole(console);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: setup">
    private void setupComponents()
    {
        setupToolBar();
        setupSplitPane();
        setupInfoTree();
        setupAsmList();
        setupRamViewer();
    }

    private void setupToolBar()
    {
        m_uiToolBar.setFloatable(false);
        //m_uiToolBar.setPreferredSize(new Dimension(400, 32));
        m_uiToolBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        m_uiToolsClockAmount.setMinimumSize(new Dimension(64, 32));
        m_uiToolsClockAmount.setMaximumSize(new Dimension(64, 32));
        m_uiToolsClockAmount.setPreferredSize(new Dimension(64, 32));
        m_uiToolsClockAmount.setFocusable(true);

        m_uiToolsRefreshClocks.setMinimumSize(new Dimension(32, 32));
        m_uiToolsRefreshClocks.setMaximumSize(new Dimension(32, 32));
        m_uiToolsRefreshClocks.setPreferredSize(new Dimension(32, 32));
        m_uiToolsRefreshClocks.setFocusable(false);

        m_uiToolsOpenRamViewer.setMinimumSize(new Dimension(32, 32));
        m_uiToolsOpenRamViewer.setMaximumSize(new Dimension(32, 32));
        m_uiToolsOpenRamViewer.setPreferredSize(new Dimension(32, 32));
        m_uiToolsOpenRamViewer.setFocusable(false);

        m_uiToolsClose.setMinimumSize(new Dimension(32, 32));
        m_uiToolsClose.setMaximumSize(new Dimension(32, 32));
        m_uiToolsClose.setPreferredSize(new Dimension(32, 32));
        m_uiToolsClose.setFocusable(false);

        m_uiToolsRefreshClocks.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsRefreshClocks_actionPerformed(e);
            }
        });

        m_uiToolsOpenRamViewer.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsOpenRamViewer_actionPerformed(e);
            }
        });

        m_uiToolsClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                m_uiToolsClose_actionPerformed(e);
            }
        });

        m_uiToolBar.add(m_uiToolsClockAmount);
        m_uiToolBar.add(m_uiToolsRefreshClocks);
        m_uiToolBar.addSeparator();
        m_uiToolBar.add(m_uiToolsOpenRamViewer);
        m_uiToolBar.addSeparator();
        m_uiToolBar.add(m_uiToolsClose);

        add(m_uiToolBar, BorderLayout.PAGE_START);
    }

    private void setupSplitPane()
    {
        m_uiSplitPaneLeft.setLayout(new BorderLayout());
        m_uiSplitPaneRight.setLayout(new BorderLayout());

        m_uiSplitPane.setDividerLocation(240);

        add(m_uiSplitPane, BorderLayout.CENTER);
    }
    
    private void setupInfoTree()
    {
        m_uiTreeScroller.getViewport().setView(m_uiInfoTree);
        m_uiTreeScroller.setBorder(BorderFactory.createEmptyBorder());

        m_uiInfoTreeCellRenderer.setLeafIcon(m_uiRegisterIcon);
        m_uiInfoTreeCellRenderer.setOpenIcon(m_uiOpenIcon);
        m_uiInfoTreeCellRenderer.setClosedIcon(m_uiClosedIcon);

        m_uiInfoTree.setEditable(false);
        m_uiInfoTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_uiInfoTree.setShowsRootHandles(true);
        m_uiInfoTree.setCellRenderer(m_uiInfoTreeCellRenderer);

        m_uiInfoTreeCPURegisters.add(m_uiInfoTreeAReg);
        m_uiInfoTreeCPURegisters.add(m_uiInfoTreeXReg);
        m_uiInfoTreeCPURegisters.add(m_uiInfoTreeYReg);
        m_uiInfoTreeCPURegisters.add(m_uiInfoTreeSPReg);
        m_uiInfoTreeCPURegisters.add(m_uiInfoTreePCReg);
        m_uiInfoTreeCPURegisters.add(m_uiInfoTreeLPCReg);

        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeNFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeVFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeBFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeDFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeIFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeZFlag);
        m_uiInfoTreeCPUFlags.add(m_uiInfoTreeCFlag);

        m_uiInfoTreeCPUEmulation.add(m_uiInfoTreeCycleCounter);
        m_uiInfoTreeCPUEmulation.add(m_uiInfoTreeTotalCycleCounter);

        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXM0P);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXM1P);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXP0FB);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXP1FB);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXM0FB);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXM1FB);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXBLPF);
        m_uiInfoTreeTIACollisionRegisters.add(m_uiInfoTreeCXPPMM);

        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT0);
        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT1);
        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT2);
        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT3);
        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT4);
        m_uiInfoTreeTIAInputRegisters.add(m_uiInfoTreeINPT5);

        m_uiInfoTreeTIAGraphicsRegisters.add(m_uiInfoTreeGRP0);
        m_uiInfoTreeTIAGraphicsRegisters.add(m_uiInfoTreeGRP1);
        m_uiInfoTreeTIAGraphicsRegisters.add(m_uiInfoTreePF0);
        m_uiInfoTreeTIAGraphicsRegisters.add(m_uiInfoTreePF1);
        m_uiInfoTreeTIAGraphicsRegisters.add(m_uiInfoTreePF2);

        m_uiInfoTreeTIAInternalRegisters.add(m_uiInfoTreePOSP0);
        m_uiInfoTreeTIAInternalRegisters.add(m_uiInfoTreePOSP1);
        m_uiInfoTreeTIAInternalRegisters.add(m_uiInfoTreePOSM0);
        m_uiInfoTreeTIAInternalRegisters.add(m_uiInfoTreePOSM1);
        m_uiInfoTreeTIAInternalRegisters.add(m_uiInfoTreePOSBL);

        m_uiInfoTreeTIAHorizontal.add(m_uiInfoTreeHCounter);
        m_uiInfoTreeTIAHorizontal.add(m_uiInfoTreeHBlankCounter);
        m_uiInfoTreeTIAHorizontal.add(m_uiInfoTreeHPictureCounter);

        m_uiInfoTreeTIAVertical.add(m_uiInfoTreeVCounter);
        m_uiInfoTreeTIAVertical.add(m_uiInfoTreeVPictureCounter);
        m_uiInfoTreeTIAVertical.add(m_uiInfoTreeVOverscanCounter);

        m_uiInfoTreeTIAPicture.add(m_uiInfoTreeTIAHorizontal);
        m_uiInfoTreeTIAPicture.add(m_uiInfoTreeTIAVertical);

        m_uiInfoTreeTIARegisters.add(m_uiInfoTreeTIACollisionRegisters);
        m_uiInfoTreeTIARegisters.add(m_uiInfoTreeTIAInputRegisters);
        m_uiInfoTreeTIARegisters.add(m_uiInfoTreeTIAGraphicsRegisters);
        m_uiInfoTreeTIARegisters.add(m_uiInfoTreeTIAInternalRegisters);

        m_uiInfoTreeTIAEmulation.add(m_uiInfoTreeRDYPin);
        m_uiInfoTreeTIAEmulation.add(m_uiInfoTreeTIAPicture);

        m_uiInfoTreeCPU.add(m_uiInfoTreeCPURegisters);
        m_uiInfoTreeCPU.add(m_uiInfoTreeCPUFlags);
        m_uiInfoTreeCPU.add(m_uiInfoTreeCPUEmulation);

        m_uiInfoTreeTIA.add(m_uiInfoTreeTIARegisters);
        m_uiInfoTreeTIA.add(m_uiInfoTreeTIAEmulation);

        m_uiInfoTreeSystem.add(m_uiInfoTreeCPU);
        m_uiInfoTreeSystem.add(m_uiInfoTreeTIA);

        // expand the tree
        m_uiInfoTree.scrollPathToVisible(new TreePath(m_uiInfoTreeCPURegisters.getPath()));
        m_uiInfoTree.scrollPathToVisible(new TreePath(m_uiInfoTreeTIARegisters.getPath()));
        //m_uiInfoTree.scrollPathToVisible(new TreePath(m_uiInfoTreeCycleCounter.getPath()));
        //m_uiInfoTree.scrollPathToVisible(new TreePath(m_uiInfoTreeHCounter.getPath()));
        //m_uiInfoTree.scrollPathToVisible(new TreePath(m_uiInfoTreeVCounter.getPath()));

        m_uiSplitPaneLeft.add(m_uiTreeScroller, BorderLayout.CENTER);
    }

    private void setupAsmList()
    {
        m_uiListScroller.getViewport().setView(m_uiAsmList);
        m_uiListScroller.setBorder(BorderFactory.createEmptyBorder());
        m_uiAsmList.setFont(new Font("Courier New", 0, 12));
        m_uiAsmList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        m_uiSplitPaneRight.add(m_uiListScroller, BorderLayout.CENTER);
    }

    private void setupRamViewer()
    {
        m_uiRamViewer.setLocation(0, (int) getSize().getHeight());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: actions">
    private void m_uiToolsRefreshClocks_actionPerformed(ActionEvent e)
    {
        m_emuConsole.debug(Integer.parseInt(m_uiToolsClockAmount.getText()));
        updateDebuggerInformation();
    }

    private void m_uiToolsOpenRamViewer_actionPerformed(ActionEvent e)
    {
        m_uiRamViewer.setVisible(true);
    }

    private void m_uiToolsClose_actionPerformed(ActionEvent e)
    {
        dispose();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: updates">
    public void updateDisAssembly()
    {
        try { misassembler.parse(m_emuConsole); }
        catch (SystemException e) { return; }

        mDisAssembly = misassembler.getDisAssembly();
        mInstructionPointers = misassembler.getInstructionPointers();

        m_uiAsmList.removeAll();
        m_uiAsmList.setListData(mDisAssembly);
    }

    public void updateDebuggerInformation()
    {
        m_uiInfoTreeAReg.setUserObject(mARegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getA()).toUpperCase());
        m_uiInfoTreeXReg.setUserObject(mXRegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getX()).toUpperCase());
        m_uiInfoTreeYReg.setUserObject(mYRegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getY()).toUpperCase());
        m_uiInfoTreeSPReg.setUserObject(mSPRegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getSP()).toUpperCase());
        m_uiInfoTreePCReg.setUserObject(mPCRegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getPC()).toUpperCase());
        m_uiInfoTreeLPCReg.setUserObject(mLPCRegPrefix + "0x" + Integer.toHexString(m_emuConsole.getCPU().getLPC()).toUpperCase());

        m_uiInfoTreeNFlag.setUserObject(mNFlagPrefix + String.valueOf(m_emuConsole.getCPU().getNFlag()));
        m_uiInfoTreeVFlag.setUserObject(mVFlagPrefix + String.valueOf(m_emuConsole.getCPU().getVFlag()));
        m_uiInfoTreeBFlag.setUserObject(mBFlagPrefix + String.valueOf(m_emuConsole.getCPU().getBFlag()));
        m_uiInfoTreeDFlag.setUserObject(mDFlagPrefix + String.valueOf(m_emuConsole.getCPU().getDFlag()));
        m_uiInfoTreeIFlag.setUserObject(mIFlagPrefix + String.valueOf(m_emuConsole.getCPU().getIFlag()));
        m_uiInfoTreeZFlag.setUserObject(mZFlagPrefix + String.valueOf(m_emuConsole.getCPU().getZFlag()));
        m_uiInfoTreeCFlag.setUserObject(mCFlagPrefix + String.valueOf(m_emuConsole.getCPU().getCFlag()));

        m_uiInfoTreeCycleCounter.setUserObject(mCycleCounterPrefix + String.valueOf(m_emuConsole.getCPU().getRemainingCycles()));
        m_uiInfoTreeTotalCycleCounter.setUserObject(mTotalCycleCounterPrefix + String.valueOf(m_emuConsole.getCPU().getTotalCycles()));

        m_uiInfoTreeCXM0P.setUserObject(mCXM0PPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXM0P)));
        m_uiInfoTreeCXM1P.setUserObject(mCXM1PPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXM1P)));
        m_uiInfoTreeCXP0FB.setUserObject(mCXP0FBPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXP0FB)));
        m_uiInfoTreeCXP1FB.setUserObject(mCXP1FBPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXP1FB)));
        m_uiInfoTreeCXM0FB.setUserObject(mCXM0FBPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXM0FB)));
        m_uiInfoTreeCXM1FB.setUserObject(mCXM1FBPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXM1FB)));
        m_uiInfoTreeCXBLPF.setUserObject(mCXBLPFPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXBLPF)));
        m_uiInfoTreeCXPPMM.setUserObject(mCXPPMMPrefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(CXPPMM)));
        m_uiInfoTreeINPT0.setUserObject(mINPT0Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT0)));
        m_uiInfoTreeINPT1.setUserObject(mINPT1Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT1)));
        m_uiInfoTreeINPT2.setUserObject(mINPT2Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT2)));
        m_uiInfoTreeINPT3.setUserObject(mINPT3Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT3)));
        m_uiInfoTreeINPT4.setUserObject(mINPT4Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT4)));
        m_uiInfoTreeINPT5.setUserObject(mINPT5Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(INPT5)));

        m_uiInfoTreeGRP0.setUserObject(mGRP0Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(GRP0)));
        m_uiInfoTreeGRP1.setUserObject(mGRP1Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(GRP1)));
        m_uiInfoTreePF0.setUserObject(mPF0Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(PF0)));
        m_uiInfoTreePF1.setUserObject(mPF1Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(PF1)));
        m_uiInfoTreePF2.setUserObject(mPF2Prefix + "0b" + Integer.toBinaryString(m_emuConsole.getTIA().getRegister(PF2)));

        m_uiInfoTreePOSP0.setUserObject(mPOSP0Prefix + String.valueOf(m_emuConsole.getTIA().getPosP0()));
        m_uiInfoTreePOSP1.setUserObject(mPOSP1Prefix + String.valueOf(m_emuConsole.getTIA().getPosP1()));
        m_uiInfoTreePOSM0.setUserObject(mPOSM0Prefix + String.valueOf(m_emuConsole.getTIA().getPosM0()));
        m_uiInfoTreePOSM1.setUserObject(mPOSM1Prefix + String.valueOf(m_emuConsole.getTIA().getPosM1()));
        m_uiInfoTreePOSBL.setUserObject(mPOSBLPrefix + String.valueOf(m_emuConsole.getTIA().getPosBL()));

        m_uiInfoTreeRDYPin.setUserObject(mRDYPinPrefix + String.valueOf(m_emuConsole.getTIA().getRDY()));

        m_uiInfoTreeHCounter.setUserObject(mHCounterPrefix + String.valueOf(m_emuConsole.getTIA().getPosition()));
        m_uiInfoTreeHBlankCounter.setUserObject(mHBlankCounterPrefix + String.valueOf(m_emuConsole.getTIA().getBlankPosition()));
        m_uiInfoTreeHPictureCounter.setUserObject(mHPictureCounterPrefix + String.valueOf(m_emuConsole.getTIA().getPicturePosition()));

        m_uiInfoTreeVCounter.setUserObject(mVCounterPrefix + String.valueOf(m_emuConsole.getTIA().getScanline()));
        m_uiInfoTreeVPictureCounter.setUserObject(mVPictureCounterPrefix + String.valueOf(m_emuConsole.getTIA().getPictureScanline()));
        //m_uiInfoTreeVOverscanCounter.setUserObject(mVOverscanCounterPrefix + String.valueOf(m_emuConsole.getTIA().getOverscanScanline()));

        m_uiInfoTree.updateUI();

        int i = 0;

        while (i < mInstructionPointers.length &&
               mInstructionPointers[i] != m_emuConsole.getCPU().getLPC()) i++;

        m_uiAsmList.setSelectedIndex(i);
        m_uiAsmList.ensureIndexIsVisible(i);
        m_uiAsmList.updateUI();

        m_uiRamViewer.updateRamInformation();
    }
    // </editor-fold>
    // </editor-fold>
}