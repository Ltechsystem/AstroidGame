package dk.sdu.mmmi.cbse.common.data;

public class GameKeys {

    public static final int UP    = 0;
    public static final int LEFT  = 1;
    public static final int RIGHT = 2;
    public static final int SPACE = 3;
    private static final int NUM_KEYS = 4;

    private final boolean[] keys  = new boolean[NUM_KEYS];
    private final boolean[] pkeys = new boolean[NUM_KEYS];

    public void update() {
        System.arraycopy(keys, 0, pkeys, 0, NUM_KEYS);
    }

    public void setKey(int k, boolean b) {
        keys[k] = b;
    }

    public boolean isDown(int k) {
        return keys[k];
    }

    public boolean isPressed(int k) {
        return keys[k] && !pkeys[k];
    }
}