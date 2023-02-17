package com.ds4h.model.util.saveProject.saveReferenceMatrix;

import com.ds4h.model.alignedImage.AlignedImage;
import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SaveMatrix {
    private SaveMatrix(){

    }

    public static void saveMatrix(final List<AlignedImage> images){
        for(AlignedImage image : images){
            final String fileName = image.getAlignedImage().getTitle();
            final char separator = '|';
            final Optional<Mat> registrationMatrix = image.getRegistrationMatrix();
            if(registrationMatrix.isPresent()){
                final Mat matrix = registrationMatrix.get();
                final StringBuilder outputMatrix = new StringBuilder();
                for(int row = 0; row < matrix.rows(); row++){
                    outputMatrix.append(separator);
                    for(int col = 0; col < matrix.cols(); col++){
                        outputMatrix.append((matrix.get(row, col)[0]));
                        if(col != matrix.cols() - 1){
                            outputMatrix.append("\t");
                            outputMatrix.append(separator);
                            outputMatrix.append("\t");
                        }
                    }
                    outputMatrix.append(separator);
                    outputMatrix.append("\n");
                }
                final String output = outputMatrix.toString();
                System.out.println(output);
            }
        }
    }
}
