/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Daniel
 */

package interstellar.system.utilities;

public final class Addressing
{
    public static final boolean isSamePage(int addr1, int addr2)
    {
        return (addr1 & 0xFF00) == (addr2 & 0xFF00);
    }
}
