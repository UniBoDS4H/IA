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
     *
     * @return
     */
    public List<AlignedImage> getImages(){
        if(!this.bunwarpjDeformation.isAlive()){
            return this.bunwarpjDeformation.getOutputList();
        }
        return Collections.emptyList();
    }

    /**
     *
     * @return
     */
    public boolean isAlive(){
        return this.bunwarpjDeformation.isAlive();
    }

    /**
     *
     * @param modeInput
     */
    public void setModeInput(final BunwarpJMode modeInput) {
        if(Objects.nonNull(modeInput)) {
            this.bunwarpjDeformation.setModeInput(modeInput);
        }
    }

    /**
     *
     * @param minScale
     */
    public void setMinScale(final BunwarpJMinScale minScale) {
        if(Objects.nonNull(minScale)) {
            this.bunwarpjDeformation.setMinScale(minScale);
        }
    }

    /**
     *
     * @param maxScale
     */
    public void setMaxScale(final BunwarpJMaxScale maxScale) {
        if(Objects.nonNull(maxScale)) {
            this.bunwarpjDeformation.setMaxScale(maxScale);
        }
    }

    /**
     *
     * @param sampleFactor
     */
    public void setSampleFactor(final int sampleFactor) {
        if(sampleFactor >= 0 && sampleFactor <= 7) {
            this.bunwarpjDeformation.setSampleFactor(sampleFactor);
        }
    }

    /**
     *
     * @param parDivWeigth
     */
    public void setParDivWeigth(final double parDivWeigth) {
        if(parDivWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParDivWeight(parDivWeigth);
        }
    }

    /**
     *
     * @param parCurlWeigth
     */
    public void setParCurlWeigth(final double parCurlWeigth) {
        if(parCurlWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParCurlWeight(parCurlWeigth);
        }
    }

    /**
     *
     * @param parLandmarkWeigth
     */
    public void setParLandmarkWeigth(final double parLandmarkWeigth) {
        if(parLandmarkWeigth >= BunwarpjDeformation.MIN_ZERO) {
            this.bunwarpjDeformation.setParLandmarkWeight(parLandmarkWeigth);
        }
    }

    /**
     *
     * @param parImageWeigth
     */
    public void setParImageWeigth(final double parImageWeigth) {
        if(parImageWeigth >= BunwarpjDeformation.MIN_ONE) {
            this.bunwarpjDeformation.setParImageWeight(parImageWeigth);
        }
    }

    /**
     *
     * @param parConsistencyWeigth
     */
    public void setParConsistencyWeigth(final double parConsistencyWeigth) {
        if(parConsistencyWeigth >= BunwarpjDeformation.MIN_TEN) {
            this.bunwarpjDeformation.setParConsistencyWeight(parConsistencyWeigth);
        }
    }

    /**
     *
     * @param parThreshold
     */
    public void setParThreshold(final double parThreshold) {
        if(parThreshold >= BunwarpjDeformation.MIN_ZERO_ONE) {
            this.bunwarpjDeformation.setParThreshold(parThreshold);
        }
    }

    /**
     *
     * @return
     */
    public double getMIN_ZERO(){
        return BunwarpjDeformation.MIN_ZERO;
    }

    /**
     *
     * @return
     */
    public double getMIN_ONE(){
        return BunwarpjDeformation.MIN_ONE;
    }

    /**
     *
     * @return
     */
    public double getMIN_ZERO_ONE(){
        return BunwarpjDeformation.MIN_ZERO_ONE;
    }

    /**
     *
     * @return
     */
    public double getMIN_TEN(){
        return BunwarpjDeformation.MIN_TEN;
    }

    /**
     *
     * @return
     */
    public BunwarpJMode getModeInput() {
        return this.bunwarpjDeformation.getModeInput();
    }

    /**
     *
     * @return
     */
    public BunwarpJMinScale getMinScale() {
        return this.bunwarpjDeformation.getMinScale();
    }

    /**
     *
     * @return
     */
    public BunwarpJMaxScale getMaxScale() {
        return this.bunwarpjDeformation.getMaxScale();
    }

    /**
     *
     * @return
     */
    public int getSampleFactor() {
        return this.bunwarpjDeformation.getSampleFactor();
    }

    /**
     *
     * @return
     */
    public double getParDivWeigth() {
        return this.bunwarpjDeformation.getParDivWeight();
    }

    /**
     *
     * @return
     */
    public double getParCurlWeigth() {
        return this.bunwarpjDeformation.getParCurlWeight();
    }

    /**
     *
     * @return
     */
    public double getParLandmarkWeigth() {
        return this.bunwarpjDeformation.getParLandmarkWeight();
    }

    /**
     *
     * @return
     */
    public double getParImageWeigth() {
        return this.bunwarpjDeformation.getParImageWeight();
    }

    /**
     *
     * @return
     */
    public double getParConsistencyWeigth() {
        return this.bunwarpjDeformation.getParConsistencyWeight();
    }

    /**
     *
     * @return
     */
    public double getParThreshold() {
        return this.bunwarpjDeformation.getParThreshold();
    }
}