package com.ds4h.controller.elastic;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticController {
    public ElasticController() {}

    public CompletableFuture<List<AlignedImage>> automaticElasticRegistration(@NotNull final AnalyzableImage targetImage,
                                                                              @NotNull final List<AnalyzableImage> movingImages,
                                                                              @NotNull final PointDetector detector) {
        return null;
    }

    public CompletableFuture<List<AlignedImage>> manualElasticRegistration(@NotNull final AnalyzableImage targetImage,
                                                                           @NotNull final List<AnalyzableImage> movingImages) {
        return null;
    }
}
