/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro, db1992
 */

package interstellar.system.tia.video;

import static interstellar.system.tia.constants.Colors.*;
import static interstellar.system.tia.constants.Dimensions.*;
import static interstellar.system.tia.constants.Video.*;

import interstellar.system.*;

public class Video
{
    private int mPALHeight = VERTICAL_PAL_PICTURE;
    private int mSECAMHeight = VERTICAL_PAL_PICTURE;
    private int mNTSCHeight = VERTICAL_NTSC_PICTURE;

    private VideoAdapter m_videoAdapter;
    private Frame mrame;
    private VideoMode m_videoMode;
    
    public Video()
    {
        m_videoAdapter = null;
        setVideoMode(VideoMode.NTSC);
    }

    public void registerAdapter(VideoAdapter videoAdapter)
    {
        m_videoAdapter = videoAdapter;
    }

    public void beginFrame()
    {
        // if cleared at the beginning of each frame, the picture stays black... why?
        mrame.clear();
    }

    public void endFrame()
    {
        if (m_videoAdapter != null) m_videoAdapter.draw(mrame);
    }

    public void drawPixel(int x, int y, int color)
    {
        mrame.setPixel(x, y, color);
    }

    public void drawPixel(int x, int y, int colorIndex, int luminanceIndex) throws SystemException
    {
        mrame.setPixel(x, y, getColorByVideoMode(m_videoMode, colorIndex, luminanceIndex));
    }

    public void setVideoMode(VideoMode mode)
    {
        m_videoMode = mode;

        mrame = new Frame(getWidth(), getHeight());
        mrame.clear();
    }

    public void setVisibleHeight(VideoMode mode, int height)
    {
        if (mode == VideoMode.PAL) mPALHeight = height;
        else if (mode == VideoMode.SECAM) mSECAMHeight = height;
        else if (mode == VideoMode.NTSC) mNTSCHeight = height;

        mrame = new Frame(getWidth(), getHeight());
        mrame.clear();
    }

    public VideoMode getVideoMode()
    {
        return m_videoMode;
    }

    public Frame getLiveFrame()
    {
        return mrame;
    }

    private int getWidth()
    {
        return HORIZONTAL_PICTURE;
    }

    private int getHeight()
    {
        if (m_videoMode == VideoMode.PAL) return mPALHeight;
        else if (m_videoMode == VideoMode.NTSC) return mNTSCHeight; // + 5 is fast, + 6 reaaally slow xDD
        else return 0;
        /*if (m_videoMode == VideoMode.PAL) return Dimensions.VERTICAL_PAL_TOTAL;
        else if (m_videoMode == VideoMode.NTSC) return Dimensions.VERTICAL_NTSC_TOTAL;
        else return 0;*/
    }

}