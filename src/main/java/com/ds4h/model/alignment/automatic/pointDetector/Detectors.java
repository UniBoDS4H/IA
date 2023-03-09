package com.ds4h.model.alignment.automatic.pointDetector;

public enum Detectors {
    BRISK("BRISK"),
    KAZE("KAZE"),
    SURF("SURF");

    private final String name;

    Detectors(final String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
