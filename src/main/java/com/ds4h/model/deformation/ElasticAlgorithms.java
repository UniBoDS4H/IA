package com.ds4h.model.deformation;

import com.drew.lang.annotations.NotNull;

public enum ElasticAlgorithms {
    BUNWARPJ("BunwarpJ"),
    BIGWARP("BigWarp");
    private final String name;
    ElasticAlgorithms(@NotNull final String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return this.name;
    }
}
