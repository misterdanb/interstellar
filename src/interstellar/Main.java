/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author shguro
 */

package interstellar;

import interstellar.ui.App;
import javax.swing.*;

public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (UnsupportedLookAndFeelException e) {}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}

        Main program = new Main();
    }

    public Main()
    {
        App myApp = new App();
    }
}