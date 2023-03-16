package com.ds4h.model.deformation;

import bunwarpj.bUnwarpJ_;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.deformation.scales.BunwarpJMaxScale;
import com.ds4h.model.deformation.scales.BunwarpJMinScale;
import com.ds4h.model.deformation.scales.BunwarpJMode;
import ij.ImagePlus;
import bunwarpj.Transformation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is used in order to apply an elastic transformation using the BUnwarpJ Library.
 */
public class BunwarpjDeformation implements Runnable{
    private BunwarpJMode modeInput;
    private BunwarpJMinScale minScale;
    private BunwarpJMaxScale maxScale;
    private int sampleFactor;

    private final List<AlignedImage> outputList;
    private final List<AlignedImage> alignedImages;
    private Optional<AlignedImage> source;
    private Thread thread;
    public final static double MIN_ZERO = 0.0,
            MIN_ZERO_ONE = 0.01,
            MIN_ONE = 0.1,
            MIN_TEN = 10.0;

    private double parDivWeigth = MIN_ZERO,
            parCurlWeigth = MIN_ZERO,
            parLandmarkWeigth = MIN_ZERO,
            parImageWeigth = MIN_ONE,
            parConsistencyWeigth = MIN_TEN,
            parThreshold = MIN_ZERO_ONE;


    public BunwarpjDeformation(){
        this.outputList = new CopyOnWriteArrayList<>();
        this.alignedImages = new CopyOnWriteArrayList<>();
        this.source = Optional.empty();
        this.thread = new Thread(this);
        this.modeInput = BunwarpJMode.FAST_MODE;
        this.minScale = BunwarpJMinScale.VERY_COARSE;
        this.maxScale = BunwarpJMaxScale.VERY_COARSE;

    }
    /**
     * Use BunwarpJ in order to apply the elastic transformation.
     * @param target : Target Image
     * @param source : Source Image
     * @return :
     */

    public ImagePlus deform(final ImagePlus target, final ImagePlus source){
        //Compute the tranformation
        final Transformation transformation = bUnwarpJ_.computeTransformationBatch(target,
                source,
                target.getProcessor(),
                source.getProcessor(),
                this.modeInput.getValue(),
                this.sampleFactor,
                this.minScale.getValue(),
                this.maxScale.getValue(),
                this.parDivWeigth,
                this.parCurlWeigth,
                this.parLandmarkWeigth,
                this.parImageWeigth,
                this.parConsistencyWeigth,
                this.parThreshold);
        return transformation.getDirectResults();
    }

    public void deformList(final List<AlignedImage> images){
        this.source = images.stream().filter(alignedImage -> !alignedImage.getRegistrationMatrix().isPresent()).findFirst();
        if(source.isPresent()  && !this.thread.isAlive()) {
            this.outputList.clear();
            this.alignedImages.clear();
            this.alignedImages.addAll(images);
            this.thread.start();
        }
    }

    public boolean isAlive(){
        return this.thread.isAlive();
    }

    public List<AlignedImage> getOutputList(){
        return new LinkedList<>(this.outputList);
    }

    /**
     * Open a new thread for the deformation operation. After the deformation is done, all the images are
     * stored inside the output list.
     */
    @Override
    public void run() {
        if(this.source.isPresent()){
            final ImagePlus sourceImage = this.source.get().getAlignedImage();
            for(final AlignedImage alignedImage : this.alignedImages){
                final Transformation transformation = bUnwarpJ_.computeTransformationBatch(alignedImage.getAlignedImage(),
                        sourceImage,
                        alignedImage.getAlignedImage().getProcessor(),
                        sourceImage.getProcessor(),
                        this.modeInput.getValue(),
                        this.sampleFactor,
                        this.minScale.getValue(),
                        this.maxScale.getValue(),
                        this.parDivWeigth,
                        this.parCurlWeigth,
                        this.parLandmarkWeigth,
                        this.parImageWeigth,
                        this.parConsistencyWeigth,
                        this.parThreshold);
                final ImagePlus image = transformation.getDirectResults();
                image.setTitle(alignedImage.getAlignedImage().getTitle());
                this.outputList.add(alignedImage.getRegistrationMatrix().isPresent() ?
                        new AlignedImage(alignedImage.getRegistrationMatrix().get(), image) :
                        new AlignedImage(image));
            }
        }
        this.thread = new Thread(this);
    }


    public void setModeInput(final BunwarpJMode modeInput) {
        if(Objects.nonNull(modeInput)) {
            this.modeInput = modeInput;
        }
    }

    public void setMinScale(final BunwarpJMinScale minScale) {
        if(Objects.nonNull(minScale)) {
            this.minScale = minScale;
        }
    }

    public void setMaxScale(final BunwarpJMaxScale maxScale) {
        if(Objects.nonNull(maxScale)) {
            this.maxScale = maxScale;
        }
    }

    public void setSampleFactor(final int sampleFactor) {
        if(sampleFactor >= 0 && sampleFactor <= 7) {
            this.sampleFactor = sampleFactor;
        }
    }

    public void setParDivWeigth(final double parDivWeigth) {
        if(parDivWeigth >= MIN_ZERO) {
            this.parDivWeigth = parDivWeigth;
        }
    }

    public void setParCurlWeigth(final double parCurlWeigth) {
        if(parCurlWeigth >= MIN_ZERO) {
            this.parCurlWeigth = parCurlWeigth;
        }
    }

    public void setParLandmarkWeigth(final double parLandmarkWeigth) {
        if(parLandmarkWeigth >= MIN_ZERO) {
            this.parLandmarkWeigth = parLandmarkWeigth;
        }
    }

    public void setParImageWeigth(final double parImageWeigth) {
        if(parImageWeigth >= MIN_ONE) {
            this.parImageWeigth = parImageWeigth;
        }
    }

    public void setParConsistencyWeigth(double parConsistencyWeigth) {
        if(parConsistencyWeigth >= MIN_TEN) {
            this.parConsistencyWeigth = parConsistencyWeigth;
        }
    }

    public void setParThreshold(double parThreshold) {
        if(parThreshold >= MIN_ZERO_ONE) {
            this.parThreshold = parThreshold;
        }
    }

    public BunwarpJMode getModeInput() {
        return modeInput;
    }

    public BunwarpJMinScale getMinScale() {
        return minScale;
    }

    public BunwarpJMaxScale getMaxScale() {
        return maxScale;
    }

    public int getSampleFactor() {
        return sampleFactor;
    }

    public double getParDivWeigth() {
        return parDivWeigth;
    }

    public double getParCurlWeigth() {
        return parCurlWeigth;
    }

    public double getParLandmarkWeigth() {
        return parLandmarkWeigth;
    }

    public double getParImageWeigth() {
        return parImageWeigth;
    }

    public double getParConsistencyWeigth() {
        return parConsistencyWeigth;
    }

    public double getParThreshold() {
        return parThreshold;
    }


}
