//Stub Cardridge

package interstellar.system.cartridges;

import interstellar.system.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Cartridge extends ConsoleDevice implements Addressable {
    int data;
    int romArray[];
    int romSize;
    File rom;
    DataInputStream inputStream;
    FileInputStream fileStream;

    public Cartridge(Console parentConsole){
        super(parentConsole, "Cartidge");
    }

    public void initialize(){
    }

    public void reset(){
    }

    public int read(int addr){
        switch(romSize){
            case 2048:
                addr = addr & 2047;
                data = romArray[addr];
                break;
            case 4096:
                addr = addr & 4095;
                data = romArray[addr];
                break;
        }
        return data;
    }

    public void write(int addr, int data){
    }

    public void setCartridge(File rom){
        this.rom = rom;
        romSize = (int) rom.length();
        romArray = new int[romSize];
        try {
            inputStream = new DataInputStream(new FileInputStream(rom));
        } catch (FileNotFoundException fne){
            fne.printStackTrace();
        }
        switch(romSize){
            case 2048:
                romArray = new int[2048];
                for(int i = 0; i < 2047; i++){
                    try{
                        romArray[i] = inputStream.read();
                    } catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
                break;
            case 4096:
                for(int i = 0; i < 4095; i++){
                    try{
                        romArray[i] = inputStream.read();
                    } catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }

                break;
        }
    }
}
