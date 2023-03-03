package com.ds4h.model.util.saveProject.saveReferenceMatrix;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.json.jsonSerializer.JSONSerializer;
import org.opencv.core.Mat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class SaveMatrix {
    private SaveMatrix(){

    }

    public static void saveMatrix(final List<AlignedImage> images, final String path){
        //JSONSerializer.createJSON(images, path);
        StringBuilder content = new StringBuilder();
        for(final AlignedImage alignedImage : images){
            if(alignedImage.getRegistrationMatrix().isPresent()) {
                content.append("REFERENCE: ").append(alignedImage.getAlignedImage().getTitle()).append("\n");
                content.append("MATRIX: ").append("\n");
                final Mat matrix = alignedImage.getRegistrationMatrix().get();
                final StringBuilder stringBuilder = new StringBuilder();
                for (int row = 0; row < matrix.rows(); row++) {
                    for (int col = 0; col < matrix.cols(); col++) {
                        stringBuilder.append(matrix.get(row, col)[0]).append(" ");
                    }
                    stringBuilder.append("\n");
                }
                content.append(stringBuilder.toString());
            }
            content.append("\n \n \n");
        }
        SaveMatrix.save(path, content.toString());
    }

    private static void save(final String path, final String content){
        try (FileWriter file = new FileWriter(path+"/"+"DS4H_reference_matrix.txt")) {
            file.write(content.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
