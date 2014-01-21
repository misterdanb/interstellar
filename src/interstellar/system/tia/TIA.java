/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro, db1992
 */

/*
 * Things TODO:
 * - sound
 * - inputs
 */

package interstellar.system.tia;

import interstellar.system.tia.video.Video;
import static interstellar.system.utilities.Conversion.*;
import static interstellar.system.tia.constants.Addresses.*;
import static interstellar.system.tia.constants.Dimensions.*;
import interstellar.system.*;

public class TIA extends ConsoleDevice implements Addressable
{
    // <editor-fold defaultstate="collapsed" desc="members">
    // <editor-fold defaultstate="collapsed" desc="members: video output">
    private Video m_video = new Video();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: picture counters">
    private int mPosition = 0;

    // VCounter does not include the vertical sync
    private int mScanline = 0;
    private int mPictureScanline = 0;

    private boolean mLastVSyncState = false;
    private boolean mLastVBlankState = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: registers">
    private int[] mRegisters = new int[0x40];
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="members: virtual pins">
    // isn't this a pin of the processor??
    private boolean mRDY = false; // at the end of every scanline this pin is set false again
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: game objects">
    // <editor-fold defaultstate="collapsed" desc="members: game objects: positions">
    private int mPosP0 = 0;
    private int mPosP1 = 0;
    private int mPosM0 = 0;
    private int mPosM1 = 0;
    private int mPosBL = 0;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: game objects: visibilities">
    private boolean mP0Visible = false;
    private boolean mP1Visible = false;
    private boolean mM0Visible = false;
    private boolean mM1Visible = false;
    private boolean mPFVisible = false;
    private boolean mBLVisible = false;

    private boolean mP0InPixel = false;
    private boolean mP1InPixel = false;
    private boolean mM0InPixel = false;
    private boolean mM1InPixel = false;
    private boolean mPFInPixel = false;
    private boolean mBLInPixel = false;

