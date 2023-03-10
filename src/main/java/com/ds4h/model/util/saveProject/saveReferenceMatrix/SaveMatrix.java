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

    private static final String FILE_NAME = "DS4HIA_reference_matrix.txt";
    private SaveMatrix(){

    }

    public static void saveMatrix(final List<AlignedImage> images, final String path){
        final StringBuilder content = new StringBuilder();
        /*
        images.stream().filter(alignedImage -> alignedImage.getRegistrationMatrix().isPresent()).parallel().forEach(alignedImage -> {
            content.append("REFERENCE: ").append(alignedImage.getAlignedImage().getTitle()).append("\n");
            content.append("MATRIX: ").append("\n");
            final Mat matrix = alignedImage.getRegistrationMatrix().get();
            final StringBuilder stringBuilder = new StringBuilder();
            for (int row = 0; row < matrix.rows(); row++) {
                for (int col = 0; col < matrix.cols(); col++) {
                    final StringBuilder element = new StringBuilder(String.valueOf(matrix.get(row, col)[0]));
                    final String output = String.format("%-22s", element);
                    System.out.println(output.length());
                    stringBuilder.append("| ").append(output).append(" |");
                }
                stringBuilder.append("\n");
            }
            if(matrix.rows() < 3){
                for(int i = 0; i < 3; i++){
                    final StringBuilder element = new StringBuilder((i != 2 ? "0" : "1"));
                    final String output = String.format("%-22s", element);
                    stringBuilder.append("| ").append(output).append(" |");
                }
                stringBuilder.append("\n");
            }
            content.append(stringBuilder.toString());
            content.append("\n \n");


        });
        */
        for(final AlignedImage alignedImage : images){
            if(alignedImage.getRegistrationMatrix().isPresent()) {
                content.append("REFERENCE: ").append(alignedImage.getAlignedImage().getTitle()).append("\n");
                content.append("MATRIX: ").append("\n");
                final Mat matrix = alignedImage.getRegistrationMatrix().get();
                final StringBuilder stringBuilder = new StringBuilder();
                for (int row = 0; row < matrix.rows(); row++) {
                    for (int col = 0; col < matrix.cols(); col++) {
                        final StringBuilder element = new StringBuilder(String.valueOf(matrix.get(row, col)[0]));
                        final String output = String.format("%-22s", element);
                        System.out.println(output.length());
                        stringBuilder.append("| ").append(output).append(" |");
                    }
                    stringBuilder.append("\n");
                }
                if(matrix.rows() < 3){
                    for(int i = 0; i < 3; i++){
                        final StringBuilder element = new StringBuilder((i != 2 ? "0" : "1"));
                        final String output = String.format("%-22s", element);
                        stringBuilder.append("| ").append(output).append(" |");
                    }
                    stringBuilder.append("\n");
                }
                content.append(stringBuilder.toString());
            }
            content.append("\n \n");
        }
        SaveMatrix.save(path, content.toString());
    }

    private static void save(final String path, final String content){
        try (final FileWriter file = new FileWriter(path+"/"+SaveMatrix.FILE_NAME)) {
            file.write(content.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
