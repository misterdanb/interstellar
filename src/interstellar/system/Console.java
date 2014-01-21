//Double check all Masks
package interstellar.system;

import static interstellar.system.utilities.Conversion.*;
import interstellar.system.mos6532.MOS6532;
import interstellar.system.mos6502.MOS6502;
import interstellar.system.cartridges.Cartridge;
import interstellar.system.tia.TIA;
import interstellar.system.tia.constants.Dimensions;
import java.io.File;

//This class work like / simmulate the electric circuit of the ataro
public class Console
{
    int mAddr;
    int mData;
    
    byte mClockCount;
    
    MOS6502 mCPU;
    MOS6532 mRAM;
    Cartridge mGame;
    TIA mStella;

    private boolean mHaltSystem = true;
    private boolean mStep = false;
    public boolean mStepping = false;

    public Console()
    {
        // give all devices a reference of this console
        mCPU = new MOS6502(this);
        mRAM = new MOS6532(this);
        mGame = new Cartridge(this);
        mStella = new TIA(this);

        mCPU.initialize();
        mRAM.initialize();
        mGame.initialize();
        mStella.initialize();
    }

    public void setSystemHalted(boolean bVal)
    {
        mHaltSystem = bVal;
    }

    public boolean getSystemHalted()
    {
        return mHaltSystem;
    }

    public MOS6502 getCPU()
    {
        return mCPU;
    }

    public MOS6532 getRIOT()
    {
        return mRAM;
    }

    public TIA getTIA()
    {
        return mStella;
    }

    public Cartridge getCardridge()
    {
        return mGame;
    }
    
    public int read(int addr)
    {
        if (!getBit(addr, 12) && getBit(addr, 7))
        {
            mRAM.setRS(getBit(addr, 9));
            mData = mRAM.read(addr & 0x7F);
        }
        
        if (!getBit(addr, 12) && !getBit(addr, 7))
        {
            mData = mStella.read(addr & 0x3F);
        }
        
        if (getBit(addr, 12))
        {
            mData = mGame.read(addr);
        }

        return mData;
    }

    public void write(int addr, int data)
    {
        if (!getBit(addr, 12) && getBit(addr, 7))
        {
            mRAM.setRS(getBit(addr, 9));
            mRAM.write(addr & 0x7F, data);
            mStepping = true;
        }
        
        if (!getBit(addr, 12) && !getBit(addr, 7))
        {
            mStella.write(addr & 0x3F, data);
        }
        
        if (getBit(addr, 12))
        {
            mGame.write(addr, data);
        }
    }

    public void setRes()
    {
        mCPU.reset();
    }

    public void setCartridge(File rom)
    {
        mGame.setCartridge(rom);
    }

    private void clock()
    {
        // debug
        if (mHaltSystem)
        {
            if (!mStep) return;
            mStep = false;
        }
        
        /*if(mStepping){
            mStep = false;
        }*/
        
        mCPU.clock();
        mRAM.clock();
        mStella.clock();
        mStella.clock();
        mStella.clock();
    }

    public void debug(int howMany)
    {
        for (int i = 0; i < howMany; i++)
        {
            mStep = true;
            clock();
        }

        //System.out.println(cpu.toString());
    }

    public void updateFrame()
    {
        for (int i = 0; (Dimensions.HORIZONTAL_TOTAL * Dimensions.VERTICAL_NTSC_TOTAL) / 3 > i; i++)
        {
            clock();
        }
    }
}
