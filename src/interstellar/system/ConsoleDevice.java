/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interstellar.system;

/**
 * @author db1992
 */

public abstract class ConsoleDevice
{
    private Console m_parentConsole = null;
    private String mDeviceName = "NullDevice";

    public ConsoleDevice(Console console, String deviceName)
    {
        m_parentConsole = console;
        mDeviceName = deviceName;
    }

    // more effective implementation needed
    //public abstract void load(byte[] data);
    //public abstract byte[] save();

    public abstract void initialize();
    public abstract void reset();

    public void setConsole(Console console) { m_parentConsole = console; }
    
    public Console getConsole() { return m_parentConsole; }
    public String getDeviceName() { return mDeviceName; }
    
    @Override
    public String toString() { return "Device: " + mDeviceName; }
}