    private boolean mP0PixelFilled = false; // are collisions also detected on copies ?
    private boolean mP1PixelFilled = false; // are collisions also detected on copies ?
    private boolean mM0PixelFilled = false; // are collisions also detected on copies ?
    private boolean mM1PixelFilled = false; // are collisions also detected on copies ?
    private boolean mPFPixelFilled = false; // are collisions also detected on copies ?
    private boolean mBLPixelFilled = false; // are collisions also detected on copies ?
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: game objects: graphics">
    // the graphics arrays are calculated to the requested size set in the registers
    private boolean[] mP0Graphics = getPlayerGraphics(0);
    private boolean[] mP1Graphics = getPlayerGraphics(1);
    private boolean[] mM0Graphics = getMissileGraphics(0);
    private boolean[] mM1Graphics = getMissileGraphics(1);
    private boolean[] mPFGraphics = getPlayfieldGraphics();
    private boolean[] mBLGraphics = getBallGraphics();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: game objects: copies and spacings">
    private int mPM0Copies = 1;
    private int mPM1Copies = 1;
    private int mPM0CopySpacing = 1;
    private int mPM1CopySpacing = 1;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: game objects: vertical delays">
    private int mDGRP0 = 0;
    private int mDGRP1 = 0;
    private int mDENABL = 0;
    private boolean mVerticalDelayP0 = false; // until new graphics are written to GRP1
    private boolean mVerticalDelayP1 = false; // until new graphics are written to GRP0
    private boolean mVerticalDelayBL = false; // until new graphics are written to GRP1
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: special emulations">
    // hmove during blanking period causes 8 black pixels at the left border
    private boolean mHMOVEWhileBlanking = false;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods">
    // <editor-fold defaultstate="collapsed" desc="methods: constructor">
    public TIA(Console console)
    {
        super(console, "TIA");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: overloaded methods">
    public void initialize()
    {
        reset();
    }

    public void reset()
    {
        //m_videoBuffer = new Video();

        clearRegisters();

        setPosition(0);

        setScanline(0);
        setPictureScanline(0);

        setPosP0(0);
        setPosP1(0);
        setPosM0(0);
        setPosM1(0);
        setPosBL(0);

        mP0Visible = false;
        mP1Visible = false;
        mM0Visible = false;
        mM1Visible = false;
        mPFVisible = false;
        mBLVisible = false;

        mP0InPixel = false;
        mP1InPixel = false;
        mM0InPixel = false;
        mM1InPixel = false;
        mPFInPixel = false;
        mBLInPixel = false;

        mP0PixelFilled = false;
        mP1PixelFilled = false;
        mM0PixelFilled = false;
        mM1PixelFilled = false;
        mPFPixelFilled = false;
        mBLPixelFilled = false;

        mP0Graphics = getPlayerGraphics(0);
        mP1Graphics = getPlayerGraphics(1);
        mM0Graphics = getMissileGraphics(0);
        mM1Graphics = getMissileGraphics(1);
        mPFGraphics = getPlayfieldGraphics();
        mBLGraphics = getBallGraphics();

        mPM0Copies = 1;
        mPM1Copies = 1;
        mPM0CopySpacing = 1;
        mPM1CopySpacing = 1;

        mDGRP0 = 0;
        mDGRP1 = 0;
        mDENABL = 0;
        mVerticalDelayP0 = false;
        mVerticalDelayP1 = false;
        mVerticalDelayBL = false;

        mRDY = false;

        //m_videoBuffer.beginFrame();
    }

    private void clearRegisters()
    {
        for (int i = 0; i < mRegisters.length; i++) setRegister(i, 0);
    }

    public int read(int addr)
    {
        // return only allowed registers
        return getRegister(addr);
    }

    public void write(int addr, int data)
    {
        writeRegister(addr, data);
    }
    
    private void writeRegister(int addr, int data)
    {
        switch (addr)
        {
            case VSYNC: updateVSYNC(data); break;
            case VBLANK: updateVBLANK(data); break;
            case WSYNC: updateWSYNC(data); break;
            case RSYNC: updateRSYNC(data); break;
            case NUSIZ0: updateNUSIZ0(data); break;
            case NUSIZ1: updateNUSIZ1(data); break;
            case COLUP0: updateCOLUP0(data); break;
            case COLUP1: updateCOLUP1(data); break;
            case COLUPF: updateCOLUPF(data); break;
            case COLUBK: updateCOLUBK(data); break;
            case CTRLPF: updateCTRLPF(data); break;
            case REFP0: updateREFP0(data); break;
            case REFP1: updateREFP1(data); break;
            case PF0: updatePF0(data); break;
            case PF1: updatePF1(data); break;
            case PF2: updatePF2(data); break;
            case RESP0: updateRESP0(data); break;
            case RESP1: updateRESP1(data); break;
            case RESM0: updateRESM0(data); break;
            case RESM1: updateRESM1(data); break;
            case RESBL: updateRESBL(data); break;
            case AUDC0: updateAUDC0(data); break;
            case AUDC1: updateAUDC1(data); break;
            case AUDF0: updateAUDF0(data); break;
            case AUDF1: updateAUDF1(data); break;
            case AUDV0: updateAUDV0(data); break;
            case AUDV1: updateAUDV1(data); break;
            case GRP0: updateGRP0(data); break;
            case GRP1: updateGRP1(data); break;
            case ENAM0: updateENAM0(data); break;
            case ENAM1: updateENAM1(data); break;
            case ENABL: updateENABL(data); break;
            case HMP0: updateHMP0(data); break;
            case HMP1: updateHMP1(data); break;
            case HMM0: updateHMM0(data); break;
            case HMM1: updateHMM1(data); break;
            case HMBL: updateHMBL(data); break;
            case VDELP0: updateVDELP0(data); break;
            case VDELP1: updateVDELP1(data); break;
            case VDELBL: updateVDELBL(data); break;
            case RESMP0: updateRESMP0(data); break;
            case RESMP1: updateRESMP1(data); break;
            case HMOVE: updateHMOVE(data); break;
            case HMCLR: updateHMCLR(data); break;
            case CXCLR: updateCXCLR(data); break;
        }
    }

    @Override
    public String toString()
    {
        return "Device: " + getDeviceName() + "\n" +
               "HCounter: " + mPosition + "\n" +
               "VCounter: " + mPictureScanline + "\n";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: accessors">
    public void setRDY(boolean rdy) { mRDY = rdy; }
    public void setVideo(Video video) { m_video = video; }
    
    public void setPosition(int position) { mPosition = position; }
    
    public void setScanline(int scanline) { mScanline = scanline; }
    public void setPictureScanline(int pictureScanline) { mPictureScanline = pictureScanline; }
    
    public void setPosP0(int posP0) { mPosP0 = posP0; }
    public void setPosP1(int posP1) { mPosP1 = posP1; }
    public void setPosM0(int posM0) { mPosM0 = posM0; }
    public void setPosM1(int posM1) { mPosM1 = posM1; }
    public void setPosBL(int posBL) { mPosBL = posBL; }
    
    public void setRegister(int addr, int data) { mRegisters[addr & 0x3F] = int8(data); }

    public boolean getRDY() { return mRDY; }
    public Video getVideo() { return m_video; }

    public int getPosition() { return mPosition; }
    public int getBlankPosition() { return mPosition < HORIZONTAL_BLANK ? mPosition : HORIZONTAL_BLANK - 1; }
    public int getPicturePosition() { return mPosition - HORIZONTAL_BLANK; }

    public int getScanline() { return mScanline; }
    public int getPictureScanline() { return mPictureScanline; }

    public int getPosP0() { return mPosP0; }
    public int getPosP1() { return mPosP1; }
    public int getPosM0() { return mPosM0; }
    public int getPosM1() { return mPosM1; }
    public int getPosBL() { return mPosBL; }

    public int getRegister(int addr) { return mRegisters[addr & 0x3F]; }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: tia: register updaters">
    private void updateVSYNC(int data)
    {
        setRegister(VSYNC, data);
    }

    private void updateVBLANK(int data)
    {
        setRegister(VBLANK, data);
    }

    private void updateWSYNC(int data)
    {
        setRegister(WSYNC, data);
        // this pin halts the processor
        mRDY = true;
    }

    private void updateRSYNC(int data)
    {
        setRegister(RSYNC, data);
        mPosition = 0; // Don't know if this is correct... but it's just for chip testing and not really used by programs, so who cares?
    }

    private void updateNUSIZ0(int data)
    {
        setRegister(NUSIZ0, data);
        // move the data to the register
        // update the player graphics for simple access
        updatePlayerGraphics(0);
        // update the missile graphics for simple access
        updateMissileGraphics(0);
        // update the amount of copies
        mPM0Copies = getPlayerAmountOfCopies(0);
        // update the copy spacing
        mPM0CopySpacing = getPlayerSpacingOfCopies(0);

        updatePlayerVisibility(0);
        updateMissileVisibility(0);
        updateMissileToPlayerCentering(0);
    }

    private void updateNUSIZ1(int data)
    {
        setRegister(NUSIZ1, data);
        // move the data to the register
        // update the player graphics for simple access
        updatePlayerGraphics(1);
        // update the missile graphics for simple access
        updateMissileGraphics(1);
        // update the amount of copies
        mPM1Copies = getPlayerAmountOfCopies(1);
        // update the copy spacing
        mPM1CopySpacing = getPlayerSpacingOfCopies(1);

        updatePlayerVisibility(1);
        updateMissileVisibility(1);
        updateMissileToPlayerCentering(1);
    }

    private void updateCOLUP0(int data)
    {
        setRegister(COLUP0, data);
    }

    private void updateCOLUP1(int data)
    {
        setRegister(COLUP1, data);
    }

    private void updateCOLUPF(int data)
    {
        setRegister(COLUPF, data);
    }

    private void updateCOLUBK(int data)
    {
        setRegister(COLUBK, data);
    }

    private void updateCTRLPF(int data)
    {
        setRegister(CTRLPF, data);
        // move the data to the regiser
        // update the ball and playfield graphics for simple access
        updateBallGraphics();
        updatePlayfieldGraphics();
        // visibility doesn't change i think
        //mPFVisible = mPF0 != 0 && mPF1 != 0 && mPF2 != 0;
    }

    private void updateREFP0(int data)
    {
        setRegister(REFP0, data);
        // move the data to the regiser
        // update the player graphics for simple access
        updatePlayerGraphics(0);
    }

    private void updateREFP1(int data)
    {
        setRegister(REFP1, data);
        // move the data to the regiser
        // update the player graphics for simple access
        updatePlayerGraphics(1);
    }

    private void updatePF0(int data)
    {
        setRegister(PF0, data);
        // move the data to the regiser
        // update the playfield graphics for simple access
        updatePlayfieldGraphics();
        // update the visibility
        updatePlayfieldVisibility();
    }

    private void updatePF1(int data)
    {
        setRegister(PF1, data);
        // move the data to the regiser
        // update the playfield graphics for simple access
        updatePlayfieldGraphics();
        // update the visibility
        updatePlayfieldVisibility();
    }

    private void updatePF2(int data)
    {
        setRegister(PF2, data);
        // move the data to the regiser
        // update the playfield graphics for simple access
        updatePlayfieldGraphics();
        // update the visibility
        updatePlayfieldVisibility();
    }

    private void updateRESP0(int data)
    {
        setRegister(RESP0, data);
        // reset the player to the current horizontal position, to 0, if we're in the hblank (+ an offset)
        setPosP0(getPicturePosition() >= 0 ? (getPicturePosition() + 5) % HORIZONTAL_PICTURE : 3);
        updateMissileToPlayerCentering(0);
    }

    private void updateRESP1(int data)
    {
        setRegister(RESP1, data);
        // reset the player to the current horizontal position, to 0, if we're in the hblank (+ an offset)
        setPosP1(getPicturePosition() >= 0 ? (getPicturePosition() + 5) % HORIZONTAL_PICTURE : 3);
        updateMissileToPlayerCentering(1);
    }

    private void updateRESM0(int data)
    {
        setRegister(RESM0, data);
        // reset the missile to the current horizontal position, to 0, if we're in the hblank and RESMP0 is not set
        if (!getBit(getRegister(RESMP0), 1)) setPosM0(getPicturePosition() >= 0 ? (getPicturePosition() + 4) % HORIZONTAL_PICTURE : 2);
        updateMissileToPlayerCentering(0);
    }

    private void updateRESM1(int data)
    {
        setRegister(RESM1, data);
        // reset the missile to the current horizontal position, to 0, if we're in the hblank and RESMP1 is not set
        if (!getBit(getRegister(RESMP1), 1)) setPosM1(getPicturePosition() >= 0 ? (getPicturePosition() + 4) % HORIZONTAL_PICTURE : 2);
        updateMissileToPlayerCentering(1);
    }

    private void updateRESBL(int data)
    {
        setRegister(RESBL, data);
        // reset the ball to the current horizontal position, to 0, if we're in the hblank
        setPosBL(getPicturePosition() >= 0 ? getPicturePosition() : 0);
    }

    private void updateAUDC0(int data)
    {
        setRegister(AUDC0, data);
    }

    private void updateAUDC1(int data)
    {
        setRegister(AUDC1, data);
    }

    private void updateAUDF0(int data)
    {
        setRegister(AUDF0, data);
    }

    private void updateAUDF1(int data)
    {
        setRegister(AUDF1, data);
    }

    private void updateAUDV0(int data)
    {
        setRegister(AUDV0, data);
    }

    private void updateAUDV1(int data)
    {
        setRegister(AUDV1, data);
    }

    private void updateGRP0(int data)
    {
        // not working!
        /*getRegister(GRP0] = data;

        mDelayedGRP1 = getRegister(GRP1];

        if (getBit(getRegister(VDELP0], 0)) getRegister(GRP0] = mDelayedGRP0;
        if (getBit(getRegister(VDELP1], 0)) getRegister(GRP1] = mDelayedGRP1;

        updatePlayerGraphics(0);
        updatePlayerGraphics(1);
        updatePlayerVisibility(0);
        updatePlayerVisibility(1);*/

        if (getBit(getRegister(VDELP1), 0))
        {
            // move P1 Graphics to the actual register that is drawn
            setRegister(GRP1, mDGRP1);
            // disable the delay until new graphics are written to the GRP1 register
            mVerticalDelayP1 = false;
            // update the graphics array for simple access
            updatePlayerGraphics(1);
            // update the visibility
            updatePlayerVisibility(1);
        }

        if (getBit(getRegister(VDELP0), 0))
        {
            // move dada to the delayed register
            mDGRP0 = data;
            // set the delay flag, because new graphics have been written
            mVerticalDelayP0 = true;
        }
        else
        {
            // just move the data to the real graphics register
            setRegister(GRP0, data);
            // update the graphics array for simple access
            updatePlayerGraphics(0);
            // update the visibility
            updatePlayerVisibility(0);
        }

        // like jstella does it (propably misunterstood by me, as it does not work)
        /*mDelayedGRP1 = getRegister(GRP1];

        if (getBit(getRegister(VDELP0], 0))
        {
            getRegister(GRP0] = mDelayedGRP0;

            updatePlayerGraphics(0);
            updatePlayerVisibility(0);
        }

        if (getBit(getRegister(VDELP1], 0))
        {
            getRegister(GRP1] = mDelayedGRP1;

            updatePlayerGraphics(1);
            updatePlayerVisibility(1);
        }*/
    }

    private void updateGRP1(int data)
    {
        // not working!
        /*getRegister(GRP1] = data;

        mDelayedGRP0 = getRegister(GRP0];
        mDelayedENABL = getRegister(ENABL];

        if (getBit(getRegister(VDELP0], 0)) getRegister(GRP0] = mDelayedGRP0;
        if (getBit(getRegister(VDELP1], 0)) getRegister(GRP1] = mDelayedGRP1;

        updatePlayerGraphics(0);
        updatePlayerGraphics(1);
        updatePlayerVisibility(0);
        updatePlayerVisibility(1);*/

        if (getBit(getRegister(VDELBL), 0))//mVerticalDelayBL)
        {
            // move the delayed enable register to the real one
            setRegister(ENABL, mDENABL);
            // disable the delay until something new is written to the register
            mVerticalDelayBL = false;
            // dont't update the ball graphics, because the size hasn't been changed
            updateBallGraphics();
            // update the balls visibility
            updateBallVisibility();
        }

        if (getBit(getRegister(VDELP0), 0))//mVerticalDelayP0)
        {
            // move P0 Graphics to the actual register that is drawn
            setRegister(GRP0, mDGRP0);
            // disable the delay until new graphics are written to the GRP1 register
            mVerticalDelayP0 = false;
            // update the graphics array for simple access
            updatePlayerGraphics(0);
            // update the visibility
            updatePlayerVisibility(0);
        }

        if (getBit(getRegister(VDELP1), 0))
        {
            // move dada to the delayed register
            mDGRP1 = data;
            // set the delay flag, because new graphics have been written
            mVerticalDelayP1 = true;
        }
        else
        {
            // just move the data to the real graphics register
            setRegister(GRP1, data);
            // update the graphics array for simple access
            updatePlayerGraphics(1);
            // update the visibility
            updatePlayerVisibility(1);
        }

        // like jstella does it (propably misunterstood by me, as it does not work)
        /*mDelayedGRP0 = getRegister(GRP0];
        mDelayedENABL = getRegister(ENABL];

        if (getBit(getRegister(VDELBL], 0) && getBit(mDelayedENABL, 1))
        {
            updateBallGraphics();
            mBLVisible = true; // has to be solved this way
        }
        else if (getBit(getRegister(ENABL], 1))
        {
            updateBallGraphics();
            updateBallVisibility();
        }

        if (getBit(getRegister(VDELP0], 0))
        {
            getRegister(GRP0] = mDelayedGRP0;

            updatePlayerGraphics(0);
            updatePlayerVisibility(0);
        }

        if (getBit(getRegister(VDELP1], 0))
        {
            getRegister(GRP1] = mDelayedGRP1;

            updatePlayerGraphics(1);
            updatePlayerVisibility(1);
        }*/
    }

    private void updateENAM0(int data)
    {
        setRegister(ENAM0, data);
        // move the data to the register
        // update the visibility, also in dependence of the RESMP0 register, as it hides the missile
        updateMissileVisibility(0);
    }

    private void updateENAM1(int data)
    {
        setRegister(ENAM1, data);
        // move the data to the register
        // update the visibility, also in dependence of the RESMP0 register, as it hides the missile
        updateMissileVisibility(1);
    }

    private void updateENABL(int data)
    {
        // not working!
        /*getRegister(ENABL] = data;

        if (getBit(getRegister(VDELBL], 0)) getRegister(ENABL] = mDelayedENABL;

        updateBallVisibility();*/

        if (getBit(getRegister(VDELBL), 0))
        {
            // move dada to the delayed register
            mDENABL = data;
            // set the delay flag, because new graphics have been written
            mVerticalDelayBL = true;
        }
        else
        {
            // just move the data to the real graphics register
            setRegister(ENABL, data);
            // update the graphics array for simple access
            updateBallGraphics();
            // update the visibility
            updateBallVisibility();
        }

        // like jstella does it (propably misunterstood by me, as it does not work)
        /*if (getBit(getRegister(VDELBL], 0) && getBit(mDelayedENABL, 1))
        {
            updateBallGraphics();
            mBLVisible = true; // has to be solved this way
        }
        else if (getBit(getRegister(ENABL], 1))
        {
            updateBallGraphics();
            updateBallVisibility();
        }*/
    }

    private void updateHMP0(int data)
    {
        setRegister(HMP0, data);
    }

    private void updateHMP1(int data)
    {
        setRegister(HMP1, data);
    }

    private void updateHMM0(int data)
    {
        setRegister(HMM0, data);
    }

    private void updateHMM1(int data)
    {
        setRegister(HMM1, data);
    }

    private void updateHMBL(int data)
    {
        setRegister(HMBL, data);
    }

    private void updateVDELP0(int data)
    {
        setRegister(VDELP0, data);
    }

    private void updateVDELP1(int data)
    {
        setRegister(VDELP1, data);
    }

    private void updateVDELBL(int data)
    {
        setRegister(VDELBL, data);
    }

    private void updateRESMP0(int data)
    {
        setRegister(RESMP0, data);
        
        updateMissileToPlayerCentering(0);
        updateMissileVisibility(0);
    }

    private void updateRESMP1(int data)
    {
        setRegister(RESMP1, data);
        
        updateMissileToPlayerCentering(1);
        updateMissileVisibility(1);
    }

    private void updateHMOVE(int data)
    {
        setRegister(HMOVE, data);
        
        // apply the horizontal motion
        setPosP0((getPosP0() + convertHighBitsToSigned(getRegister(HMP0))) % HORIZONTAL_PICTURE);
        setPosP1((getPosP1() + convertHighBitsToSigned(getRegister(HMP1))) % HORIZONTAL_PICTURE);
        setPosM0((getPosM0() + convertHighBitsToSigned(getRegister(HMM0))) % HORIZONTAL_PICTURE);
        setPosM1((getPosM1() + convertHighBitsToSigned(getRegister(HMM1))) % HORIZONTAL_PICTURE);
        setPosBL((getPosBL() + convertHighBitsToSigned(getRegister(HMBL))) % HORIZONTAL_PICTURE);
        
        updateMissileToPlayerCentering(0);
        updateMissileToPlayerCentering(1);

        if (getPosition() < HORIZONTAL_BLANK) mHMOVEWhileBlanking = true;
    }

    private void updateHMCLR(int data)
    {
        setRegister(HMCLR, data);
        
        // clear all motion registers
        setRegister(HMP0, 0);
        setRegister(HMP1, 0);
        setRegister(HMM0, 0);
        setRegister(HMM1, 0);
        setRegister(HMBL, 0);
    }

    private void updateCXCLR(int data)
    {
        setRegister(CXCLR, data);
        
        // clear all collision registers
        setRegister(CXM0P, 0);
        setRegister(CXM1P, 0);
        setRegister(CXP0FB, 0);
        setRegister(CXP1FB, 0);
        setRegister(CXM0FB, 0);
        setRegister(CXM1FB, 0);
        setRegister(CXBLPF, 0);
        setRegister(CXPPMM, 0);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: tia: image computation">
    private void updateGraphics()
    {
        // recheck the player and missile copies and how they behave
        // according to the collision checks
        mP0InPixel = false;
        mP1InPixel = false;
        mM0InPixel = false;
        mM1InPixel = false;
        mPFInPixel = false;
        mBLInPixel = false;

        int iVisibleP0Copy = 0;
        int iVisibleM0Copy = 0;
        int iVisibleP1Copy = 0;
        int iVisibleM1Copy = 0;

        for (int i = 0; i < mPM0Copies; i++)
        {
            if (mP0Visible && inPixel(getPosP0() + i * (mPM0CopySpacing + 1) * PLAYER_WIDTH, mP0Graphics.length))
            {
                iVisibleP0Copy = i;
                mP0InPixel = true;
            }

            if (mM0Visible && inPixel(getPosM0() + i * (mPM0CopySpacing + 1) * PLAYER_WIDTH, mM0Graphics.length))
            {
                iVisibleM0Copy = i;
                mM0InPixel = true;
            }
        }

        for (int i = 0; i < mPM1Copies; i++)
        {
            if (mP1Visible && inPixel(getPosP1() + i * (mPM1CopySpacing + 1) * PLAYER_WIDTH, mP1Graphics.length))
            {
                iVisibleP1Copy = i;
                mP1InPixel = true;
            }

            if (mM1Visible && inPixel(getPosM1() + i * (mPM1CopySpacing + 1) * PLAYER_WIDTH, mM1Graphics.length))
            {
                iVisibleM1Copy = i;
                mM1InPixel = true;
            }
        }

        if (mPFVisible) mPFInPixel = true;
        if (mBLVisible && inPixel(getPosBL(), mBLGraphics.length)) mBLInPixel = true;

        mP0PixelFilled = mP0InPixel ? mP0Graphics[getPicturePosition() - (getPosP0() + iVisibleP0Copy * (mPM0CopySpacing + 1) * PLAYER_WIDTH)] : false;
        mP1PixelFilled = mP1InPixel ? mP1Graphics[getPicturePosition() - (getPosP1() + iVisibleP1Copy * (mPM1CopySpacing + 1) * PLAYER_WIDTH)] : false;
        mM0PixelFilled = mM0InPixel ? mM0Graphics[getPicturePosition() - (getPosM0() + iVisibleM0Copy * (mPM0CopySpacing + 1) * PLAYER_WIDTH)] : false; // missile with player copy spacing
        mM1PixelFilled = mM1InPixel ? mM1Graphics[getPicturePosition() - (getPosM1() + iVisibleM1Copy * (mPM1CopySpacing + 1) * PLAYER_WIDTH)] : false; // missile with player copy spacing
        mPFPixelFilled = mPFInPixel ? mPFGraphics[getPicturePosition()] : false;
        mBLPixelFilled = mBLInPixel ? mBLGraphics[getPicturePosition() - getPosBL()] : false;
    }

    private void checkCollisions()
    {
        if (mM0PixelFilled && mP0PixelFilled) setRegister(CXM0P, setBit(getRegister(CXM0P), 6, true));
        if (mM0PixelFilled && mP1PixelFilled) setRegister(CXM0P, setBit(getRegister(CXM0P), 7, true));

        if (mM1PixelFilled && mP0PixelFilled) setRegister(CXM1P, setBit(getRegister(CXM1P), 7, true));
        if (mM1PixelFilled && mP1PixelFilled) setRegister(CXM1P, setBit(getRegister(CXM1P), 6, true));

        if (mP0PixelFilled && mPFPixelFilled) setRegister(CXP0FB, setBit(getRegister(CXP0FB), 7, true));
        if (mP0PixelFilled && mBLPixelFilled) setRegister(CXP0FB, setBit(getRegister(CXP0FB), 6, true));

        if (mP1PixelFilled && mPFPixelFilled) setRegister(CXP1FB, setBit(getRegister(CXP1FB), 7, true));
        if (mP1PixelFilled && mBLPixelFilled) setRegister(CXP1FB, setBit(getRegister(CXP1FB), 6, true));

        if (mM0PixelFilled && mPFPixelFilled) setRegister(CXM0FB, setBit(getRegister(CXM0FB), 7, true));
        if (mM0PixelFilled && mBLPixelFilled) setRegister(CXM0FB, setBit(getRegister(CXM0FB), 6, true));

        if (mM1PixelFilled && mPFPixelFilled) setRegister(CXM1FB, setBit(getRegister(CXM1FB), 7, true));
        if (mM1PixelFilled && mBLPixelFilled) setRegister(CXM1FB, setBit(getRegister(CXM1FB), 6, true));

        if (mBLPixelFilled && mPFPixelFilled) setRegister(CXBLPF, setBit(getRegister(CXBLPF), 7, true));

        if (mP0PixelFilled && mP1PixelFilled) setRegister(CXPPMM, setBit(getRegister(CXPPMM), 7, true));
        if (mM0PixelFilled && mM1PixelFilled) setRegister(CXPPMM, setBit(getRegister(CXPPMM), 6, true));
    }

    private boolean inPixel(int pos, int length)
    {
        return pos <= getPicturePosition() && getPicturePosition() <= pos + length - 1;
    }

    private int getPlayerSize(int index)
    {
        int iPlayerFactorIndex = 0;

        if (index == 0) iPlayerFactorIndex = getRegister(NUSIZ0) & 0x07;
        else if (index == 1) iPlayerFactorIndex = getRegister(NUSIZ1) & 0x07;
        else return 1;

        switch (iPlayerFactorIndex)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
                return PLAYER_SINGLE_SIZE;

            case 5: return PLAYER_DOUBLE_SIZE;
            case 7: return PLAYER_QUAD_SIZE;
        }

        return 1;
    }

    private int getPlayerAmountOfCopies(int index) // same for missile
    {
        // returns the amount of all visible player shapes
        int iPlayerCopies = 0;

        if (index == 0) iPlayerCopies = getRegister(NUSIZ0) & 0x07;
        else if (index == 1) iPlayerCopies = getRegister(NUSIZ1) & 0x07;
        else return 1;

        switch (iPlayerCopies)
        {
            case 0:
            case 5:
            case 7:
                return PLAYER_COPIES_1;

            case 1:
            case 2:
            case 4:
                return PLAYER_COPIES_2;

            case 3:
            case 6:
                return PLAYER_COPIES_3;
        }

        return 1;
    }

    private int getPlayerSpacingOfCopies(int index) // same for missile
    {
        int iPlayerCopySpacing = 0;

        if (index == 0) iPlayerCopySpacing = getRegister(NUSIZ0) & 0x07;
        else if (index == 1) iPlayerCopySpacing = getRegister(NUSIZ1) & 0x07;
        else return 0;

        switch (iPlayerCopySpacing)
        {
            case 0:
            case 5:
            case 7:
                return PLAYER_COPY_SPACING_CLOSE;

            case 1:
            case 3:
                return PLAYER_COPY_SPACING_MEDIUM;

            case 2:
            case 6:
                return 3;

            case 4: return PLAYER_COPY_SPACING_WIDE;
        }

        return 0;
    }

    private int getMissileSize(int index)
    {
        int iMissileFactorIndex = 0;

        if (index == 0) iMissileFactorIndex = (getRegister(NUSIZ0) & 0x18) >> 4;
        else if (index == 1) iMissileFactorIndex = (getRegister(NUSIZ1) & 0x18) >> 4;
        else return 1;

        switch (iMissileFactorIndex)
        {
            case 0: return MISSILE_SINGLE_SIZE;
            case 1: return MISSILE_DOUBLE_SIZE;
            case 2: return MISSILE_QUAD_SIZE;
            case 3: return MISSILE_OCTA_SIZE;
        }

        return 1;
    }

    private int getPlayfieldSize()
    {
        return HORIZONTAL_PICTURE / PLAYFIELD_WIDTH; // playfield has a factor of 4
    }

    private int getBallSize()
    {
        int iBallFactorIndex = (getRegister(CTRLPF) & 0x18) >> 4;

        switch (iBallFactorIndex)
        {
            case 0: return BALL_SINGLE_SIZE;
            case 1: return BALL_DOUBLE_SIZE;
            case 2: return BALL_QUAD_SIZE;
            case 3: return BALL_OCTA_SIZE;
        }

        return 1;
    }

    private boolean[] getPlayerGraphics(int index)
    {
        int iSize = getPlayerSize(index);
        boolean[] bGraphics = new boolean[PLAYER_WIDTH * iSize];
        boolean bReflect = false;

        if (index == 0)
        {
            bReflect = getBit(getRegister(REFP0), 3);

            // graphics are read from right to left
            if (bReflect) for (int i = 0; i < bGraphics.length; i++) bGraphics[i] = getBit(getRegister(GRP0), (int) (i / iSize)); // int rounds down
            // graphics are read from left to right
            else for (int i = 0; i < bGraphics.length; i++) bGraphics[i] = getBit(getRegister(GRP0), bGraphics.length - 1 - (int) (i / iSize)); // int rounds down
        }
        else if (index == 1)
        {
            bReflect = getBit(getRegister(REFP1), 3);

            // graphics are read from right to left
            if (bReflect) for (int i = 0; i < bGraphics.length; i++) bGraphics[i] = getBit(getRegister(GRP1), (int) (i / iSize)); // int rounds down
            // graphics are read from left to right
            else for (int i = 0; i < bGraphics.length; i++) bGraphics[i] = getBit(getRegister(GRP1), bGraphics.length - 1 - (int) (i / iSize)); // int rounds down
        }

        return bGraphics;
    }

    private boolean[] getMissileGraphics(int index)
    {
        int iSize = getMissileSize(index);
        boolean[] bGraphics = new boolean[MISSILE_WIDTH * iSize];

        // fill the graphics
        for (int i = 0; i < bGraphics.length; i++)
        {
            bGraphics[i] = true;
        }

        return bGraphics;
    }
    
    private boolean[] getPlayfieldGraphics()
    {
        int iSize = getPlayfieldSize();
        boolean[] bGraphics = new boolean[PLAYFIELD_WIDTH * iSize];
        boolean bReflect = getBit(getRegister(CTRLPF), 0);

        boolean[] bPlayfieldDots = new boolean[20]; // left screen half

        // read the playfield registers to an array
        for (int i = 0; i < 4; i++) bPlayfieldDots[0 + i] = getBit(getRegister(PF0), 4 + i); // Bit 4-7
        for (int i = 0; i < 8; i++) bPlayfieldDots[4 + i] = getBit(getRegister(PF1), 7 - i); // Bit 7-0
        for (int i = 0; i < 8; i++) bPlayfieldDots[12 + i] = getBit(getRegister(PF2), i);    // Bit 0-7

        // fill the screen size array up to the half
        for (int i = 0; i < HORIZONTAL_PICTURE / 2; i++) bGraphics[i] = bPlayfieldDots[i / iSize];

        // if reflection is enabled fill the right half of the array in opposite direction
        if (bReflect) for (int i = HORIZONTAL_PICTURE / 2; i < HORIZONTAL_PICTURE; i++) bGraphics[i] = bGraphics[HORIZONTAL_PICTURE - i - 1]; // check, but should also work
        // else just copy it to the right half
        else for (int i = HORIZONTAL_PICTURE / 2; i < HORIZONTAL_PICTURE; i++) bGraphics[i] = bGraphics[i - HORIZONTAL_PICTURE / 2];

        return bGraphics;
    }

    private boolean[] getBallGraphics()
    {
        int iSize = getBallSize();
        boolean[] bGraphics = new boolean[BALL_WIDTH * iSize];

        // fill the graphics
        for (int i = 0; i < bGraphics.length; i++) bGraphics[i] = true;

        return bGraphics;
    }

    private void updateMissileToPlayerCentering(int index)
    {
        if (index == 0)
        {
            // if RESMP0 is set, we center the missile to the players position
            if (getBit(getRegister(RESMP0), 1))
            {
                int iPlayerSize = getPlayerSize(0);
                setPosM0(getPosP0());

                switch (iPlayerSize)
                {
                    case 1: setPosM0((getPosM0() + MISSILE_CENTERING_OFFSET_SINGLE) % HORIZONTAL_PICTURE); break;
                    case 2: setPosM0((getPosM0() + MISSILE_CENTERING_OFFSET_DOUBLE) % HORIZONTAL_PICTURE); break;
                    case 4: setPosM0((getPosM0() + MISSILE_CENTERING_OFFSET_QUAD) % HORIZONTAL_PICTURE); break;
                }
            }
        }
        else if (index == 1)
        {
            // if RESMP1 is set, we center the missile to the players position
            if (getBit(getRegister(RESMP1), 1))
            {
                int iPlayerSize = getPlayerSize(1);
                setPosM1(getPosP1());

                switch (iPlayerSize)
                {
                    case 1: setPosM1((getPosM1() + MISSILE_CENTERING_OFFSET_SINGLE) % HORIZONTAL_PICTURE); break;
                    case 2: setPosM1((getPosM1() + MISSILE_CENTERING_OFFSET_DOUBLE) % HORIZONTAL_PICTURE); break;
                    case 4: setPosM1((getPosM1() + MISSILE_CENTERING_OFFSET_QUAD) % HORIZONTAL_PICTURE); break;
                }
            }
        }
    }

    private void updatePlayerVisibility(int index)
    {
        if (index == 0) mP0Visible = getRegister(GRP0) != 0;
        else if (index == 1) mP1Visible = getRegister(GRP1) != 0;
    }

    private void updateMissileVisibility(int index)
    {
        if (index == 0) mM0Visible = getBit(getRegister(ENAM0), 1) && !getBit(getRegister(RESMP0), 1);
        else if (index == 1) mM1Visible = getBit(getRegister(ENAM1), 1) && !getBit(getRegister(RESMP1), 1);
    }

    private void updatePlayfieldVisibility()
    {
        mPFVisible = getRegister(PF0) != 0 || getRegister(PF1) != 0 || getRegister(PF2) != 0;
    }

    private void updateBallVisibility()
    {
        mBLVisible = getBit(getRegister(ENABL), 1);
    }

    private void updatePlayerGraphics(int index)
    {
        if (index == 0) mP0Graphics = getPlayerGraphics(0);
        else if (index == 1) mP1Graphics = getPlayerGraphics(1);
    }

    private void updateMissileGraphics(int index)
    {
        if (index == 0) mM0Graphics = getMissileGraphics(0);
        else if (index == 1) mM1Graphics = getMissileGraphics(1);
    }

    private void updatePlayfieldGraphics()
    {
        mPFGraphics = getPlayfieldGraphics();
    }

    private void updateBallGraphics()
    {
        mBLGraphics = getBallGraphics();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: tia: emulation">
    public void clock()
    {
        try { executeColorClock(); }
        catch (SystemException e) { System.err.println(e.toString()); }
    }
    
    private void scanlineUp() { setScanline(getScanline() + 1); }
    private void pictureUp() { setScanline(getScanline() + 1); setPictureScanline(getPictureScanline() + 1); }
    private void positionUp() { setPosition(getPosition() + 1); }

    private void executeColorClock() throws SystemException
    {
        // Begin the Color clock
        beginColorClock();

        if (getPosition() < HORIZONTAL_BLANK)
        {
            // horizontal blank (16 clocks sync and color burst, doesn't need to be emulated)
            // we're in left of the actual tv picture, so nothing to drawPixel
            endColorClock();
            return;
        }

        if (getBit(getRegister(VSYNC), 1) || getBit(getRegister(VBLANK), 1))
        {
            // we're over the actual tv picture, so nothing to drawPixel
            //mVPictureCounter = 0;
            endColorClock();
            return;
        }

        // actually overscan is part of blanking period
        /*if (getBit(getRegister(VBLANK], 1) && mPictureScanlineCounter >= m_videoBuffer.getLiveFrame().getHeight()) // CHECK FOR PAL !!!
        {
            // we're below the actual tv picture, so nothing to drawPixel
            endColorClock();
            return;
        }*/

        // check all possible collisiosns
        updateGraphics();
        checkCollisions(); // check for possible timing problems
        drawGraphics();

        // Color clock is over, end it
        endColorClock();
    }

    private void beginColorClock() throws SystemException
    {
    }

    private void endColorClock() throws SystemException
    {
        // TODO CHECK
        if (getPosition() == HORIZONTAL_TOTAL - 1)
        {
            // scanline is over
            setPosition(0);
            mHMOVEWhileBlanking = false;
            mRDY = false; // scnaline over, processor must not be halt anymore

            // condition for beginning the frame
            if (mLastVBlankState && !getBit(getRegister(VBLANK), 1))
            {
                // the actual picture begins now
                m_video.beginFrame();
            }

            // condition for ending the frame
            if (!mLastVBlankState && getBit(getRegister(VBLANK), 1))
            {
                // the actual picture begins now
                m_video.endFrame();
            }

            // condition for releasing the frame
            // extra condition, as it would below release the frame more than one time,
            // what eats more computing power
            /*if (mPictureScanlineCounter == m_videoBuffer.getLiveFrame().getHeight())
            {
                // the actual picture can now be released
                m_videoBuffer.endFrame();
            }*/

            // this all here is, because the software has to do all the vertical timings
            if (getBit(getRegister(VSYNC), 1))
            {
                // vertical sync (new frame)
                // actually this is done during the VBLANK, so
                // there are 3 lines vblank more, that are not
                // recognized
                if (!mLastVSyncState)
                {
                    setScanline(0);
                    setPictureScanline(0);
                    //mScanline = 0;
                    //mVBlankScanline = 0; // counts blanking and overscan
                    //mPictureScanline = 0;
                }
                else
                {
                    scanlineUp();
                    //mVBlankScanline++;
                }

                //if (!mLastVSyncState) m_videoBuffer.endFrame();
            }
            else if (getBit(getRegister(VBLANK), 1))
            {
                // vertical blank time
                scanlineUp();
                //mVBlankScanline++;
                //mVPictureCounter = 0; // dont know if this is necassary
            }
            // overscan is actually part of blanking period
            /*else if (getBit(getRegister(VBLANK], 1) && mPictureScanlineCounter >= m_videoBuffer.getLiveFrame().getHeight())
            {
                // picture is over
                mScanlineCounter++;
                mOverscanScanlineCounter++;
            }*/
            else
            {
                pictureUp();
                //mPictureScanline++;
            }

            mLastVSyncState = getBit(getRegister(VSYNC), 1);
            mLastVBlankState = getBit(getRegister(VBLANK), 1);
        }
        else
        {
            // next color clock
            positionUp();
        }
    }

    private void drawGraphics() throws SystemException
    {
        // 8 black pixels at the left border
        if (mHMOVEWhileBlanking && getPicturePosition() < 8) m_video.drawPixel(getPicturePosition(), getPictureScanline(), 0x000000);
        // is only triggered when it is in the real picture
        else if(getBit(getRegister(CTRLPF), 2))
        {
            if (mPFPixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUPF)), getLowBits(getRegister(COLUPF)));
            else if (mBLPixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUPF)), getLowBits(getRegister(COLUPF)));
            else if (mP0PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP0)), getLowBits(getRegister(COLUP0)));
            else if (mM0PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP0)), getLowBits(getRegister(COLUP0)));
            else if (mP1PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP1)), getLowBits(getRegister(COLUP1)));
            else if (mM1PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP1)), getLowBits(getRegister(COLUP1)));
            else m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUBK)), getLowBits(getRegister(COLUBK)));
        }
        else
        {
            if (mP0PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP0)), getLowBits(getRegister(COLUP0)));
            else if (mM0PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP0)), getLowBits(getRegister(COLUP0)));
            else if (mP1PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP1)), getLowBits(getRegister(COLUP1)));
            else if (mM1PixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP1)), getLowBits(getRegister(COLUP1)));
            else if (mBLPixelFilled) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUPF)), getLowBits(getRegister(COLUPF)));
            else if (mPFPixelFilled)
            {
                if (getBit(getRegister(CTRLPF), 1))
                {
                    if (getPicturePosition() < HORIZONTAL_PICTURE / 2) m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP0)), getLowBits(getRegister(COLUP0)));
                    else m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUP1)), getLowBits(getRegister(COLUP1)));
                }
                else m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUPF)), getLowBits(getRegister(COLUPF)));
            }
            else m_video.drawPixel(getPicturePosition(), getPictureScanline(), getHighBits(getRegister(COLUBK)), getLowBits(getRegister(COLUBK)));
        }
    }
    // </editor-fold>
    // </editor-fold>
}