package com.ds4h.model.reuse;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.importProject.ImportProject;
import com.ds4h.model.util.saveProject.SaveImages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReuseSources {

    private ReuseSources(){

    }
    public static List<ImageCorners> reuseSources(final CornerManager cornerManager, final List<AlignedImage> images) throws FileNotFoundException {
        final String path = SaveImages.saveTMPImages(images.stream().map(AlignedImage::getAlignedImage).collect(Collectors.toList()));
        if(!path.isEmpty()){
            return new LinkedList<>(ImportProject.importProject(new File(path)));
        }
        return Collections.emptyList();
    }
}
