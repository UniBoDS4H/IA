package com.ds4h.model.util.json.jsonSerializer;

import com.ds4h.model.cornerManager.CornerManager;
import com.ds4h.model.util.json.JSONFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.List;

public class JSONSerializer extends JSONFile {


    private JSONSerializer(){
        super();
    }

    public static JSONArray createJSON(final CornerManager cornerManager){
        final JSONArray imageList = new JSONArray();
        //Create the json
        cornerManager.getCornerImages().forEach(imageCorners -> {
            final JSONObject obj = new JSONObject();
            final JSONArray array = new JSONArray();
            for(Point point : imageCorners.getCorners()){
                array.put(point.toString());
            }
            obj.put(FILE_KEY, imageCorners.getImage().getTitle());
            if(cornerManager.getSourceImage().isPresent() && cornerManager.getSourceImage().get().equals(imageCorners)){
                obj.put(TARGET_KEY, true);
            }
            obj.put(POINTS_KEY, array);
            imageList.put(obj);
        });
        return imageList;
    }

    public static JSONArray createJSON(final List<Mat> matrixList){
        return null;
    }
}
