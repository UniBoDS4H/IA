package com.ds4h.controller.imageController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;
import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;
import ij.process.LUT;
import java.awt.image.ColorModel;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ImageController {
    private final AlignmentControllerInterface alignmentControllerInterface;
    private final BunwarpJController bunwarpJController;
    private ImageEnum imageEnum = ImageEnum.ALIGNED;

    /**
     * Constructor for the ImageController object.
     * @param alignmentControllerInterface the alignment controller (Manual or Automatic).
     * @param bunwarpJController the bunwarpj controller, for the elastic transformation.
     */
    public ImageController(final AlignmentControllerInterface alignmentControllerInterface, final BunwarpJController bunwarpJController){
        this.alignmentControllerInterface = alignmentControllerInterface;
        this.bunwarpJController = bunwarpJController;
    }

    /**
     * Creates the Stack with all the output images from the alignment/deformation.
     * @return Stack with all the images.
     * @throws RuntimeException If there are no images for the stack.
     * @throws OutOfMemoryError If there is not enough space for the heap.
     */
    public ImagePlus getAlignedImagesAsStack() throws RuntimeException, OutOfMemoryError{
        if(!this.getAlignedImages().isEmpty()){
            final ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(),
                    this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            final List<AlignedImage> images = this.getAlignedImages();
            for (final AlignedImage image : images) {
                if(!(image.getAlignedImage().getProcessor() instanceof ByteProcessor)){
                    final ImagePlus copy = new ImagePlus(image.getName(),
                            image.getAlignedImage().getProcessor().convertToByteProcessor());
                    stack.addSlice(image.getName(), copy.getProcessor());
                }else {
                    IJ.log("[NAME] " + image.getName());
                    stack.addSlice(image.getName(), image.getAlignedImage().getProcessor());
                }
            }
            try {
                System.gc();
                return new ImagePlus("Aligned Stack", stack);
            }catch (Exception e){
                throw new RuntimeException("Something went wrong with the creation of the stack.");
            }
        }
        throw new RuntimeException("The detection has failed, the number of points found can not be used with the selected \"Algorithm\".\n" +
                "Please consider to expand the memory (by going to Edit > Options > Memory & Threads)\n" +
                "increase the Threshold Factor and change the \"Algorithm\".");
    }

    /**
     * Returns all the images got from the alignment/deformation.
     * @return the images.
     */
    public List<AlignedImage> getAlignedImages(){
        switch (this.imageEnum){
            case ALIGNED:
                return this.alignmentControllerInterface.getAlignedImages();
            case ELASTIC:
                return this.bunwarpJController.getImages();
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

    /**
     * Perform the Elastic Deformation.
     * @param alignedImages all the images to deform.
     */
    public void elastic(final List<AlignedImage> alignedImages){
        if(Objects.nonNull(alignedImages) && !alignedImages.isEmpty()) {
            this.bunwarpJController.transformation(alignedImages);
            this.imageEnum = ImageEnum.ELASTIC;
        }
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
     * Returns the status of the deformation.
     * @return the status of the deformation.
     */
    public boolean deformationIsAlive(){
        return this.bunwarpJController.isAlive();
    }

    public void releaseImages(){
        IJ.log("QUI");
        this.getAlignedImages().forEach(AlignedImage::releaseImage);
        System.gc();
    }

}
