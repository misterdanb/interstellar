/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

package interstellar.system.mos6532.constants;

public final class Addresses
{
    /*
     * SOURCE: http://nocash.emubase.de/2k6specs.htm
     */

    // MOS6532 READ AND WRITE ADDRESSES (from outside the MOS6532 of course)
    //All Addresses without CS and RS
    public static final int RAMBEGIN = 0x0000; // 11111111  128 bytes RAM (in PIA chip) for variables and stack
    public static final int RAMEND   = 0x007F; // (byte at 0x00FF inclusive)
    public static final int SWCHA  = 0x00; // 11111111  Port A; input or output  (read or write)
    public static final int SWACNT = 0x01; // 11111111  Port A DDR, 0= input, 1=output
    public static final int SWCHB  = 0x02; // 11111111  Port B; console switches (read only)
    public static final int SWBCNT = 0x03; // 11111111  Port B DDR (hardwired as input)
    public static final int INTIM  = 0x04; // 11111111  Timer output (read only)
    public static final int INSTAT = 0x05; // 11......  Timer Status (read only, undocumented)
    public static final int TIM1T  = 0x14; // 11111111  set 1 clock interval (838 nsec/interval)
    public static final int TIM8T  = 0x15; // 11111111  set 8 clock interval (6.7 usec/interval)
    public static final int TIM64T = 0x16; // 11111111  set 64 clock interval (53.6 usec/interval)
    public static final int T1024T = 0x17; // 11111111  set 1024 clock interval (858.2 usec/interval)
}
