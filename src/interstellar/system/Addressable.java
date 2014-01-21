package interstellar.system;

/**
 * @author shguro, daniel
 */
public interface Addressable
{
    public abstract int read(int addr);
    public abstract void write(int addr, int data);
}
