/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.system.mos6502.disasm;

import static interstellar.system.utilities.Conversion.*;
import static interstellar.system.mos6502.constants.Tables.*;

/**
 * @author db1992
 */

public class Instruction
{
    public static enum InstructionType
    {
        NormalInstruction,
        JumpInstruction,
        BranchInstruction,
        SubRoutineBegin,
        SubRoutineEnd;
    };

    private InstructionType m_positionMarker = InstructionType.NormalInstruction;
    private AddressingMode m_addressingMode = AddressingMode.ZeroPage;
    private int mInstructionPointer = 0x0000;
    private int mInstruction = 0x00;
    private int mOperand = 0x00;

    public Instruction()
    {
    }

    public Instruction(InstructionType positionMarker, AddressingMode addressingMode, int instructionPointer, int instruction, int operand)
    {
        m_positionMarker = positionMarker;
        m_addressingMode = addressingMode;
        mInstructionPointer = instructionPointer;
        mInstruction = instruction;
        mOperand = operand;
    }

    public void setPositionMarker(InstructionType positionMarker) { m_positionMarker = positionMarker; }
    public void setAddressingMode(AddressingMode addressingMode) { m_addressingMode = addressingMode; }
    public void setInstructionPointer(int instructionPointer) { mInstructionPointer = int16(instructionPointer); }
    public void setInstruction(int instruction) { mInstruction = int8(instruction); }
    public void setOperand(int operand) { mInstruction = int16(operand); }

    public InstructionType getPositionMarker() { return m_positionMarker; }
    public AddressingMode getAddressingMode() { return m_addressingMode; }
    public int getInstructionPointer() { return mInstructionPointer; }
    public int getInstruction() { return mInstruction; }
    public int getOperand() { return mOperand; }

    public String toString()
    {
        String sPointer = Integer.toHexString(mInstructionPointer);
        String sInstruction = OPNAMES[mInstruction];
        String sOperand = "";

        switch (m_addressingMode)
        {
            case Accumulator:
                sOperand = "A";
                break;

            case Absolute:
                sOperand = "$" + Integer.toHexString(mOperand);
                break;

            case AbsoluteX:
                sOperand = "$" + Integer.toHexString(mOperand) + ",X";
                break;

            case AbsoluteY:
                sOperand = "$" + Integer.toHexString(mOperand) + ",Y";
                break;

            case Immediate:
                sOperand = "#" + Integer.toHexString(mOperand);
                break;

            case Implied:
                sOperand = "";
                break;

            case Indirect:
                sOperand = "$" + Integer.toHexString(mOperand);
                break;

            case IndirectX:
                sOperand = "($" + Integer.toHexString(mOperand) + ",X)";
                break;

            case IndirectY:
                sOperand = "/$" + Integer.toHexString(mOperand) + "),Y";
                break;

            case NotAvailable:
                sOperand = "N/A";
                break;

            case Relative:
                sOperand = "$" + Integer.toHexString(mOperand);
                break;

            case ZeroPage:
                sOperand = "$" + Integer.toHexString(mOperand);
                break;

            case ZeroPageX:
                sOperand = "$" + Integer.toHexString(mOperand) + ",X";
                break;

            case ZeroPageY:
                sOperand = "$" + Integer.toHexString(mOperand) + ",Y";
                break;

            default:
                sOperand = "N/A";
                break;
        }

        return sPointer.toUpperCase() + "| " + sInstruction.toUpperCase() + " " + sOperand.toUpperCase();
    }
}
