package com.ds4h.controller.elastic;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.deformation.ElasticAlgorithms;
import com.ds4h.model.deformation.elastic.ElasticRegistration;
import com.ds4h.model.deformation.elastic.ElasticRegistrationImpl;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.ImageManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticController {
    private ElasticAlgorithms currentAlgorithm;
    private final ElasticRegistration elasticRegistration;
    public ElasticController() {
        this.elasticRegistration = new ElasticRegistrationImpl();
        this.currentAlgorithm = ElasticAlgorithms.BIGWARP;
    }

    /**
     *
     * @param imageManager
     * @param detector
     * @return
     */
    @NotNull
    public CompletableFuture<List<AlignedImage>> automaticElasticRegistration(@NotNull final ImageManager imageManager,
                                                                              @NotNull final Detectors detector) {
        final List<AlignedImage> outputImages = new LinkedList<>();
        final PointDetector pointDetector = detector.pointDetector();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    pointDetector.detectPoint(targetImage, image);
                    final AlignedImage transformedImage = this.elasticRegistration.transformImage(image, targetImage);
                    outputImages.add(transformedImage);
                    targetImage.clearPoints();
                    image.clearPoints();
                });
                outputImages.add(new AlignedImage(targetImage.getImagePlus().getProcessor(), targetImage.getName()));
            });
           return outputImages;
        });
    }

    /**
     *
     * @param imageManager
     * @return
     */
    @NotNull
    public CompletableFuture<List<AlignedImage>> manualElasticRegistration(@NotNull final ImageManager imageManager) {
        final List<AlignedImage> outputImages = new LinkedList<>();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    final AlignedImage alignedImage = this.elasticRegistration.transformImage(image, targetImage);
                    outputImages.add(alignedImage);
                });
                outputImages.add(new AlignedImage(targetImage.getImagePlus().getProcessor(), targetImage.getName()));
            });
            return outputImages;
        });
    }

    public ElasticAlgorithms[] getAlgorithms() {
        return ElasticAlgorithms.values();
    }

    public void setAlgorithms(@NotNull ElasticAlgorithms algorithm) {
        this.currentAlgorithm = algorithm;
    }
}
