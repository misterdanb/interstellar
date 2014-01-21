/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.system.mos6502.disasm;
import java.util.*;

/**
 * @author db1992
 */

public class Routine
{
    private ArrayList<Instruction> mnstructions = new ArrayList<Instruction>();

    public Routine()
    {
    }

    public void addInstruction(Instruction instruction) { mnstructions.add(instruction); }
    public void removeInstruction(Instruction instruction) { mnstructions.remove(instruction); }

    public Instruction getInstruction(int index) { return mnstructions.get(index); }

    public int length() { return mnstructions.size(); }
}
