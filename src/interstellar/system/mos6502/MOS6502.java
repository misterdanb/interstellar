/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro, db1992
 */

/**
 * Things TODO:
 * - Check all instruction
 * - Implement illegal instructions
 * - Recheck Interrupts (for 6502 emulation)
 */

package interstellar.system.mos6502;

import static interstellar.system.utilities.Conversion.*;
import static interstellar.system.utilities.Addressing.*;
import static interstellar.system.mos6502.constants.Addresses.*;
import static interstellar.system.mos6502.constants.Tables.*;
import static interstellar.system.mos6502.constants.Interrupts.*;
import static interstellar.system.cartridges.constants.Addresses.*;
import interstellar.system.*;

public class MOS6502 extends ConsoleDevice
{
    // <editor-fold defaultstate="collapsed" desc="members">
    // <editor-fold defaultstate="collapsed" desc="members: 6507 flag">
    private static boolean s_b6507 = true;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: status flags">
    private boolean mN = false;   // Negative-Flag
    private boolean mV = false;   // Overflow-Flag
    private boolean mB = false;   // Break-Flag (break was executed, if set)
    private boolean mD = false;   // BCD-Flag (enables Binary Coded Decimal)
    private boolean mI = s_b6507; // Interrupt-Flag (blocks interrupts, if set) // Standard true, cause 6507
    private boolean mZ = false;   // Zero-Flag (zero value was placed into a register, if set, non-zero value, if not)
    private boolean mC = false;   // Carry-Flag
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: registers">
    private int mA = 0x00;  // Accumulator-Register
    private int mX = 0x00;  // X-Register
    private int mY = 0x00;  // Y-Register
    private int mSP = 0xFF; // Stack-Pointer
    private int mPC = 0x00; // Progam Counter

    // the following program counter
    private int mLastPC = 0x00;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: virtual pins">
    private boolean mNMI = false; // Non-Maskable-Interrupt-"Pin"
    private boolean mRES = false; // RESet-Pin // really this pin for interrupt?
    private boolean mIRQ = false; // Interrupt-ReQuest-"Pin"
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: cycle counting">
    private int mInstructionCycles = 0;
    private boolean mAdditionalCyclesCalculated = false; // You can read as many times as you want, page-crossing will only be calculated one time

