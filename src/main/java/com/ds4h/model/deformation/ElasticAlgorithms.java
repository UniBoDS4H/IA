package com.ds4h.model.deformation;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.deformation.elastic.ElasticRegistration;
import com.ds4h.model.deformation.elastic.ElasticRegistrationImpl;

public enum ElasticAlgorithms {
    BUNWARPJ("BunwarpJ", new BunwarpjDeformation()),
    BIGWARP("BigWarp", new ElasticRegistrationImpl());
    private final String name;
    private final ElasticRegistration algorithm;
    ElasticAlgorithms(@NotNull final String name, @NotNull final ElasticRegistration registration) {
        this.name = name;
        this.algorithm = registration;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public ElasticRegistration getAlgorithm() {
        return this.algorithm;
    }
}
