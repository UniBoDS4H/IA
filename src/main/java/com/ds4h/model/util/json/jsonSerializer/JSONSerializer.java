package com.ds4h.model.util.json.jsonSerializer;

import com.ds4h.model.pointManager.PointManager;
import com.ds4h.model.util.json.JSONFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.io.FileWriter;
import java.io.IOException;

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

    /**
     *
     * @param pointManager a
     * @param path b
     */
    public static void createJSON(final PointManager pointManager, final String path){
        final JSONArray imageList = new JSONArray();
        //Create the json
        pointManager.getPointImages().forEach(imageCorners -> {
            final JSONObject obj = new JSONObject();
            final JSONArray array = new JSONArray();
            for(Point point : imageCorners.getPoints()){
                array.put(point.toString());
            }
            obj.put(FILE_KEY, imageCorners.getTitle());
            if(pointManager.getTargetImage().isPresent() && pointManager.getTargetImage().get().equals(imageCorners)){
                obj.put(TARGET_KEY, true);
            }
            obj.put(POINTS_KEY, array);
            imageList.put(obj);
        });
        JSONSerializer.writeJSON(imageList, path);
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
}
