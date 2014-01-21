/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

/*
 * Actually it's for the 6502
 */

package interstellar.system.mos6502.constants;

public final class Addresses
{
    /*
     * SOURCE: http://nocash.emubase.de/2k6specs.htm
     */

    // INTERRUPT ADRESSES
    public static final int NMILOW  = 0xFFFA; // wird nicht gebraucht
    public static final int NMIHIGH = 0xFFFB; // wird nicht gebraucht
    public static final int RESLOW  = 0xFFFC; //interstellar.system.constants.cartridges.Addresses.ENTRYLOW;
    public static final int RESHIGH = 0xFFFD; //interstellar.system.constants.cartridges.Addresses.ENTRYHIGH;
    public static final int IRQLOW  = 0xFFFE; //interstellar.system.constants.cartridges.Addresses.BREAKLOW;
    public static final int IRQHIGH = 0xFFFF; //interstellar.system.constants.cartridges.Addresses.BREAKHIGH;
    public static final int BRKLOW  = 0xFFFE; //interstellar.system.constants.cartridges.Addresses.BREAKLOW;
    public static final int BRKHIGH = 0xFFFF; //interstellar.system.constants.cartridges.Addresses.BREAKHIGH;
}