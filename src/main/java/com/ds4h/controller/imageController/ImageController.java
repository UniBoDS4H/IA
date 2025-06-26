package com.ds4h.controller.imageController;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.elastic.ElasticController;
import com.ds4h.controller.elastic.bunwarpJController.BunwarpJController;
import com.ds4h.model.alignment.automatic.pointDetector.Detectors;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.ImageManager;
import com.ds4h.model.util.ImageStackCreator;
import ij.ImagePlus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ImageController {
    private final AlignmentControllerInterface alignmentControllerInterface;
    private final ElasticController elasticController;
    private ImageEnum imageEnum = ImageEnum.ALIGNED;

    /**
     * Constructor for the ImageController object.
     * @param alignmentControllerInterface the alignment controller (Manual or Automatic).
     * @param elasticController: TODO
     */
    public ImageController(final AlignmentControllerInterface alignmentControllerInterface, final ElasticController elasticController) {
        this.alignmentControllerInterface = alignmentControllerInterface;
        this.elasticController = elasticController;
    }

    /**
     * Creates the Stack with all the output images from the alignment/deformation.
     * @return Stack with all the images.
     * @throws RuntimeException If there are no images for the stack.
     * @throws OutOfMemoryError If there is not enough space for the heap.
     */
    @NotNull
    public ImagePlus getAlignedImagesAsStack() throws RuntimeException, OutOfMemoryError{
        if(!this.getAlignedImages().isEmpty()){
            return ImageStackCreator.createImageStack(this.getAlignedImages());
        }
        throw new RuntimeException("The detection has failed, the number of points found can not be used with the selected \"Algorithm\".\n" +
                "Please consider to expand the memory (by going to Edit > Options > Memory & Threads)\n" +
                "increase the Threshold Factor and change the \"Algorithm\".");
    }

    @NotNull
    public ImagePlus getAlignedImageAsStack(@NotNull final List<AlignedImage> alignedImageList) {
        return ImageStackCreator.createImageStack(alignedImageList);
    }

    /**
     * Returns all the images got from the alignment/deformation.
     * @return the images.
     */
    public List<AlignedImage> getAlignedImages(){
        switch (this.imageEnum){
            case ALIGNED:
                return this.alignmentControllerInterface.getAlignedImages();
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Align the images.
     */
    public void align(){
        this.imageEnum = ImageEnum.ALIGNED;
    }

    public CompletableFuture<List<AlignedImage>> elastic(@NotNull final ImageManager imageManager, @NotNull Detectors detector){
        this.imageEnum = ImageEnum.ELASTIC;
        return this.elasticController.automaticElasticRegistration(imageManager, detector);
    }

    /**
     * Returns the name of the controller.
     * @return the name of the controller.
     */
    public String name(){
        switch (this.imageEnum){
            case ALIGNED:
                return this.alignmentControllerInterface.name();
            case ELASTIC:
                return "Elastic Deformation";
            default:
                return "";
        }
    }

    /**
     * Release all the images from the heap.
     */
    public void releaseImages(){
        switch (this.imageEnum){
            case ALIGNED:
                this.alignmentControllerInterface.releaseImages();
                break;
        }
        System.gc();
    }

    @NotNull
    public ElasticController getElasticController() {
        return elasticController;
    }
}
