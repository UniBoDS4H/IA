package com.ds4h.controller.elastic;

import com.drew.lang.annotations.NotNull;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.model.alignment.automatic.pointDetector.PointDetector;
import com.ds4h.model.deformation.ElasticAlgorithms;
import com.ds4h.model.deformation.elastic.ElasticRegistration;
import com.ds4h.model.deformation.elastic.ElasticRegistrationImpl;
import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.ImageManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElasticController {
    private ElasticAlgorithms currentAlgorithm;
    public ElasticController() {
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
        final ElasticRegistration registration = this.currentAlgorithm.getAlgorithm();
        final List<AlignedImage> outputImages = new LinkedList<>();
        final PointDetector pointDetector = detector.pointDetector();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    this.detectPoints(targetImage, image, pointDetector);
                    final AlignedImage transformedImage = registration.transformImage(image, targetImage);
                    outputImages.add(transformedImage);
                    targetImage.clearPoints();
                    image.clearPoints();
                });
                outputImages.add(new AlignedImage(targetImage.getImagePlus().getProcessor(), targetImage.getName()));
            });
           return outputImages;
        });
    }

    private void detectPoints(@NotNull final AnalyzableImage targetImage,
                              @NotNull final AnalyzableImage movingImage,
                              @NotNull final PointDetector pointDetector) {
        if (currentAlgorithm.equals(ElasticAlgorithms.BIGWARP)) {
            pointDetector.detectPoint(targetImage, movingImage);
        }
    }

    /**
     *
     * @param imageManager
     * @return
     */
    @NotNull
    public CompletableFuture<List<AlignedImage>> manualElasticRegistration(@NotNull final ImageManager imageManager) {
        final List<AlignedImage> outputImages = new LinkedList<>();
        final ElasticRegistration registration = this.currentAlgorithm.getAlgorithm();
        return CompletableFuture.supplyAsync(() -> {
            imageManager.getTargetImage().ifPresent(targetImage -> {
                imageManager.getImagesToAlign().forEach(image -> {
                    final AlignedImage alignedImage = registration.transformImage(image, targetImage);
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

    public void setAlgorithm(@NotNull ElasticAlgorithms algorithm) {
        this.currentAlgorithm = algorithm;
    }
}
