package com.ds4h.view.bunwarpjGUI;

public enum BunwarpJMaxScale {
    VERY_COARSE("Very Coarse", 0),
    COARSE("Coarse", 1),
    FINE("Fine", 2),
    VERY_FINE("Very Fine", 3),
    SUPER_FINE("Super Fine", 4);
    private String scale;
    private int valueScale;
    private BunwarpJMaxScale(final String scale, final int valueScale){
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
