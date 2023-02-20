package com.ds4h.model.util.saveProject.saveReferenceMatrix;

import com.ds4h.model.alignedImage.AlignedImage;
import com.ds4h.model.util.json.jsonSerializer.JSONSerializer;
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

    public static void saveMatrix(final List<AlignedImage> images, final String path){
        JSONSerializer.createJSON(images, path);
    }
}
