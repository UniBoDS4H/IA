package com.ds4h.controller.bunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.deformation.BunwarpjDeformation;
import com.ds4h.model.deformation.scales.BunwarpJMaxScale;
import com.ds4h.model.deformation.scales.BunwarpJMinScale;
import com.ds4h.model.deformation.scales.BunwarpJMode;
import ij.ImagePlus;

import java.util.*;

/**
 * Controller for the BunwarpJ elastic deformation
 */
public class BunwarpJController {

    private final BunwarpjDeformation bunwarpjDeformation;

    public BunwarpJController(){
        this.bunwarpjDeformation = new BunwarpjDeformation();
    }

    /**
     * Use BunwarpJ in order to apply the elastic transformation using the Class Model.
     * @param images : the list of images to deform
     * @return : the transformed image
     */
    public List<ImagePlus> transformation(final List<AlignedImage> images){
        final Optional<AlignedImage> source = images.stream().filter(alignedImage -> !alignedImage.getRegistrationMatrix().isPresent()).findFirst();
        if(source.isPresent()) {
            final List<ImagePlus> imagePlusList = new LinkedList<>();
            images.stream().map(AlignedImage::getAlignedImage)
                    .map(alignedImg -> this.bunwarpjDeformation.deform(alignedImg, source.get().getAlignedImage()))
                    .forEach(imagePlusList::add);
            return imagePlusList;
        }else{
            return Collections.emptyList();
        }
    }

    public void setModeInput(final BunwarpJMode modeInput) {
        if(Objects.nonNull(modeInput)) {
            this.bunwarpjDeformation.setModeInput(modeInput);
        }
    }

    public void setMinScale(final BunwarpJMinScale minScale) {
        if(Objects.nonNull(minScale)) {
            this.bunwarpjDeformation.setMinScale(minScale);
        }
    }

    public void setMaxScale(final BunwarpJMaxScale maxScale) {
        if(Objects.nonNull(maxScale)) {
            this.bunwarpjDeformation.setMaxScale(maxScale);
        }
    }

    public void setSampleFactor(final int sampleFactor) {
        if(sampleFactor >= 0 && sampleFactor <= 7) {
            this.bunwarpjDeformation.setSampleFactor(sampleFactor);
        }
    }

    public void setParDivWeigth(final double parDivWeigth) {
        if(parDivWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParDivWeigth(parDivWeigth);
        }
    }

    public void setParCurlWeigth(final double parCurlWeigth) {
        if(parCurlWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParCurlWeigth(parCurlWeigth);
        }
    }

    public void setParLandmarkWeigth(final double parLandmarkWeigth) {
        if(parLandmarkWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParLandmarkWeigth(parLandmarkWeigth);
        }
    }

    public void setParImageWeigth(final double parImageWeigth) {
        if(parImageWeigth >= BunwarpjDeformation.MIN_ONE) {
            this.bunwarpjDeformation.setParImageWeigth(parImageWeigth);
        }
    }

    public void setParConsistencyWeigth(final double parConsistencyWeigth) {
        if(parConsistencyWeigth >= BunwarpjDeformation.MIN_TEN) {
            this.bunwarpjDeformation.setParConsistencyWeigth(parConsistencyWeigth);
        }
    }

    public void setParThreshold(final double parThreshold) {
        if(parThreshold >= BunwarpjDeformation.MIN_ZERO_ONE) {
            this.bunwarpjDeformation.setParThreshold(parThreshold);
        }
    }

    public double getMIN_ZERO(){
        return BunwarpjDeformation.MIN_ZERO;
    }

    public double getMIN_ONE(){
        return BunwarpjDeformation.MIN_ONE;
    }
    public double getMIN_ZERO_ONE(){
        return BunwarpjDeformation.MIN_ZERO_ONE;
    }
    public double getMIN_TEN(){
        return BunwarpjDeformation.MIN_TEN;
    }

    public BunwarpJMode getModeInput() {
        return this.bunwarpjDeformation.getModeInput();
    }

    public BunwarpJMinScale getMinScale() {
        return this.bunwarpjDeformation.getMinScale();
    }

    public BunwarpJMaxScale getMaxScale() {
        return this.bunwarpjDeformation.getMaxScale();
    }

    public int getSampleFactor() {
        return this.bunwarpjDeformation.getSampleFactor();
    }

    public double getParDivWeigth() {
        return this.bunwarpjDeformation.getParDivWeigth();
    }

    public double getParCurlWeigth() {
        return this.bunwarpjDeformation.getParCurlWeigth();
    }

    public double getParLandmarkWeigth() {
        return this.bunwarpjDeformation.getParLandmarkWeigth();
    }

    public double getParImageWeigth() {
        return this.bunwarpjDeformation.getParImageWeigth();
    }

    public double getParConsistencyWeigth() {
        return this.bunwarpjDeformation.getParConsistencyWeigth();
    }

    public double getParThreshold() {
        return this.bunwarpjDeformation.getParThreshold();
    }

}