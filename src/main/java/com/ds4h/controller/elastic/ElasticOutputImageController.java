package com.ds4h.controller.elastic;

import com.drew.lang.annotations.NotNull;
import com.ds4h.controller.alignmentController.AlignmentControllerInterface;
import com.ds4h.model.image.alignedImage.AlignedImage;
import com.ds4h.model.util.ImageStackCreator;
import ij.ImagePlus;

import java.util.List;

public class ElasticOutputImageController implements AlignmentControllerInterface {
    private final List<AlignedImage> alignedImageList;

    public ElasticOutputImageController(@NotNull final List<AlignedImage> alignedImageList) {
        this.alignedImageList = alignedImageList;
    }

    @NotNull
    @Override
    public List<AlignedImage> getAlignedImages() {
        return this.alignedImageList;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @NotNull
    @Override
    public String name() {
        return "ELASTIC";
    }

    @NotNull
    @Override
    public ImagePlus getAlignedImagesAsStack() {
        return ImageStackCreator.createImageStack(alignedImageList);
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public void releaseImages() {
        this.alignedImageList.clear();
        System.gc();
    }
}
