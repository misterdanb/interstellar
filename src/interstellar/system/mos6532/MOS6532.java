/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro
 */

package interstellar.system.mos6532;

import interstellar.system.mos6532.constants.Addresses;
import interstellar.system.*;
import static interstellar.system.utilities.Conversion.*;

public class MOS6532 extends ConsoleDevice implements Addressable
{
    private int mRAM[]; //internal 128Byte Ram

    private int mData; //Databus
    private int mPortA; //Input port a
    private int mPortB; //Input port b
    private int mDDRA;
    private int mDDRB;
    private int mTimerPrescalerCount;
    private int mTimerPrescaler;
    
    // registers
    private int mTimer;
    private int mOverflow;

    private boolean mRamSelect;
    private boolean mHighDecrement;
    private boolean mInstantReadUnderflow; //Underflow flag since Instat read
    private boolean mTISetUnderflow; //Underflow flag since TI set

    public MOS6532(Console parentConsole)
    {
        super(parentConsole, "RIOT");
        
        mRAM = new int[128];
    }

    public void initialize()
    {
    }

    public void reset()
    {
    }

    public int read(int addr)
    {
        if (!mRamSelect)
        {
            mData = mRAM[addr&127];
        }
        else
        {
            switch (addr)
            {
                case Addresses.INTIM:
                    mData=mTimer;
                    mHighDecrement = false;
                    break;
                    
                case Addresses.INSTAT:
                    mOverflow = 0;
                    
                    if (mTISetUnderflow)
                    {
                        mOverflow=setBit(mOverflow, 6, true);
                    }
                    if (mInstantReadUnderflow)
                    {
                        mOverflow=setBit(mOverflow, 7, true);
                    }
                    
                    mTISetUnderflow=false;
                    mData = mOverflow;
                    
                //case Addresses.SWCHB:
                    //data=0x0B;
            }
        }
        return mData;
    }

    public void write(int addr, int data){
                //RAM
        if (!mRamSelect)
        {
            mRAM[addr & 0x7F] = data;
        }
        else
        {
             switch (addr)
             {
                 case Addresses.TIM1T:
                     mTimer = data;
                     mTISetUnderflow = false;
                     mTimerPrescaler = 1;
                     mTimerPrescalerCount = mTimerPrescaler;
                     mTimer--;
                     
                     if (mTimer < 0)
                     {
                         mInstantReadUnderflow = true;
                         mTISetUnderflow = true;
                         mTimer = 0xFF;
                         mHighDecrement = true;
                     }
                     
                     break;
                     
                 case Addresses.TIM8T:
                     mTimer = data;
                     mTISetUnderflow = false;
                     mTimerPrescaler = 8;
                     mTimerPrescalerCount = mTimerPrescaler;
                     mTimer--;
                     
                     if (mTimer < 0)
                     {
                         mInstantReadUnderflow = true;
                         mTISetUnderflow = true;
                         mTimer = 0xFF;
                         mHighDecrement = true;
                     }
                     
                     break;
                     
                 case Addresses.TIM64T:
                     mTimer = data;
                     mTISetUnderflow = false;
                     mTimerPrescaler = 64;
                     mTimerPrescalerCount=mTimerPrescaler;
                     mTimer--;
                     
                     if (mTimer < 0)
                     {
                         mInstantReadUnderflow = true;
                         mTISetUnderflow = true;
                         mTimer = 0xFF;
                         mHighDecrement = true;
                     }
                     
                     break;
                     
                 case Addresses.T1024T:
                     mTimer = data;
                     mTISetUnderflow = false;
                     mTimerPrescaler = 1024;
                     mTimerPrescalerCount=mTimerPrescaler;
                     mTimer--;
                     
                     if (mTimer < 0)
                     {
                         mInstantReadUnderflow = true;
                         mTISetUnderflow = true;
                         mTimer = 0xFF;
                         mHighDecrement = true;
                     }
                     
                     break;
            }
        }
    }

    public void setRS(boolean ramSelect)
    {
        mRamSelect = ramSelect;
    }

    public int[] getRam(){
        return mRAM;
    }

    public void clock(){
        //Timer
        if(mHighDecrement){
            mTimer--;
        } else {
            mTimerPrescalerCount--;
            if(mTimerPrescalerCount<0){
                mTimerPrescalerCount=mTimerPrescaler;
                mTimer--;
            }
        }
        if(mTimer<0){
            mInstantReadUnderflow=true;
            mTISetUnderflow=true;
            mTimer = 0xFF;
            mHighDecrement=true;
        }
        //System.out.println("TimerReg:"+Integer.toHexString(timerReg)+" timerPrescaller:"+timerPrescaller+" timerPrescallerRegister:"+timerPrescallerCount+" highDecrement:"+highDecrement);
    }
}
