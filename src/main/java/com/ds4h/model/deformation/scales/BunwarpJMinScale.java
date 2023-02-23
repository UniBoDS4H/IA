package com.ds4h.model.deformation.scales;

public enum BunwarpJMinScale {
    VERY_COARSE("Very Coarse", 0),
    COARSE("Coarse", 1),
    FINE("Fine", 2),
    VERY_FINE("Very Fine", 3);
    private String scale;
    private int valueScale;
    private BunwarpJMinScale(final String scale, final int valueScale){
        this.scale = scale;
        this.valueScale = valueScale;
    }

    public int getValue(){
        return this.valueScale;
    }

    public String toString(){
        return this.scale;
    }
}
