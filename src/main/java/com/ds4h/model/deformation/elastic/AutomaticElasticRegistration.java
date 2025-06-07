package com.ds4h.model.deformation.elastic;

import bigwarp.landmarks.LandmarkTableModel;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.image.alignedImage.AlignedImage;
import ij.ImagePlus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class AutomaticElasticRegistration implements ElasticRegistration {
    final PointDetector detector;
    public AutomaticElasticRegistration(@NotNull final PointDetector detector) {
        this.detector = detector;
    }

    @NotNull
    @Override
    public CompletableFuture<AlignedImage> transform(@NotNull AlignedImage movingImage, @NotNull AlignedImage targetImage) {
        return CompletableFuture.supplyAsync(() -> {

            return null;
        });
    }
    private List<ImagePlus> applyElasticRegistration(@NotNull final AlignedImage movingImage, @NotNull final AlignedImage targetImage, @NotNull final LandmarkTableModel landmarkTableModel) {
        return Collections.emptyList();
    }
}
