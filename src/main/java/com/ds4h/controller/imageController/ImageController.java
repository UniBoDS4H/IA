package com.ds4h.controller.imageController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.ImageConverter;

import java.awt.image.ColorModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ImageController {
    private final AlignmentControllerInterface alignmentControllerInterface;
    private final BunwarpJController bunwarpJController;
    private ImageEnum imageEnum = ImageEnum.ALIGNED;

    /**
     *
     * @param alignmentControllerInterface a
     * @param bunwarpJController b
     */
    public ImageController(final AlignmentControllerInterface alignmentControllerInterface, final BunwarpJController bunwarpJController){
        this.alignmentControllerInterface = alignmentControllerInterface;
        this.bunwarpJController = bunwarpJController;
    }

    public ImagePlus getAlignedImagesAsStack() throws RuntimeException{
        if(!this.getAlignedImages().isEmpty()){
            final ImageStack stack = new ImageStack(this.getAlignedImages().get(0).getAlignedImage().getWidth(),
                    this.getAlignedImages().get(0).getAlignedImage().getHeight(), ColorModel.getRGBdefault());
            System.gc();
            final List<AlignedImage> images = this.getAlignedImages();
            //final LUT[] luts = new LUT[images.size()];
            //int index = 0;
            final int bitDepth = images.stream()
                    .min(Comparator.comparingInt(img -> img.getAlignedImage().getBitDepth())).get().getAlignedImage().getBitDepth();
            IJ.log("[ALIGNED STACK] Bit Depth: " + bitDepth);
            for (final AlignedImage image : images) {
                if(!(image.getAlignedImage().getProcessor() instanceof ByteProcessor)) {
                    final ImageConverter imageConverter = new ImageConverter(image.getAlignedImage());
                    imageConverter.convertToGray8();
                    IJ.log("[STACK] " + (image.getAlignedImage().getProcessor() instanceof ByteProcessor));
                }
                //luts[index] = image.getAlignedImage().getProcessor().getLut();
                IJ.log("[NAME] " + image.getName());
                stack.addSlice(image.getName(), image.getAlignedImage().getProcessor());
                //index++;
            }
            try {
                //final CompositeImage composite = new CompositeImage(new ImagePlus("Aligned Stack", stack));
                //composite.setLuts(luts);
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
     *
     * @return a
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
     *
     * @return a
     */
    public ImageEnum type(){
        return this.imageEnum;
    }

    /**
     *
     */
    public void align(){
        this.imageEnum = ImageEnum.ALIGNED;
    }

    /**
     *
     * @param alignedImages a
     */
    public void elastic(final List<AlignedImage> alignedImages){
        if(Objects.nonNull(alignedImages) && !alignedImages.isEmpty()) {
            this.bunwarpJController.transformation(alignedImages);
            this.imageEnum = ImageEnum.ELASTIC;
        }
    }

    /**
     *
     * @return a
     */
    public boolean isAlive(){
        return this.alignmentControllerInterface.isAlive();
    }

    /**
     *
     * @return a
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
     *
     * @return a
     */
    public boolean deformationIsAlive(){
        return this.bunwarpJController.isAlive();
    }

}
