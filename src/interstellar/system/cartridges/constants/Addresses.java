/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

package interstellar.system.cartridges.constants;

public final class Addresses
{
    /*
     * SOURCE: http://nocash.emubase.de/2k6specs.htm
     */

    // CARTRIDGE MEMORY
    public static final int ROMBEGIN     = 0xF000; // 11111111  Cartridge ROM (4 Kbytes max)
    public static final int ROMEND       = 0xFFFF; // (byte at 0xFFFF inclusive)
    public static final int RAMW128BEGIN = 0xF000; // 11111111  Cartridge RAM Write (optional 128 bytes)
    public static final int RAMW128END   = 0xF07F; // (byte at 0xF07F inclusive)
    public static final int RAMW256BEGIN = 0xF000; // 11111111  Cartridge RAM Write (optional 256 bytes)
    public static final int RAMW256END   = 0xF0FF; // (byte at 0xF0FF inclusive)
    public static final int RAMR128BEGIN = 0xF080; // 11111111  Cartridge RAM Read (optional 128 bytes)
    public static final int RAMR128END   = 0xF0FF; // (byte at 0xF0FF inclusive)
    public static final int RAMR256BEGIN = 0xF100; // 11111111  Cartridge RAM Read (optional 256 bytes)
    public static final int RAMR256END   = 0xF1FF; // (byte at 0xF1FF inclusive)
    public static final int BANK8K       = 0x003F; // ......11  Cart Bank Switching (for some 8K ROMs, 4x2K)
    public static final int BANKBEGIN    = 0xFFF4; // <strobe>  Cart Bank Switching (for ROMs greater 4K)
    public static final int BANKEND      = 0xFFFB; // (byte at 0xFFFB inclusive)
    public static final int ENTRYLOW     = 0xFFFC; // 11111111  Cart Entrypoint (16bit pointer)
    public static final int ENTRYHIGH    = 0xFFFD; // (byte at 0xFFFD inclusive)
    public static final int BREAKLOW     = 0xFFFE; // 11111111  Cart Breakpoint (16bit pointer)
    public static final int BREAKHIGH    = 0xFFFF; // (byte at 0xFFFF inclusive)
}
