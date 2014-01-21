/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.system.mos6502.disasm;

import java.util.*;

import static interstellar.system.utilities.Conversion.*;
import static interstellar.system.tia.constants.Addresses.*;
import static interstellar.system.mos6532.constants.Addresses.*;
import static interstellar.system.cartridges.constants.Addresses.*;
import static interstellar.system.mos6502.constants.Tables.*;
import interstellar.system.*;

/**
 * @author db1992
 */

public class Disassembler
{
    private Routine m_mainRoutine = new Routine();
    private ArrayList<Routine> mubRoutines = new ArrayList<Routine>();

    public Disassembler()
    {
    }

    public void parse(Console console) throws SystemException
    {
        int iPointer = 0;
        int iEntry = 0;

        iEntry = joinBytes(console.read(interstellar.system.cartridges.constants.Addresses.ENTRYHIGH),
                        console.read(interstellar.system.cartridges.constants.Addresses.ENTRYLOW));

        iPointer = iEntry;

        while (iPointer < 0xFFF4)
        {
            int iInstReg = console.read(iPointer);
            int iOperand = 0x00;
            int iInstLength = 0;

            AddressingMode addressingMode = OPADDRESSING[iInstReg];
            Instruction.InstructionType positionMarker = Instruction.InstructionType.NormalInstruction;

            if (OPNAMES[iInstReg].equals("JSR") ||
                OPNAMES[iInstReg].equals("JMP")) positionMarker = Instruction.InstructionType.JumpInstruction;
            else if (OPNAMES[iInstReg].equals("BPL") ||
                     OPNAMES[iInstReg].equals("BMI") ||
                     OPNAMES[iInstReg].equals("BVC") ||
                     OPNAMES[iInstReg].equals("BVS") ||
                     OPNAMES[iInstReg].equals("BCC") ||
                     OPNAMES[iInstReg].equals("BCS") ||
                     OPNAMES[iInstReg].equals("BNE") ||
                     OPNAMES[iInstReg].equals("BEQ")) positionMarker = Instruction.InstructionType.BranchInstruction;

            switch (addressingMode)
            {
                case Accumulator:
                    iInstLength = 1;
                    break;
                    
                case Absolute:
                    iInstLength = 3;
                    break;

                case AbsoluteX:
                    iInstLength = 3;
                    break;

                case AbsoluteY:
                    iInstLength = 3;
                    break;

                case Immediate:
                    iInstLength = 2;
                    break;

                case Implied:
                    iInstLength = 1;
                    break;

                case Indirect:
                    iInstLength = 3;
                    break;

                case IndirectX:
                    iInstLength = 2;
                    break;

                case IndirectY:
                    iInstLength = 2;
                    break;

                case NotAvailable:
                    iInstLength = 1;
                    break;

                case Relative:
                    iInstLength = 2;
                    break;

                case ZeroPage:
                    iInstLength = 2;
                    break;

                case ZeroPageX:
                    iInstLength = 2;
                    break;

                case ZeroPageY:
                    iInstLength = 2;
                    break;

                default: throw new SystemException("No Adressingmode matches (" + addressingMode + ")");
            }

            if (iInstLength == 1) iOperand = 0x00;
            else if (iInstLength == 2) iOperand = console.read(iPointer + 1);
            else if (iInstLength == 3) iOperand = (console.read(iPointer + 2) << 8) & console.read(iPointer + 1);

            m_mainRoutine.addInstruction(new Instruction(positionMarker, addressingMode, iPointer, iInstReg, iOperand));
            
            iPointer += iInstLength;
        }
    }

    private String getAddressString(int highByte, int lowByte)
    {
        int iAddr = joinBytes(highByte, lowByte);

        switch (iAddr)
        {
            // TIA
            case VSYNC:  return "VSYNC";
            case VBLANK: return "VBLANK";
            case WSYNC:  return "WSYNC";
            case RSYNC:  return "RSYNC";
            case NUSIZ0: return "NUSIZ0";
            case NUSIZ1: return "NUSIZ1";
            case COLUP0: return "COLUP0";
            case COLUP1: return "COLUP1";
            case COLUPF: return "COLUPF";
            case COLUBK: return "COLUBK";
            case CTRLPF: return "CTRLPF";
            case REFP0:  return "REFP0";
            case REFP1:  return "REFP1";
            case PF0:    return "PF0";
            case PF1:    return "PF1";
            case PF2:    return "PF2";
            case RESP0:  return "RESP0";
            case RESP1:  return "RESP1";
            case RESM0:  return "RESM0";
            case RESM1:  return "RESM1";
            case RESBL:  return "RESBL";
            case AUDC0:  return "AUDC0";
            case AUDC1:  return "AUDC1";
            case AUDF0:  return "AUDF0";
            case AUDF1:  return "AUDF1";
            case AUDV0:  return "AUDV0";
            case AUDV1:  return "AUDV1";
            case GRP0:   return "GRP0";
            case GRP1:   return "GRP1";
            case ENAM0:  return "ENAM0";
            case ENAM1:  return "ENAM1";
            case ENABL:  return "ENABL";
            case HMP0:   return "HMP0";
            case HMP1:   return "HMP1";
            case HMM0:   return "HMM0";
            case HMM1:   return "HMM1";
            case HMBL:   return "HMBL";
            case VDELP0: return "VDELP0";
            case VDELP1: return "VDELP1";
            case VDELBL: return "VDELBL";
            case RESMP0: return "RESMP0";
            case RESMP1: return "RESMP1";
            case HMOVE:  return "HMOVE";
            case HMCLR:  return "HMCLR";
            case CXCLR:  return "CXCLR";
            case CXM0P:  return "CXM0P";
            case CXM1P:  return "CXM1P";
            case CXP0FB: return "CXP0FB";
            case CXP1FB: return "CXP1FB";
            case CXM0FB: return "CXM0FB";
            case CXM1FB: return "CXM1FB";
            case CXBLPF: return "CXBLPF";
            case CXPPMM: return "CXPPMM";
            case INPT0:  return "INPT0";
            case INPT1:  return "INPT1";
            case INPT2:  return "INPT2";
            case INPT3:  return "INPT3";
            case INPT4:  return "INPT4";
            case INPT5:  return "INPT5";

            // MOS6532

            // Cartridges
        }

        return "$" + Integer.toHexString(iAddr);
    }

    public String[] getDisAssembly()
    {
        String[] sDisAssembly = new String[m_mainRoutine.length()];

        for (int i = 0; i < sDisAssembly.length; i++)
        {
            Instruction instruction = m_mainRoutine.getInstruction(i);
            sDisAssembly[i] = instruction.toString();
        }

        return sDisAssembly;
    }

    public int[] getInstructionPointers()
    {
        int[] iInstructionPointers = new int[m_mainRoutine.length()];
        
        for (int i = 0; i < iInstructionPointers.length; i++)
        {
            Instruction instruction = m_mainRoutine.getInstruction(i);
            iInstructionPointers[i] = instruction.getInstructionPointer();
        }

        return iInstructionPointers;
    }
}