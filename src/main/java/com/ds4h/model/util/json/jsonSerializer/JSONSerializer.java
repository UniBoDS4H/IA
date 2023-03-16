package com.ds4h.model.util.json.jsonSerializer;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.json.JSONFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JSONSerializer extends JSONFile {


    private JSONSerializer(){
        super();
    }

    /*
        JSON FILE :
        {
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
        }
     */
    public static void createJSON(final PointManager pointManager, final String path){
        final JSONArray imageList = new JSONArray();
        //Create the json
        pointManager.getCornerImages().forEach(imageCorners -> {
            final JSONObject obj = new JSONObject();
            final JSONArray array = new JSONArray();
            for(Point point : imageCorners.getPoints()){
                array.put(point.toString());
            }
            obj.put(FILE_KEY, imageCorners.getTitle());
            if(pointManager.getSourceImage().isPresent() && pointManager.getSourceImage().get().equals(imageCorners)){
                obj.put(TARGET_KEY, true);
            }
            obj.put(POINTS_KEY, array);
            imageList.put(obj);
        });
        JSONSerializer.writeJSON(imageList, path);
    }


    public static void createJSON(final List<AlignedImage> images, final String path){
        final JSONArray jsonArray = new JSONArray();
        for(AlignedImage alignedImage : images){
            if(alignedImage.getRegistrationMatrix().isPresent()) {
                final Mat matrix = alignedImage.getRegistrationMatrix().get();
                final JSONObject obj = new JSONObject();
                System.out.println(matrix.rows() + " " + matrix.cols());
                obj.put(FILE_KEY, alignedImage.getName());
                for (int row = 0; row < matrix.rows(); row++) {
                    final JSONArray singleRow = new JSONArray();
                    for (int col = 0; col < matrix.cols(); col++) {
                        final String element  = Arrays.toString(matrix.get(row, col));
                        singleRow.put(element);
                    }
                    obj.put(ROW_KEY+"_"+String.valueOf(row), singleRow);
                }
                jsonArray.put(obj);
            }
        }
        JSONSerializer.writeJSON(jsonArray, path, MATRIX_PROJECT_NAME);
    }


    /**
     * Write the JSONFile in the path. This is used inside the EXPORT of the project
     * @param array all the project information
     * @param path where will be stored the inormation
     */
    public static void writeJSON(final JSONArray array, final String path){
        try (FileWriter file = new FileWriter(path+"/"+ EXPORT_PROJECT_NAME)) {
            file.write(array.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Write the JSONFile in the path
     * @param array all the project information
     * @param path where will be stored the inormation
     */
    private static void writeJSON(final JSONArray array, final String path, final String fileName){
        try (FileWriter file = new FileWriter(path+"/"+fileName)) {
            file.write(array.toString(2));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
