package com.ds4h.model.deformation.scales;

public enum BunwarpJMode {

    FAST_MODE("FAST", 0),
    ACCURATE("ACCURATE", 1),
    MONO("MONO", 2);

    private final String mode;
    private final int valMode;
    private BunwarpJMode(final String mode, final int valMode){
        this.mode = mode;
        this.valMode = valMode;
    }

    /**
     * Returns the value mode.
     * @return the value mode.
     */
    public int getValue(){
        return this.valMode;
    }

    /**
     * Returns the mode name.
     * @return the mode name.
     */
    public String toString(){
        return this.mode;
    }
}
