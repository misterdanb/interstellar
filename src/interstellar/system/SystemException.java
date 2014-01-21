/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro, db1992
 */

package interstellar.system;

import java.lang.Exception;

public class SystemException extends Exception
{
    public SystemException(ConsoleDevice device)
    {
        super(device.getDeviceName() + "-Exception");
    }

    public SystemException(ConsoleDevice device, String exception)
    {
        super(device.getDeviceName() + "-Exception: " + exception);
    }

    public SystemException(String exception)
    {
        super("ConsoleDevice-Exception: " + exception);
    }
}