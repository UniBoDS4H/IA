package com.ds4h.model.deformation.elastic;

import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.alignedImage.AlignedImage;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.*;

public class AutomaticElasticRegistration implements ElasticRegistration {
    final PointDetector detector;
    public AutomaticElasticRegistration(@NotNull final PointDetector detector) {
        this.detector = detector;
    }

    @NotNull
    @Override
    public CompletableFuture<AlignedImage> transform(@NotNull AlignedImage movingImage, @NotNull AlignedImage targetImage) {
        return CompletableFuture.supplyAsync(() -> null);
    }
}
