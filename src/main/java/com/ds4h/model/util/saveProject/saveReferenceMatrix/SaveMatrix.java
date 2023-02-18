package com.ds4h.model.util.saveProject.saveReferenceMatrix;

import com.ds4h.model.alignedImage.AlignedImage;
import org.opencv.core.Mat;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SaveMatrix {
    private SaveMatrix(){

    }

    public static void saveMatrix(final List<AlignedImage> images){
        final List<Mat> matrix = new LinkedList<>();
        for(AlignedImage image : images){
            final String fileName = image.getAlignedImage().getTitle();
            final char separator = '|';
            final Optional<Mat> registrationMatrix = image.getRegistrationMatrix();
            registrationMatrix.ifPresent(matrix::add);
        }
    }
}
