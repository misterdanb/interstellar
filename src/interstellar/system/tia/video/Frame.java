/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.system.tia.video;

/**
 * @author shguro, db1992
 */

public class Frame
{
    private int[] mRawImage;
    private int mWidth, mHeight;

    public Frame(int width, int height)
    {
        mRawImage = new int[width * height];
        mWidth = width;
        mHeight = height;
    }
    
    public int length()
    {
        return mRawImage.length;
    }

    public void clear()
    {
        for (int i = 0; i < mRawImage.length; i++) mRawImage[i] = 0x000000;
    }
    
    public void setPixel(int pos, int color)
    {
        if (pos >= mRawImage.length) return;
        mRawImage[pos] = color;
    }
    
    public void setPixel(int x, int y, int color)
    {
        if (y * mWidth + x >= mRawImage.length) return;
        mRawImage[y * mWidth + x] = color;
    }

    public void setData(int[] data)
    {
        mRawImage = data;
    }

    public int getPixel(int pos)
    {
        if (pos >= mRawImage.length) return 0;
        return mRawImage[pos];
    }

    public int getPixel(int x, int y)
    {
        if (y * mWidth + x >= mRawImage.length) return 0;
        return mRawImage[y * mWidth + x];
    }

    public int[] getData()
    {
        return mRawImage;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public int getHeight()
    {
        return mHeight;
    }
}
