/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro, db1992
 */

package interstellar.system.tia.constants;

public final class Dimensions
{
    public static final int HORIZONTAL_BLANK = 68;
    public static final int HORIZONTAL_PICTURE = 160;
    public static final int HORIZONTAL_TOTAL = 228;

    public static final int VERTICAL_SYNC = 3;
    
    public static final int VERTICAL_PAL_BLANK = 45;
    public static final int VERTICAL_PAL_PICTURE = 228;
    public static final int VERTICAL_PAL_OVERSCAN = 36;

    public static final int VERTICAL_NTSC_BLANK = 37;
    public static final int VERTICAL_NTSC_PICTURE = 192;
    public static final int VERTICAL_NTSC_OVERSCAN = 30;
    
    public static final int VERTICAL_PAL_TOTAL = VERTICAL_SYNC + VERTICAL_PAL_BLANK + VERTICAL_PAL_PICTURE + VERTICAL_PAL_OVERSCAN;
    public static final int VERTICAL_NTSC_TOTAL = VERTICAL_SYNC + VERTICAL_NTSC_BLANK + VERTICAL_NTSC_PICTURE + VERTICAL_NTSC_OVERSCAN;

    public static final int PLAYER_WIDTH = 8;
    public static final int MISSILE_WIDTH = 1;
    public static final int PLAYFIELD_WIDTH = 40;
    public static final int BALL_WIDTH = 1;

    public static final int PLAYER_SINGLE_SIZE = 1;
    public static final int PLAYER_DOUBLE_SIZE = 2;
    public static final int PLAYER_QUAD_SIZE = 4;

    public static final int PLAYER_COPIES_1 = 1;
    public static final int PLAYER_COPIES_2 = 2;
    public static final int PLAYER_COPIES_3 = 3;

    public static final int PLAYER_COPY_SPACING_CLOSE = 1;
    public static final int PLAYER_COPY_SPACING_MEDIUM = 3;
    public static final int PLAYER_COPY_SPACING_WIDE = 7;

    public static final int MISSILE_SINGLE_SIZE = 1;
    public static final int MISSILE_DOUBLE_SIZE = 2;
    public static final int MISSILE_QUAD_SIZE = 4;
    public static final int MISSILE_OCTA_SIZE = 8;

    public static final int MISSILE_CENTERING_OFFSET_SINGLE = 3;
    public static final int MISSILE_CENTERING_OFFSET_DOUBLE = 6;
    public static final int MISSILE_CENTERING_OFFSET_QUAD = 10;

    public static final int BALL_SINGLE_SIZE = 1;
    public static final int BALL_DOUBLE_SIZE = 2;
    public static final int BALL_QUAD_SIZE = 4;
    public static final int BALL_OCTA_SIZE = 8;
}
