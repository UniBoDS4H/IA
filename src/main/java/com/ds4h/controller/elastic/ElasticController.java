package com.ds4h.controller.elastic;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.deformation.elastic.ElasticRegistration;
import com.ds4h.model.deformation.elastic.ElasticRegistrationImpl;
import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.ImageManager;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticController {
    final ElasticRegistration elasticRegistration;
    public ElasticController() {
        this.elasticRegistration = new ElasticRegistrationImpl();
    }

    @NotNull
    public CompletableFuture<List<AlignedImage>> automaticElasticRegistration(@NotNull final ImageManager imageManager,
                                                                              @NotNull final Detectors detector) {
        final List<AlignedImage> outputImages = new LinkedList<>();
        final PointDetector pointDetector = detector.pointDetector();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    pointDetector.detectPoint(targetImage, image);
                    this.elasticRegistration.transformImage(targetImage, image)
                            .whenComplete((transformedImage, e) -> outputImages.add(transformedImage));
                    targetImage.clear();
                });
            });
           return outputImages;
        });
    }

    public CompletableFuture<List<AlignedImage>> manualElasticRegistration(@NotNull final ImageManager imageManager) {
        final List<AlignedImage> outputImages = new LinkedList<>();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    this.elasticRegistration.transformImage(targetImage, image).whenComplete((transformedImage, e) -> outputImages.add(transformedImage));
                });
            });
            return outputImages;
        });
    }
}
