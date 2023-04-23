package com.ds4h.model.deformation.scales;

public enum BunwarpJMaxScale {
    VERY_COARSE("Very Coarse", 0),
    COARSE("Coarse", 1),
    FINE("Fine", 2),
    VERY_FINE("Very Fine", 3),
    SUPER_FINE("Super Fine", 4);
    private final String scale;
    private final int valueScale;
    BunwarpJMaxScale(final String scale, final int valueScale){
        this.scale = scale;
        this.valueScale = valueScale;
    }

    /**
     * Returns the value.
     * @return the value
     */
    public int getValue(){
        return this.valueScale;
    }

    /**
     * Returns the scale name.
     * @return the scale name.
     */
    public String toString(){
        return this.scale;
    }
}
