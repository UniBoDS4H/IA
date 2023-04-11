package com.ds4h.controller.imageController;

import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.controller.bunwarpJController.BunwarpJController;
import com.ds4h.model.alignedImage.AlignedImage;

import java.util.Collections;
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
