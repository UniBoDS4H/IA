package com.ds4h.controller.bunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.deformation.BunwarpjDeformation;
import com.ds4h.model.deformation.scales.BunwarpJMaxScale;
import com.ds4h.model.deformation.scales.BunwarpJMinScale;
import com.ds4h.model.deformation.scales.BunwarpJMode;

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
     */
    public void transformation(final List<AlignedImage> images){
        if(!this.bunwarpjDeformation.isAlive()) {
            this.bunwarpjDeformation.deformList(images);
        }
    }

    /**
     * Returns all the deformed images.
     * @return all the deformed images.
     */
    public List<AlignedImage> getImages(){
        if(!this.bunwarpjDeformation.isAlive()){
            return this.bunwarpjDeformation.getOutputList();
        }
        return Collections.emptyList();
    }

    /**
     * Returns the status thread.
     * @return the status thread.
     */
    public boolean isAlive(){
        return this.bunwarpjDeformation.isAlive();
    }

    /**
     * Set the "modeInput".
     * @param modeInput for the deformation.
     */
    public void setModeInput(final BunwarpJMode modeInput) {
        if(Objects.nonNull(modeInput)) {
            this.bunwarpjDeformation.setModeInput(modeInput);
        }
    }

    /**
     * Set the "minScale".
     * @param minScale for the deformation.
     */
    public void setMinScale(final BunwarpJMinScale minScale) {
        if(Objects.nonNull(minScale)) {
            this.bunwarpjDeformation.setMinScale(minScale);
        }
    }

    /**
     * Set the "maxScale".
     * @param maxScale for the deformation.
     */
    public void setMaxScale(final BunwarpJMaxScale maxScale) {
        if(Objects.nonNull(maxScale)) {
            this.bunwarpjDeformation.setMaxScale(maxScale);
        }
    }

    /**
     * Set the "sampleFactor".
     * @param sampleFactor for the deformation.
     */
    public void setSampleFactor(final int sampleFactor) {
        if(sampleFactor >= 0 && sampleFactor <= 7) {
            this.bunwarpjDeformation.setSampleFactor(sampleFactor);
        }
    }

    /**
     * Set the "parDivWeight".
     * @param parDivWeight for the deformation.
     */
    public void setParDivWeight(final double parDivWeight) {
        if(parDivWeight >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParDivWeight(parDivWeight);
        }
    }

    /**
     * Set the "parCurlWeight".
     * @param parCurlWeight for the deformation.
     */
    public void setParCurlWeight(final double parCurlWeight) {
        if(parCurlWeight >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParCurlWeight(parCurlWeight);
        }
    }

    /**
     * Set the "parLandmarkWeight".
     * @param parLandmarkWeight for the deformation.
     */
    public void setParLandmarkWeight(final double parLandmarkWeight) {
        if(parLandmarkWeight >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParLandmarkWeight(parLandmarkWeight);
        }
    }

    /**
     * Set the "parImageWeight".
     * @param parImageWeight for the deformation.
     */
    public void setParImageWeight(final double parImageWeight) {
        if(parImageWeight >= BunwarpjDeformation.MIN_ONE) {
            this.bunwarpjDeformation.setParImageWeight(parImageWeight);
        }
    }

    /**
     * Set the "parConsistencyWeight".
     * @param parConsistencyWeight for the deformation.
     */
    public void setParConsistencyWeight(final double parConsistencyWeight) {
        if(parConsistencyWeight >= BunwarpjDeformation.MIN_TEN) {
            this.bunwarpjDeformation.setParConsistencyWeight(parConsistencyWeight);
        }
    }

    /**
     * Set the "Threshold".
     * @param parThreshold for the deformation.
     */
    public void setParThreshold(final double parThreshold) {
        if(parThreshold >= BunwarpjDeformation.MIN_ZERO_ONE) {
            this.bunwarpjDeformation.setParThreshold(parThreshold);
        }
    }

    /**
     * Returns the "MIN_ZERO".
     * @return the MIN_ZERO.
     */
    public double getMIN_ZERO(){
        return BunwarpjDeformation.MIN_ZERO;
    }

    /**
     * Returns the "MIN_ONE".
     * @return the MIN_ONE.
     */
    public double getMIN_ONE(){
        return BunwarpjDeformation.MIN_ONE;
    }

    /**
     * Returns the "MIN_ZERO_ONE".
     * @return the MIN_ZERO_ONE.
     */
    public double getMIN_ZERO_ONE(){
        return BunwarpjDeformation.MIN_ZERO_ONE;
    }

    /**
     * Returns the "MIN_TEN".
     * @return the MIN_TEN.
     */
    public double getMIN_TEN(){
        return BunwarpjDeformation.MIN_TEN;
    }

    /**
     * Returns the "modeInput".
     * @return the modeInput.
     */
    public BunwarpJMode getModeInput() {
        return this.bunwarpjDeformation.getModeInput();
    }

    /**
     * Returns the "minScale".
     * @return the minScale.
     */
    public BunwarpJMinScale getMinScale() {
        return this.bunwarpjDeformation.getMinScale();
    }

    /**
     * Returns the "maxScale".
     * @return the maxScale.
     */
    public BunwarpJMaxScale getMaxScale() {
        return this.bunwarpjDeformation.getMaxScale();
    }

    /**
     * Retursn the "SampleFactor".
     * @return the sampleFactor.
     */
    public int getSampleFactor() {
        return this.bunwarpjDeformation.getSampleFactor();
    }

    /**
     * Returns the "divWeight".
     * @return the divWeight.
     */
    public double getParDivWeight() {
        return this.bunwarpjDeformation.getParDivWeight();
    }

    /**
     * Returns the "curlWeight".
     * @return the curlWeight.
     */
    public double getParCurlWeight() {
        return this.bunwarpjDeformation.getParCurlWeight();
    }

    /**
     * Returns the "landmarkWeight".
     * @return the landmarkWeight.
     */
    public double getParLandmarkWeight() {
        return this.bunwarpjDeformation.getParLandmarkWeight();
    }

    /**
     * Returns the "imageWeight".
     * @return the imageWeight.
     */
    public double getParImageWeight() {
        return this.bunwarpjDeformation.getParImageWeight();
    }

    /**
     * Returns the "consistencyWeight".
     * @return the consistencyWeight.
     */
    public double getParConsistencyWeight() {
        return this.bunwarpjDeformation.getParConsistencyWeight();
    }

    /**
     * Returns the "Threshold".
     * @return the Threshold.
     */
    public double getParThreshold() {
        return this.bunwarpjDeformation.getParThreshold();
    }
}