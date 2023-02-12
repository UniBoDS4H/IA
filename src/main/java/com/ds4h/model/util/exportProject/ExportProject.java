package com.ds4h.model.util.exportProject;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.save.SaveImages;
import ij.ImagePlus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExportProject {
    private final static String FILE_KEY = "FILE_NAME";
    private final static String POINTS_KEY = "POINTS";
    private ExportProject(){

    }
    /*
            JSON FILE :
            {
                images = [
                            file_name : "image.tif",
                            points: [ XX:YY,
                                      XX,YY,
                                      .....
                                    ],
                              file_name : "image2.tif",
                              points: [ XX:YY,
                                        XX,YY,
                                        .....
                                       ],
                          ]
            }
         */
    public static void exportProject(final List<ImageCorners> images,final String path) throws IOException {

        final JSONArray imageList = new JSONArray();
        //SaveImages.saveImages(images.stream().map(ImageCorners::getImage).collect(Collectors.toList()), path);
        //TODO: Save the file and the images in the same directory
        images.forEach(imageCorners -> {
            final JSONObject obj = new JSONObject();
            obj.put(FILE_KEY, imageCorners.getImage().getTitle());
            final JSONArray array = new JSONArray();
            for(Point point : imageCorners.getCorners()){
                array.put(point.toString());
            }
            obj.put(POINTS_KEY, array);
            imageList.put(obj);
        });

        try (FileWriter file = new FileWriter(path+ "/project.json")) {
            file.write(imageList.toString(2));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