    private int mRemainingCycles = 0;
    private int mTotalCycles = 0;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="members: write delay">
    // for writes that go to the common memory
    private int mWriteDelayByte = 0x00;
    private int mWriteDelayAddr = 0x00;
    private boolean mAddressWrite = false;
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods">
    // <editor-fold defaultstate="collapsed" desc="methods: constructor">
    public MOS6502(Console console)
    {
        super(console, "CPU");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: overloaded methods">
    public void initialize()
    {
        reset();
    }

    public void reset()
    {
        mN = false;
        mV = false;
        mB = false;
        mD = false;
        mI = s_b6507;
        mZ = false;
        mC = false;

        mA = 0x00;
        mX = 0x00;
        mY = 0x00;
        mSP = 0xFF;
        mPC = joinBytes(readMemory(ENTRYHIGH), readMemory(ENTRYLOW));
        mLastPC = mPC;

        mNMI = false;
        mRES = false;
        mIRQ = false;

        mInstructionCycles = 0;
        mAdditionalCyclesCalculated = false;
    }

    public int read(int addr) { return 0; }
    public void write(int addr, int data) {}

    @Override
    public String toString()
    {
        return "Device: " + getDeviceName() + "\n" +
               "Registers:\n" +
               "- Status:\n" +
               "-- N: " + getNFlag() + "\n" +
               "-- V: " + getVFlag() + "\n" +
               "-- B: " + getBFlag() + "\n" +
               "-- D: " + getDFlag() + "\n" +
               "-- I: " + getIFlag() + "\n" +
               "-- Z: " + getZFlag() + "\n" +
               "-- C: " + getCFlag() + "\n" +
               "- A: " + Integer.toHexString(getA()) + "\n" +
               "- X: " + Integer.toHexString(getX()) + "\n" +
               "- Y: " + Integer.toHexString(getY()) + "\n" +
               "- SP: " + Integer.toHexString(getSP()) + "\n" +
               "- PC: " + Integer.toHexString(getPC()) + "\n" +
               "- LPC (active PC): " + Integer.toHexString(getLPC()) + "\n" +
               "Active Instruction: " + OPNAMES[readMemory(getLPC())] + "\n";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: accessors">
    // <editor-fold defaultstate="collapsed" desc="methods: accessors: set">
    public void setNFlag(boolean data) { mN = data; }
    public void setVFlag(boolean data) { mV = data; }
    public void setBFlag(boolean data) { mB = data; }
    public void setDFlag(boolean data) { mD = data; }
    public void setIFlag(boolean data) { mI = data; }
    public void setZFlag(boolean data) { mZ = data; }
    public void setCFlag(boolean data) { mC = data; }

    public void setA(int data) { mA = int8(data); }
    public void setX(int data) { mX = int8(data); }
    public void setY(int data) { mY = int8(data); }
    public void setSP(int data) { mSP = int8(data); }
    public void setPC(int data) { mPC = int16(data); }
    public void setLPC(int data) { mLastPC = int16(data); }

    public void setProcessorStatusRegister(int data)
    {
        setNFlag(getBit(data, 7));
        setVFlag(getBit(data, 6));
        setBFlag(getBit(data, 4));
        setDFlag(getBit(data, 3));
        setIFlag(getBit(data, 2));
        setZFlag(getBit(data, 1));
        setCFlag(getBit(data, 0));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="methods: accessors: get">
    public boolean getNFlag() { return mN; }
    public boolean getVFlag() { return mV; }
    public boolean getBFlag() { return mB; }
    public boolean getDFlag() { return mD; }
    public boolean getIFlag() { return mI; }
    public boolean getZFlag() { return mZ; }
    public boolean getCFlag() { return mC; }

    public int getA() { return mA; }
    public int getX() { return mX; }
    public int getY() { return mY; }
    public int getSP() { return mSP; }
    public int getPC() { return mPC; }
    public int getLPC() { return mLastPC; }

    public int getInstructionCycles() { return mInstructionCycles; }
    public int getRemainingCycles() { return mRemainingCycles; }
    public int getTotalCycles() { return mTotalCycles; }
    
    public int getProcessorStatusRegister()
    {
        int iN = getNFlag() ? 0x80 : 0x00;
        int iV = getVFlag() ? 0x40 : 0x00;
        int iB = getBFlag() ? 0x10 : 0x00;
        int iD = getDFlag() ? 0x08 : 0x00;
        int iI = getIFlag() ? 0x04 : 0x00;
        int iZ = getZFlag() ? 0x02 : 0x00;
        int iC = getCFlag() ? 0x01 : 0x00;

        int iResult = iN | iV | iB | iD | iI | iZ | iC | 0x20; // 0x20, becaus the not used bit is always 1

        return iResult;
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor">
    // <editor-fold defaultstate="collapsed" desc="methods: processor: interrupts">
    public void interrupt(InterruptMode mode)
    {
        if (mode == InterruptMode.NMI) mNMI = true;
        else if (mode == InterruptMode.RES) mRES = true;
        else if (mode == InterruptMode.IRQ) mIRQ = true;
    }
    
    private void executeInterrupt(int highByte, int lowByte)
    {
        // 0x0100-0x01FF reserved for Stack
        writeMemory(0x0100 + getSP(), int8(getPC() >> 8), true);
        stackDown();

        writeMemory(0x0100 + getSP(), int8(getPC()), true);
        stackDown();

        writeMemory(0x0100 + getSP(), getProcessorStatusRegister(), true);
        stackDown();

        int iLowByte = readMemory(int16(lowByte));
        int iHighByte = readMemory(int16(highByte));

        setPC(joinBytes(iHighByte, iLowByte));
    }

    private void executeNMI()
    {
        if (!s_b6507 && mNMI)
        {
            executeInterrupt(NMIHIGH, NMILOW); // Not influenced by Interrupt-Disable-Flag, it's NON-maskable
            mNMI = false; // set it false, so interupt in interrupt is allowed ???
        }
    }

    private void executeRES()
    {
        if (mRES)
        {
            if (!s_b6507)
            {
                executeInterrupt(RESHIGH, RESLOW); // Not influenced by Interrupt-Disable-Flag, is it ???
            }
            else
            {
                // checkcheckcheck........
                int iLowByte = readMemory(BREAKLOW);
                int iHighByte = readMemory(BREAKHIGH);

                setPC(joinBytes(iHighByte, iLowByte));
            }
            
            mRES = false; // set it false, so interupt in interrupt is allowed ???
        }
    }
    
    private void executeIRQ()
    {
        if (!s_b6507 && !getIFlag() && mIRQ)
        {
            executeInterrupt(IRQHIGH, IRQLOW); // If Interrupt-Disable-Flag is NOT set, do the interrupt
            mIRQ = false; // set it false, so interupt in interrupt is allowed ???
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: memory access">
    private int readMemory(int addr)
    {
        return getConsole().read(int16(addr));
    }

    private int readAddress()
    {
        AddressingMode addrMode = OPADDRESSING[readMemory(getLPC())];

        switch (addrMode)
        {
            case Absolute: return readAbsolute(readMemory(getLPC() + 2), readMemory(getLPC() + 1));
            case AbsoluteX: return readAbsoluteX(readMemory(getLPC() + 2), readMemory(getLPC() + 1));
            case AbsoluteY: return readAbsoluteY(readMemory(getLPC() + 2), readMemory(getLPC() + 1));
            case Indirect: return readIndirect(readMemory(getLPC() + 2), readMemory(getLPC() + 1));
            case IndirectX: return readIndirectX(readMemory(getLPC() + 1));
            case IndirectY: return readIndirectY(readMemory(getLPC() + 1));
            case ZeroPage: return readZero(readMemory(getLPC() + 1));
            case ZeroPageX: return readZeroX(readMemory(getLPC() + 1));
            case ZeroPageY: return readZeroY(readMemory(getLPC() + 1));
        }

        return 0;
    }

    private int readOperand()
    {
        AddressingMode addrMode = OPADDRESSING[readMemory(getLPC())];

        switch (addrMode)
        {
            case Accumulator: return getA();
            case Relative: return readRelative(readMemory(getLPC() + 1));
            case Immediate: return readImmediate(readMemory(getLPC() + 1));
            default: return readMemory(readAddress());
        }
    }

    private void writeMemory(int addr, int data)
    {
        mWriteDelayByte = data;
        mWriteDelayAddr = addr;
        mAddressWrite = true;
    }

    private void writeMemory(int addr, int data, boolean immediate)
    {
        //if (immediate) getConsole().write(cut16(addr), cut8(data));
        //else writeMemory(addr, data);

        // without delay
        getConsole().write(int16(addr), int8(data));
    }

    private void writeOperand(int data)
    {
        AddressingMode addrMode = OPADDRESSING[readMemory(getLPC())];

        switch (addrMode)
        {
            case Accumulator: setA(int8(data)); break;
            default: writeMemory(readAddress(), int8(data)); break;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: addressing">
    private void calculateAdditionalCycles(int addr1, int addr2, boolean branch)
    {
        if (!mAdditionalCyclesCalculated)
        {
            if (branch)
            {
                mInstructionCycles += 1;
                
                // Page Boundary Crossed ?
                if (!mAdditionalCyclesCalculated &&
                    !isSamePage(addr1, addr2) &&
                    OPPAGECROSSING[readMemory(getLPC())] == PageCrossingMode.BranchPageCrossing) mInstructionCycles += 1;
            }
            else
            {
                // Page Boundary Crossed ?
                if (!mAdditionalCyclesCalculated &&
                    !isSamePage(addr1, addr2) &&
                    OPPAGECROSSING[readMemory(getLPC())] == PageCrossingMode.NormalPageCrossing) mInstructionCycles += 1;
            }
            
            // Calculate them only ONE time
            mAdditionalCyclesCalculated = true;
        }
    }

    private int readAbsolute(int dataHigh, int dataLow)
    {
        int iAddr = joinBytes(dataHigh, dataLow);
        return iAddr;
    }

    private int readAbsoluteX(int dataHigh, int dataLow)
    {
        int iAddr = readAbsolute(dataHigh, dataLow);
        calculateAdditionalCycles(iAddr, iAddr + getX(), false);
        return iAddr + getX();
    }

    private int readAbsoluteY(int dataHigh, int dataLow)
    {
        int iAddr = readAbsolute(dataHigh, dataLow);
        calculateAdditionalCycles(iAddr, iAddr + getY(), false);
        return iAddr + getY();
    }

    private int readImmediate(int data)
    {
        return data;
    }

    private int readIndirect(int dataHigh, int dataLow)
    {
        int iHLAddr = joinBytes(dataHigh, dataLow);
        int iLowByte = readMemory(int16(iHLAddr));
        int iHighByte = readMemory(int16(iHLAddr + 1));
        int iAddr = joinBytes(iHighByte, iLowByte);
        return iAddr;
    }

    private int readIndirectX(int data)
    {
        int iLowByte = readMemory(int8(data + getX()));
        int iHighByte = readMemory(int8(data + getX() + 1));
        int iAddr = joinBytes(iHighByte, iLowByte);
        return iAddr;
    }

    private int readIndirectY(int data)
    {
        int iLowByte = readMemory(int8(data));
        int iHighByte = readMemory(int8(data + 1));
        int iAddr = joinBytes(iHighByte, iLowByte);
        calculateAdditionalCycles(iAddr, iAddr + getY(), false);
        return int16(iAddr + getY());
    }

    private int readRelative(int data)
    {
        int iAddr = int8(data);
        calculateAdditionalCycles(getPC(), getPC() + convertSignedByteToValue(iAddr), true);
        return iAddr;
    }

    private int readZero(int data)
    {
        int iAddr = int8(data);
        return iAddr;
    }

    private int readZeroX(int data)
    {
        int iAddr = readZero(data + getX());
        return iAddr;
    }

    private int readZeroY(int data)
    {
        int iAddr = readZero(data + getY());
        return iAddr;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: emulation">
    public void clock()
    {
        try { executeProcessorClock(); }
        catch (SystemException e) { System.err.println(e.toString()); }
    }
    
    private int nextPC(int lastPC)
    {
        AddressingMode addrMode = OPADDRESSING[readMemory(lastPC)];
        int iNextPC = lastPC;

        switch (addrMode)
        {
            case Accumulator: iNextPC += 1; break;
            case Absolute: iNextPC += 3; break;
            case AbsoluteX: iNextPC += 3; break;
            case AbsoluteY: iNextPC += 3; break;
            case Immediate: iNextPC += 2; break;
            case Implied: iNextPC += 1; break;
            case Indirect: iNextPC += 3; break;
            case IndirectX: iNextPC += 2; break;
            case IndirectY: iNextPC += 2; break;
            case Relative: iNextPC += 2; break;
            case ZeroPage: iNextPC += 2; break;
            case ZeroPageX: iNextPC += 2; break;
            case ZeroPageY: iNextPC += 2; break;
        }

        return iNextPC;
    }
    
    public void executeProcessorClock() throws SystemException
    {
        // Do nothing, if RDY pin is set
        if (getConsole().getTIA().getRDY())
        {
            // write before processor is halted by the tia??
            if (mAddressWrite)
            {
                writeMemory(mWriteDelayAddr, mWriteDelayByte, true);
                mAddressWrite = false;
            }
            
            return; // Processor waits because WSYNC was called
        }

        mTotalCycles++;

        if (mRemainingCycles > 0)
        {
            mRemainingCycles--;
            return;
        }
        
        if (mAddressWrite)
        {
            writeMemory(mWriteDelayAddr, mWriteDelayByte, true);
            mAddressWrite = false;
        }

        executeInstruction();
    }

    public void executeInstruction() throws SystemException
    {
        // TODO CHECK
        setLPC(getPC());
        setPC(nextPC(getLPC()));

        // Won't do anything but RES, if s_b6507 is set true
        executeNMI();
        executeRES();
        executeIRQ();

        int iInstReg = readMemory(getLPC());

        mInstructionCycles = OPCYCLES[iInstReg] - 1; // Set the amount of cycles (minimum)
                                                       // -1, because this method call also counts
        mAdditionalCyclesCalculated = false;

        switch (iInstReg)
        {
            // ILLEGAL INSTRUCTIONS
            // LAX (Load Accumulator and X)
            case 0xA3:
            case 0xA7:
            case 0xB3:
            case 0xAF:
            case 0xB7:
            case 0xBF:
                OP_LAX();
                break;

            // SAX (Store Accumulator and X)
            case 0x87:
            case 0x97:
            case 0x83:
            case 0x8F:
                OP_SAX();
                break;

            // OFFICIAL INSTRUCTIONS
            // ADC (ADd with Carry)
            case 0x69:
            case 0x65:
            case 0x75:
            case 0x6D:
            case 0x7D:
            case 0x79:
            case 0x61:
            case 0x71:
                OP_ADC();
                break;

            // AND (bitwise AND with accumulator)
            case 0x29:
            case 0x25:
            case 0x35:
            case 0x2D:
            case 0x3D:
            case 0x39:
            case 0x21:
            case 0x31:
                OP_AND();
                break;

            // ASL (Arithmetic Shift Left)
            case 0x0A:
            case 0x06:
            case 0x16:
            case 0x0E:
            case 0x1E:
                OP_ASL();
                break;

            // BIT (test BITs)
            case 0x24:
            case 0x2C:
                OP_BIT();
                break;

            // BPL (Branch on PLus)
            case 0x10:
                OP_BPL();
                break;

            // BMI (Branch on MInus)
            case 0x30:
                OP_BMI();
                break;

            // BVC (Branch on oVerflow Clear)
            case 0x50:
                OP_BVC();
                break;

            // BVS (Branch on oVerflow Set)
            case 0x70:
                OP_BVS();
                break;

            // BCC (Branch on Carry Clear)
            case 0x90:
                OP_BCC();
                break;

            // BCS (Branch on Carry Set)
            case 0xB0:
                OP_BCS();
                break;

            // BNE (Branch on Not Equal)
            case 0xD0:
                OP_BNE();
                break;

            // BEQ (Branch on EQual)
            case 0xF0:
                OP_BEQ();
                break;

            // BRK (BReaK)
            case 0x00:
                OP_BRK();
                break;

            // CMP (CoMPare accumulator)
            case 0xC9:
            case 0xC5:
            case 0xD5:
            case 0xCD:
            case 0xDD:
            case 0xD9:
            case 0xC1:
            case 0xD1:
                OP_CMP();
                break;

            // CPX (ComPare X register)
            case 0xE0:
            case 0xE4:
            case 0xEC:
                OP_CPX();
                break;

            // CPY (ComPare Y register)
            case 0xC0:
            case 0xC4:
            case 0xCC:
                OP_CPY();
                break;

            // DEC (DECrement memory)
            case 0xC6:
            case 0xD6:
            case 0xCE:
            case 0xDE:
                OP_DEC();
                break;

            // EOR (bitwise Exclusive OR)
            case 0x49:
            case 0x45:
            case 0x55:
            case 0x4D:
            case 0x5D:
            case 0x59:
            case 0x41:
            case 0x51:
                OP_EOR();
                break;

            // CLC (CLear Carry)
            case 0x18:
                OP_CLC();
                break;

            // SEC (SEt Carry)
            case 0x38:
                OP_SEC();
                break;

            // Brauchen wir das???
            // CLI (CLear Interrupt)
            case 0x58:
                OP_CLI();
                break;

            // Brauchen wir das???
            // SEI (SEt Interrupt)
            case 0x78:
                OP_SEI();
                break;

            // CLV (CLear oVerflow)
            case 0xB8:
                OP_CLV();
                break;

            // CLD (CLear Decimal)
            case 0xD8:
                OP_CLD();
                break;

            // SED (SEt Decimal)
            case 0xF8:
                OP_SED();
                break;

            // INC (INCrement memory)
            case 0xE6:
            case 0xF6:
            case 0xEE:
            case 0xFE:
                OP_INC();
                break;

            // JMP (JuMP)
            case 0x4C:
            case 0x6C:
                OP_JMP();
                break;

            // JSR (Jump to SubRoutine)
            case 0x20:
                OP_JSR();
                break;

            // LDA (LoaD Accumulator)
            case 0xA9:
            case 0xA5:
            case 0xB5:
            case 0xAD:
            case 0xBD:
            case 0xB9:
            case 0xA1:
            case 0xB1:
                OP_LDA();
                break;

            // LDX (LoaD X register)
            case 0xA2:
            case 0xA6:
            case 0xB6:
            case 0xAE:
            case 0xBE:
                OP_LDX();
                break;

            // LDY (LoaD Y register)
            case 0xA0:
            case 0xA4:
            case 0xB4:
            case 0xAC:
            case 0xBC:
                OP_LDY();
                break;

            // LSR (Logical Shift Right)
            case 0x4A:
            case 0x46:
            case 0x56:
            case 0x4E:
            case 0x5E:
                OP_LSR();
                break;

            // NOP (No OPeration)
            case 0xEA:
                OP_NOP();
                break;

            // ORA (bitwiser OR with Accumulator)
            case 0x09:
            case 0x05:
            case 0x15:
            case 0x0D:
            case 0x1D:
            case 0x19:
            case 0x01:
            case 0x11:
                OP_ORA();
                break;

            // TAX (Transfer A to X)
            case 0xAA:
                OP_TAX();
                break;

            // TXA (Transfer X to A)
            case 0x8A:
                OP_TXA();
                break;

            // DEX (DEcrement X)
            case 0xCA:
                OP_DEX();
                break;

            // INX (INcrement X)
            case 0xE8:
                OP_INX();
                break;

            // TAY (Transfer A to Y)
            case 0xA8:
                OP_TAY();
                break;

            // TYA (Transfer Y to A)
            case 0x98:
                OP_TYA();
                break;

            // DEY (DEcrement Y)
            case 0x88:
                OP_DEY();
                break;

            // INY (INcrement Y)
            case 0xC8:
                OP_INY();
                break;

            // ROL (ROtate Left)
            case 0x2A:
            case 0x26:
            case 0x36:
            case 0x2E:
            case 0x3E:
                OP_ROL();
                break;

            // ROR (ROtate Right)
            case 0x6A:
            case 0x66:
            case 0x76:
            case 0x6E:
            case 0x7E:
                OP_ROR();
                break;

            // Brauchcen wir das???
            // RTI (ReTurn from Interrupt)
            case 0x40:
                OP_RTI();
                break;

            // RTS (ReTurn from Subroutine)
            case 0x60:
                OP_RTS();
                break;

            // SBC (SuBtract with Carry)
            case 0xE9:
            case 0xE5:
            case 0xF5:
            case 0xED:
            case 0xFD:
            case 0xF9:
            case 0xE1:
            case 0xF1:
                OP_SBC();
                break;

            // STA (STore Accumulator)
            case 0x85:
            case 0x95:
            case 0x8D:
            case 0x9D:
            case 0x99:
            case 0x81:
            case 0x91:
                OP_STA();
                break;

            // TXS (Transfer X to Stack ptr)
            case 0x9A:
                OP_TXS();
                break;

            // TSX (Transfer Stack ptr to X)
            case 0xBA:
                OP_TSX();
                break;

            // PHA (PusH Accumulator)
            case 0x48:
                OP_PHA();
                break;

            // PLA (PuLl Accumulator)
            case 0x68:
                OP_PLA();
                break;

            // PHP (PusH Processor status)
            case 0x08:
                OP_PHP();
                break;

            // PLP (PuLl Processor status)
            case 0x28:
                OP_PLP();
                break;

            // STX (STore X register)
            case 0x86:
            case 0x96:
            case 0x8E:
                OP_STX();
                break;

            // STY (STore Y register)
            case 0x84:
            case 0x94:
            case 0x8C:
                OP_STY();
                break;
        }

        mRemainingCycles = getInstructionCycles();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: instructions">
    // <editor-fold defaultstate="collapsed" desc="methods: processor: instructions: helper methods">
    private void stackDown() { setSP(getSP() - 1); }
    private void stackUp() { setSP(getSP() + 1); }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: instructions: illegal">
    private void OP_LAX()
    {
        // TODO CHECK
        int iMem = readOperand();

        setZFlag(iMem == 0);
        setNFlag(getBit(iMem, 7));

        setA(iMem);
        setX(iMem);
    }

    private void OP_SAX()
    {
        // TODO CHECK
        writeOperand(getA() & getX());
    }

    // search for more
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods: processor: instructions: official">
    private void OP_ADC()
    {
        // TODO implement decimal mode
        /*int iM = readOperand();

        int iNumberValue = convertSignedToValue(getAReg()) +
                           convertSignedToValue(iM) +
                           (getC() ? 1 : 0);
        int iVal = cut8(convertValueToSigned(iNumberValue));

        setV(getBit(getAReg(), 7) != getBit(iVal, 7));
        setN(getBit(getAReg(), 7));
        setZ(iVal == 0);
        setC(iNumberValue > 255);

        if (getD())
        {
            iNumberValue = convertBCDToValue(getAReg()) +
                           convertBCDToValue(iM) +
                           (getC() ? 1 : 0);
            iVal = cut8(convertValueToBCD(iNumberValue));
            
            setC(iNumberValue > 99);
        }*/

        int iMem = int8(readOperand());
        int iNumVal = getA() + iMem + (getCFlag() ? 1 : 0);
        int iVal = int8(iNumVal);

        setZFlag(iVal == 0);

        if (getDFlag())
        {
        }
        else
        {
            setVFlag(!getBit(getA() ^ iMem, 7) && getBit(getA() ^ iNumVal, 7));
            setCFlag(iNumVal > 0xFF);
            setNFlag(getBit(getA(), 7));
        }

        setA(iVal);
    }

    private void OP_AND()
    {
        int iMem = readOperand();
        int iVal = getA() & iMem;

        setNFlag(getBit(getA(), 7));
        setZFlag(iVal == 0);

        setA(iVal);
    }

    private void OP_ASL()
    {
        int iMem = readOperand();
        int iVal = int8(iMem << 1);

        setCFlag(getBit(iMem, 7));
        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        writeOperand(iVal);
    }

    private void OP_BIT()
    {
        int iMem = readOperand();
        int iVal = getA() & iMem;

        setNFlag(getBit(iVal, 7));
        setVFlag(getBit(iVal, 6));
        setZFlag(iVal == 0);
    }

    private void branch(int operand)
    {
        int iMem = operand;
        setPC(getPC() + convertSignedByteToValue(iMem));
    }

    private void OP_BPL()
    {
        if (!getNFlag()) branch(readOperand());
    }

    private void OP_BMI()
    {
        if (getNFlag()) branch(readOperand());
    }

    private void OP_BVC()
    {
        if (!getVFlag()) branch(readOperand());
    }

    private void OP_BVS()
    {
        if (getVFlag()) branch(readOperand());
    }

    private void OP_BCC()
    {
        if (!getCFlag()) branch(readOperand());
    }

    private void OP_BCS()
    {
        if (getCFlag()) branch(readOperand());
    }

    private void OP_BNE()
    {
        if (!getZFlag()) branch(readOperand());
    }

    private void OP_BEQ()
    {
        if (getZFlag()) branch(readOperand());
    }

    private void OP_BRK()
    {
        // 0x0100-0x01FF reserved for Stack
        int iStack = int16(getPC() + 1);

        int iLow = readMemory(interstellar.system.cartridges.constants.Addresses.BREAKLOW);
        int iHigh = readMemory(interstellar.system.cartridges.constants.Addresses.BREAKHIGH);

        int iMem = joinBytes(iHigh, iLow);
        
        setBFlag(true);

        writeMemory(0x0100 + getSP(), int8(iStack >> 8), true);
        stackDown();
        writeMemory(0x0100 + getSP(), int8(iStack), true);
        stackDown();
        writeMemory(0x0100 + getSP(), getProcessorStatusRegister(), true);
        stackDown();
        
        setIFlag(true);

        setPC(iMem);
    }

    private void OP_CMP()
    {
        int iMem = readOperand();
        int iVal = int8(getA() - iMem);

        setNFlag(getBit(iVal, 7));
        setCFlag(!getBit(getA() - iMem, 8));
        setZFlag(iVal == 0);
    }

    private void OP_CPX()
    {
        int iMem = readOperand();
        int iVal = int8(getX() - iMem);

        setNFlag(getBit(iVal, 7));
        setCFlag(!getBit(getX() - iMem, 8));
        setZFlag(iVal == 0);
    }

    private void OP_CPY()
    {
        int iMem = readOperand();
        int iVal = int8(getY() - iMem);

        setNFlag(getBit(iVal, 7));
        setCFlag(!getBit(getY() - iMem, 8));
        setZFlag(iVal == 0);
    }

    private void OP_DEC()
    {
        int iMem = readOperand();
        int iVal = int8(iMem - 1);

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);
        
        writeOperand(iVal);
    }

    private void OP_EOR()
    {
        int iMem = readOperand();
        int iVal = getA() ^ iMem;

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setA(iVal);
    }

    private void OP_CLC()
    {
        // Clears the Carry-Flag
        setCFlag(false);
    }

    private void OP_SEC()
    {
        // Sets the Carry-Flag
        setCFlag(true);
    }

    private void OP_CLI()
    {
        // Clears the Interrupt-Flag
        if (!s_b6507) setIFlag(false);
    }

    private void OP_SEI()
    {
        // Sets the Interrupt-Flag
        setIFlag(true);
    }

    private void OP_CLV()
    {
        // Clears the Overflow-Flag
        setVFlag(false);
    }

    private void OP_CLD()
    {
        // Clears the BCD-Flag
        setDFlag(false);
    }

    private void OP_SED()
    {
        // Sets the BCD-Flag
        setDFlag(true);
    }

    private void OP_INC()
    {
        int iMem = readOperand();
        int iVal = int8(iMem + 1); // should be 0 if greater than 255 ??? recheck

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);
    }

    private void OP_JMP()
    {
        int iMem = readAddress(); // seems that JSR and JMP both read addresses to jump
        setPC(iMem);
    }

    private void OP_JSR()
    {
        int iMem = readAddress();

        // Stack + Absolute Adressing from 0x0100-0x01FF
        writeMemory(0x0100 + getSP(), int8((getPC() - 1) >> 8), true);
        stackDown();
        writeMemory(0x0100 + getSP(), int8(getPC() - 1), true);
        stackDown();

        setPC(iMem);
    }

    private void OP_LDA()
    {
        int iMem = readOperand();

        setNFlag(getBit(iMem, 7));
        setZFlag(iMem == 0);

        setA(iMem);
    }

    private void OP_LDX()
    {
        int iMem = readOperand();

        setNFlag(getBit(iMem, 7));
        setZFlag(iMem == 0);

        setX(iMem);
    }

    private void OP_LDY()
    {
        int iMem = readOperand();

        setNFlag(getBit(iMem, 7));
        setZFlag(iMem == 0);

        setY(iMem);
    }

    private void OP_LSR()
    {
        int iMem = readOperand();
        int iVal = (iMem >> 1) & 0x7F; // oder 0xFF, gibt normal keinen Fehler

        setCFlag(getBit(iMem, 0));
        setZFlag(iVal == 0);
        setNFlag((iVal & 0x80) != 0); // check this

        writeOperand(iVal);
    }

    private void OP_NOP()
    {
    }

    private void OP_ORA()
    {
        int iMem = readOperand();
        int iVal = getA() | iMem;

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setA(iVal);
    }

    private void OP_TAX()
    {
        setNFlag(getBit(getA(), 7));
        setZFlag(getA() == 0);

        setX(getA());
    }

    private void OP_TXA()
    {
        setNFlag(getBit(getX(), 7));
        setZFlag(getX() == 0);

        setA(getX());
    }

    private void OP_DEX()
    {
        int iMem = getX();
        int iVal = int8(iMem - 1);

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setX(iVal);
    }

    private void OP_INX()
    {
        int iMem = getX();
        int iVal = int8(iMem + 1);

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setX(iVal);
    }

    private void OP_TAY()
    {
        setNFlag(getBit(getA(), 7));
        setZFlag(getA() == 0);

        setY(getA());
    }

    private void OP_TYA()
    {
        setNFlag(getBit(getY(), 7));
        setZFlag(getY() == 0);

        setA(getY());
    }

    private void OP_DEY()
    {
        int iMem = getY();
        int iVal = int8(iMem - 1);

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setY(iVal);
    }

    private void OP_INY()
    {
        int iMem = getY();
        int iVal = int8(iMem + 1);

        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        setY(iVal);
    }

    private void OP_ROL()
    {
        int iMem = readOperand();
        int iVal = int8((iMem << 1) | (getCFlag() ? 0x01 : 0x00));

        setCFlag(getBit(iMem, 7));
        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        writeOperand(iVal);
    }

    private void OP_ROR()
    {
        int iMem = readOperand();
        int iVal = int8((iMem >> 1) | (getCFlag() ? 0x01 : 0x00)); // changed getBit(iMem, 0) to mC, because he
                                                             // checks with the old value of carry (carry isn't)
                                                             // set yet

        setCFlag(getBit(iMem, 0));
        setNFlag(getBit(iVal, 7));
        setZFlag(iVal == 0);

        writeOperand(iVal);
    }

    private void OP_RTI()
    {
        stackUp();
        setProcessorStatusRegister(readMemory(0x0100 + getSP()));

        stackUp();
        int iLow = readMemory(0x0100 + getSP());
        stackUp();
        int iHigh = readMemory(0x0100 + getSP());

        int iMem = joinBytes(iHigh, iLow);

        setPC(iMem);
    }

    private void OP_RTS()
    {
        stackUp();
        int iLow = readMemory(0x0100 + getSP());
        stackUp();
        int iHigh = readMemory(0x0100 + getSP());

        int iMem = int16(joinBytes(iHigh, iLow) + 1);

        setPC(iMem);
    }

    private void OP_SBC()
    {
        // TODO implement decimal mode
        /*int iMem = readOperand();

        int iNumberValue = convertSignedToValue(getAReg()) -
                           convertSignedToValue(iMem) -
                           (!getC() ? 1 : 0);
        int iVal = cut8(convertValueToSigned(iNumberValue));

        setV(getBit(getAReg(), 7) != getBit(iVal, 7));

        if (getD())
        {
            iNumberValue = convertBCDToValue(getAReg()) -
                           convertBCDToValue(iMem) -
                           (!getC() ? 1 : 0);
            iVal = cut8(convertValueToBCD(iNumberValue));
            
            setV(iNumberValue > 99 || iNumberValue < 0);
        }

        setN(getBit(iVal, 7));
        setZ(iNumberValue == 0);
        setC(iNumberValue >= 0);

        setAReg(iVal);*/

        int iMem = readOperand();
        int iNumVal = getA() - iMem  - (!getCFlag() ? 1 : 0);
        //int iVal = cut8(iNumVal);
        int iVal = iNumVal < 0 ? setBit(iNumVal * (-1), 7, true) : int8(iNumVal); // necessary?
        if (iNumVal < 0 && getBit(iNumVal, 7)) iVal = int8(iNumVal * (-1));

        setZFlag(iVal == 0);

        if (getDFlag())
        {
        }
        else
        {
            setVFlag(getBit(getA() ^ iMem, 7) && getBit(getA() ^ iNumVal, 7));
            setCFlag(iNumVal >= 0);
            setNFlag(getBit(iVal, 7)); // because the numeric value wont have this bit set
        }

        setA(iVal);
    }

    private void OP_STA()
    {
        writeOperand(getA());
    }

    private void OP_TXS()
    {
        setSP(getX());
    }

    private void OP_TSX()
    {
        setNFlag(getBit(getSP(), 7));
        setZFlag(getSP() == 0);

        setX(getSP());
    }

    private void OP_PHA()
    {
        writeMemory(0x0100 + getSP(), getA());
        stackDown();
    }

    private void OP_PLA()
    {
        stackUp();
        int iMem = readMemory(0x0100 + getSP());

        setNFlag(getBit(iMem, 7));
        setZFlag(iMem == 0);

        setA(iMem);
    }

    private void OP_PHP()
    {
        writeMemory(0x0100 + getSP(), getProcessorStatusRegister());
        stackDown();
    }

    private void OP_PLP()
    {
        stackUp();
        int iMem = readMemory(0x0100 + getSP());
        
        setProcessorStatusRegister(iMem);
    }

    private void OP_STX()
    {
        writeOperand(getX());
    }

    private void OP_STY()
    {
        writeOperand(getY());
    }
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>
    // </editor-fold>
}
