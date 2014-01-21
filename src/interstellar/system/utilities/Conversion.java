/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Daniel
 */

package interstellar.system.utilities;

public final class Conversion
{
    public static final boolean getBit(int data, int bit)
    {
        return (data & (0x01 << bit)) != 0;
    }

    public static final int setBit(int data, int bit, boolean value)
    {
        /*int iResult = inByte >> bit;

        if (getBit(inByte, bit) != value)
        {
            if (value) iResult += 0x01;
            else iResult -= 0x01;
        }

        return iResult << bit;*/
        return value ? data | (0x01 << bit) : (data & ~(0x01 << bit));
    }
    
    public static final int int4(int data)
    {
        return data & 0xF;
    }

    public static final int int8(int data)
    {
        return data & 0xFF;
    }

    public static final int int16(int data)
    {
        return data & 0xFFFF;
    }

    public static final int joinBytes(int dataHigh, int dataLow)
    {
        return (int8(dataHigh) << 8) | int8(dataLow);
    }

    public static final int convertBCDToValue(int data)
    {
        int iHigh = (data >> 4) & 0x0F;
        int iLow = data & 0x0F;

        return iHigh * 10 + iLow;
    }

    public static final int convertValueToBCD(int data)
    {
        int iHigh = (data / 10) & 0x0F;
        int iLow = (data - iHigh) & 0x0F;

        return (iHigh << 4) | iLow;
    }

    public static final int convertSignedByteToValue(int data)
    {
        return (data & 0x7F) - (getBit(data, 7) ? 128 : 0);
    }

    public static final int convertValueToSignedByte(int data)
    {
        return data < 0 ? (data - 0x80) | 0x80 : data & 0x7F;
    }

    public static final int convertHighBitsToSigned(int data)
    {
        return (getHighBits(data) & 0x07) - (getBit(getHighBits(data), 3) ? 8 : 0);
    }

    public static final int convertLowBitsToSigned(int data)
    {
        return (getLowBits(data) & 0x07) - (getBit(getLowBits(data), 3) ? 8 : 0);
    }

    public static final int getHighBits(int data)
    {
        return (data >> 4) & 0x0F;
    }

    public static final int getLowBits(int data)
    {
        return data & 0x0F;
    }
}
