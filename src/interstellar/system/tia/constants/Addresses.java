/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

package interstellar.system.tia.constants;

public final class Addresses
{
    /*
     * SOURCE: http://nocash.emubase.de/2k6specs.htm
     */

    // TIA WRITE ONLY ADDRESSES (from outside the TIA of course)
    public static final int VSYNC  = 0x00; // ......1.  vertical sync set-clear
    public static final int VBLANK = 0x01; // 11....1.  vertical blank set-clear
    public static final int WSYNC  = 0x02; // <strobe>  wait for leading edge of horizontal blank
    public static final int RSYNC  = 0x03; // <strobe>  reset horizontal sync counter
    public static final int NUSIZ0 = 0x04; // ..111111  number-size player-missile 0
    public static final int NUSIZ1 = 0x05; // ..111111  number-size player-missile 1
    public static final int COLUP0 = 0x06; // 1111111.  color-lum player 0 and missile 0
    public static final int COLUP1 = 0x07; // 1111111.  color-lum player 1 and missile 1
    public static final int COLUPF = 0x08; // 1111111.  color-lum playfield and ball
    public static final int COLUBK = 0x09; // 1111111.  color-lum background
    public static final int CTRLPF = 0x0A; // ..11.111  control playfield ball size & collisions
    public static final int REFP0  = 0x0B; // ....1...  reflect player 0
    public static final int REFP1  = 0x0C; // ....1...  reflect player 1
    public static final int PF0    = 0x0D; // 1111....  playfield register byte 0
    public static final int PF1    = 0x0E; // 11111111  playfield register byte 1
    public static final int PF2    = 0x0F; // 11111111  playfield register byte 2
    public static final int RESP0  = 0x10; // <strobe>  reset player 0
    public static final int RESP1  = 0x11; // <strobe>  reset player 1
    public static final int RESM0  = 0x12; // <strobe>  reset missile 0
    public static final int RESM1  = 0x13; // <strobe>  reset missile 1
    public static final int RESBL  = 0x14; // <strobe>  reset ball
    public static final int AUDC0  = 0x15; // ....1111  audio control 0
    public static final int AUDC1  = 0x16; // ....1111  audio control 1
    public static final int AUDF0  = 0x17; // ...11111  audio frequency 0
    public static final int AUDF1  = 0x18; // ...11111  audio frequency 1
    public static final int AUDV0  = 0x19; // ....1111  audio volume 0
    public static final int AUDV1  = 0x1A; // ....1111  audio volume 1
    public static final int GRP0   = 0x1B; // 11111111  graphics player 0
    public static final int GRP1   = 0x1C; // 11111111  graphics player 1
    public static final int ENAM0  = 0x1D; // ......1.  graphics (enable) missile 0
    public static final int ENAM1  = 0x1E; // ......1.  graphics (enable) missile 1
    public static final int ENABL  = 0x1F; // ......1.  graphics (enable) ball
    public static final int HMP0   = 0x20; // 1111....  horizontal motion player 0
    public static final int HMP1   = 0x21; // 1111....  horizontal motion player 1
    public static final int HMM0   = 0x22; // 1111....  horizontal motion missile 0
    public static final int HMM1   = 0x23; // 1111....  horizontal motion missile 1
    public static final int HMBL   = 0x24; // 1111....  horizontal motion ball
    public static final int VDELP0 = 0x25; // .......1  vertical delay player 0
    public static final int VDELP1 = 0x26; // .......1  vertical delay player 1
    public static final int VDELBL = 0x27; // .......1  vertical delay ball
    public static final int RESMP0 = 0x28; // ......1.  reset missile 0 to player 0
    public static final int RESMP1 = 0x29; // ......1.  reset missile 1 to player 1
    public static final int HMOVE  = 0x2A; // <strobe>  apply horizontal motion
    public static final int HMCLR  = 0x2B; // <strobe>  clear horizontal motion registers
    public static final int CXCLR  = 0x2C; // <strobe>  clear collision latches

    // TIA READ ONLY ADDRESSES (from outside the TIA of course)
    public static final int CXM0P  = 0x30; // read collision M0-P1, M0-P0 (Bit 7,6)
    public static final int CXM1P  = 0x31; // read collision M1-P0, M1-P1
    public static final int CXP0FB = 0x32; // read collision P0-PF, P0-BL
    public static final int CXP1FB = 0x33; // read collision P1-PF, P1-BL
    public static final int CXM0FB = 0x34; // read collision M0-PF, M0-BL
    public static final int CXM1FB = 0x35; // read collision M1-PF, M1-BL
    public static final int CXBLPF = 0x36; // read collision BL-PF, unused
    public static final int CXPPMM = 0x37; // read collision P0-P1, M0-M1
    public static final int INPT0  = 0x38; // read pot port
    public static final int INPT1  = 0x39; // read pot port
    public static final int INPT2  = 0x3A; // read pot port
    public static final int INPT3  = 0x3B; // read pot port
    public static final int INPT4  = 0x3C; // read input
    public static final int INPT5  = 0x3D; // read input
}