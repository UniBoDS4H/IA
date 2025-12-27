package com.ds4h.model.util.saveProject.saveReferenceMatrix;

import com.ds4h.model.image.alignedImage.AlignedImage;
import org.opencv.core.Mat;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Save the Registration matrix.
 */
public class SaveMatrix {

    private static final String FILE_NAME = "DS4HIA_reference_matrix.txt";
    private SaveMatrix(){

    }

    /**
     * Save all the registration matrix stored inside "images".
     * @param images the list of all the "AlignedImage" object.
     * @param path where will be stored the file with all the matrix.
     */
    public static void saveMatrix(final List<AlignedImage> images, final String path){
        final StringBuilder content = new StringBuilder();

        images.stream()
                .filter(alignedImage -> alignedImage.getRegistrationMatrix().isPresent())
                .forEach(alignedImage -> {
                    content.append("REFERENCE: ").append(alignedImage.getAlignedImage().getTitle()).append("\n");
                    content.append("MATRIX: ").append("\n");
                    final Mat matrix = alignedImage.getRegistrationMatrix().get();
                    final StringBuilder stringBuilder = new StringBuilder();
                    final int rows = matrix.rows();
                    final int cols = matrix.cols();

                    IntStream.range(0, rows).forEach(row -> {
                        IntStream.range(0, cols).forEach(col -> {
                            final StringBuilder element = new StringBuilder(String.valueOf(matrix.get(row, col)[0]));
                            final String output = String.format("%-22s", element);
                            stringBuilder.append("| ").append(output).append(" |");
                        });
                        stringBuilder.append("\n");
                    });
                    if(rows < 3){
                        IntStream.range(0, 3).forEach(i -> {
                            final StringBuilder element = new StringBuilder((i != 2 ? "0" : "1"));
                            final String output = String.format("%-22s", element);
                            stringBuilder.append("| ").append(output).append(" |");
                        });
                        stringBuilder.append("\n");
                    }
                    content.append(stringBuilder.toString());
                    content.append("\n \n");
        });
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
