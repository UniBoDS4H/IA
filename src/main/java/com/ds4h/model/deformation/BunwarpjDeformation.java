package com.ds4h.model.deformation;

import bunwarpj.bUnwarpJ_;
import com.ds4h.model.deformation.elastic.ElasticRegistration;
import com.ds4h.model.image.AnalyzableImage;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.deformation.scales.BunwarpJMaxScale;
import com.ds4h.model.deformation.scales.BunwarpJMinScale;
import com.ds4h.model.deformation.scales.BunwarpJMode;
import ij.ImagePlus;
import bunwarpj.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This class is used in order to apply an elastic transformation using the BUnwarpJ Library.
 */
public class BunwarpjDeformation implements ElasticRegistration {
    private BunwarpJMode modeInput;
    private BunwarpJMinScale minScale;
    private BunwarpJMaxScale maxScale;
    private int sampleFactor;

    public final static double MIN_ZERO = 0.0,
            MIN_ZERO_ONE = 0.01,
            MIN_ONE = 0.1,
            MIN_TEN = 10.0;

    private double parDivWeight = MIN_ZERO,
            parCurlWeight = MIN_ZERO,
            parLandmarkWeight = MIN_ZERO,
            parImageWeight = MIN_ONE,
            parConsistencyWeight = MIN_TEN,
            parThreshold = MIN_ZERO_ONE;

    /**
     * Constructor for the BunwarpJ object.
     */
    public BunwarpjDeformation(){
        this.modeInput = BunwarpJMode.FAST_MODE;
        this.minScale = BunwarpJMinScale.VERY_COARSE;
        this.maxScale = BunwarpJMaxScale.VERY_COARSE;

    }

    private AlignedImage computeTransformation(final AnalyzableImage targetImage, final AnalyzableImage movingImage) {
        final ImagePlus sourceImage = targetImage.getImagePlus();
        final Transformation transformation = bUnwarpJ_.computeTransformationBatch(movingImage.getImagePlus(),
                sourceImage,
                movingImage.getImagePlus().getProcessor(),
                sourceImage.getProcessor(),
                this.modeInput.getValue(),
                this.sampleFactor,
                this.minScale.getValue(),
                this.maxScale.getValue(),
                this.parDivWeight,
                this.parCurlWeight,
                this.parLandmarkWeight,
                this.parImageWeight,
                this.parConsistencyWeight,
                this.parThreshold);
        final ImagePlus image = transformation.getDirectResults();
        return new AlignedImage(image.getProcessor(), movingImage.getName());
    }

    /**
     * Set the "modeInput".
     * @param modeInput how to perform the deformation.
     */
    public void setModeInput(final BunwarpJMode modeInput) {
        if(Objects.nonNull(modeInput)) {
            this.modeInput = modeInput;
        }
    }

    /**
     * Set the "minScale".
     * @param minScale for the deformation.
     */
    public void setMinScale(final BunwarpJMinScale minScale) {
        if(Objects.nonNull(minScale)) {
            this.minScale = minScale;
        }
    }

    /**
     * Set the "maxScale".
     * @param maxScale for the deformation.
     */
    public void setMaxScale(final BunwarpJMaxScale maxScale) {
        if(Objects.nonNull(maxScale)) {
            this.maxScale = maxScale;
        }
    }

    /**
     *  Set the "sampleFactor".
     * @param sampleFactor for the deformation.
     */
    public void setSampleFactor(final int sampleFactor) {
        if(sampleFactor >= 0 && sampleFactor <= 7) {
            this.sampleFactor = sampleFactor;
        }
    }

    /**
     * Set the "parDivWeight".
     * @param parDivWeight for the deformation.
     */
    public void setParDivWeight(final double parDivWeight) {
        if(parDivWeight >= MIN_ZERO) {
            this.parDivWeight = parDivWeight;
        }
    }

    /**
     * Set the "parCurlWeight".
     * @param parCurlWeight for the deformation
     */
    public void setParCurlWeight(final double parCurlWeight) {
        if(parCurlWeight >= MIN_ZERO) {
            this.parCurlWeight = parCurlWeight;
        }
    }

    /**
     * Set the "parLandmarkWeight".
     * @param parLandmarkWeight for the deformation.
     */
    public void setParLandmarkWeight(final double parLandmarkWeight) {
        if(parLandmarkWeight >= MIN_ZERO) {
            this.parLandmarkWeight = parLandmarkWeight;
        }
    }

    /**
     * Set the "parImageWeight".
     * @param parImageWeight for the deformation.
     */
    public void setParImageWeight(final double parImageWeight) {
        if(parImageWeight >= MIN_ONE) {
            this.parImageWeight = parImageWeight;
        }
    }

    /**
     * Set the "parConsistencyWeight".
     * @param parConsistencyWeight for the deformation.
     */
    public void setParConsistencyWeight(double parConsistencyWeight) {
        if(parConsistencyWeight >= MIN_TEN) {
            this.parConsistencyWeight = parConsistencyWeight;
        }
    }

    /**
     * Set the "parThreshold".
     * @param parThreshold for the deformation.
     */
    public void setParThreshold(double parThreshold) {
        if(parThreshold >= MIN_ZERO_ONE) {
            this.parThreshold = parThreshold;
        }
    }

    /**
     * Returns the "modeInput".
     * @return the modeInput.
     */
    public BunwarpJMode getModeInput() {
        return modeInput;
    }

    /**
     * Returns the "minScale".
     * @return the minScale.
     */
    public BunwarpJMinScale getMinScale() {
        return minScale;
    }

    /**
     * Returns the "maxScale".
     * @return the maxScale.
     */
    public BunwarpJMaxScale getMaxScale() {
        return maxScale;
    }

    /**
     * Returns the "sampleFactor".
     * @return the sampleFactor.
     */
    public int getSampleFactor() {
        return sampleFactor;
    }

    /**
     * Returns the "divWeight".
     * @return the divWeight.
     */
    public double getParDivWeight() {
        return parDivWeight;
    }

    /**
     * Returns the "curlWeight".
     * @return the curlWeight.
     */
    public double getParCurlWeight() {
        return parCurlWeight;
    }

    /**
     * Returns the "landmarkWeight".
     * @return the landmarkWeight.
     */
    public double getParLandmarkWeight() {
        return parLandmarkWeight;
    }

    /**
     * Returns the "ImageWeight".
     * @return the ImageWeight.
     */
    public double getParImageWeight() {
        return parImageWeight;
    }

    /**
     * Returns the "ConsistencyWeight".
     * @return the ConsistencyWeight.
     */
    public double getParConsistencyWeight() {
        return parConsistencyWeight;
    }

    /**
     * Returns the "Threshold".
     * @return the Threshold.
     */
    public double getParThreshold() {
        return parThreshold;
    }


    @NotNull
    @Override
    public List<AlignedImage> transformImages(@NotNull AnalyzableImage targetImage, @NotNull List<AnalyzableImage> movingImages) {
        return movingImages.stream()
                .map(movingImage -> this.computeTransformation(targetImage, movingImage))
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public AlignedImage transformImage(@NotNull AnalyzableImage targetImage, @NotNull AnalyzableImage movingImage) {
        return this.computeTransformation(targetImage, movingImage);
    }
}
