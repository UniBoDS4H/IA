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

    public ImageController(final AlignmentControllerInterface alignmentControllerInterface, final BunwarpJController bunwarpJController){
        this.alignmentControllerInterface = alignmentControllerInterface;
        this.bunwarpJController = bunwarpJController;
    }

    public List<AlignedImage> getAlignedImages(){
        switch (this.imageEnum){
            case ALIGNED:
                return this.alignmentControllerInterface.getAlignedImages();
            case ELASTIC:
                System.out.println(this.bunwarpJController.getImages().size());
                return this.bunwarpJController.getImages();
            default:
                return Collections.emptyList();
        }
    }

    public void align(){
        this.imageEnum = ImageEnum.ALIGNED;
    }

    public void elastic(final List<AlignedImage> alignedImages){
        if(Objects.nonNull(alignedImages) && !alignedImages.isEmpty()) {
            this.bunwarpJController.transformation(alignedImages);
            this.imageEnum = ImageEnum.ELASTIC;
        }
    }

    public boolean isAlive(){
        return this.alignmentControllerInterface.isAlive();
    }

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

    public boolean deformationIsAlive(){
        return this.bunwarpJController.isAlive();
    }

}
