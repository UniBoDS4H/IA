package com.ds4h.model.util.json.jsonDeserializer;

import com.ds4h.model.util.Pair;
import com.ds4h.model.util.json.JSONFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.io.*;
import java.util.*;

public class JSONDeserializer extends JSONFile {
    private static String TARGET_IMAGE_NAME = null;

    private JSONDeserializer(){
        super();
    }

    /**
     * Read the entire JSON and parse i, in order to create a Map with Key the name of the image and value all the points for the image.
     * @param jsonFile the json file raed from the directory
     * @return the Map<FileName, List of Points>
     * @throws FileNotFoundException if the jsonFile does not exist
     */

    public static Map<String, List<Point>> readImportProjectJSON(final File jsonFile) throws FileNotFoundException {
        final String content = JSONDeserializer.getContent(jsonFile); //Get the content of the JSON file
        final Map<String, List<Point>> values = new HashMap<>();
        if(!content.isEmpty()) {
            final JSONArray imageList = new JSONArray(content); //Put the entire content inside the JSONArray
            for (int i = 0; i < imageList.length(); i++) {
                //I know because how to export that I have a JSONObject with a JSONArray and a simple String
                final JSONObject obj = imageList.getJSONObject(i); // I get the JSON Object
                if(!obj.isNull(POINTS_KEY) && !obj.isNull(FILE_KEY)) {
                    final JSONArray points = obj.getJSONArray(POINTS_KEY); //Get the JSONArray of points
                    final String fileName = obj.getString(FILE_KEY); // Get the file name which a refered the points
                    final List<Point> listPoint = new LinkedList<>();
                    for (int k = 0; k < points.length(); k++) {
                        //Iterate all the points
                        final Pair<Double, Double> pairPoint = JSONDeserializer.getPoint(points.getString(k)); // Parse the points from string to Pair of double. "{POINTX, POINTY}"
                        final Point point = new Point(pairPoint.getFirst(), pairPoint.getSecond()); // Create the point
                        listPoint.add(point); // Add the points inside the list
                    }
                    //Find the target image
                    if (!obj.isNull(TARGET_KEY)) {
                        TARGET_IMAGE_NAME = fileName;
                    }
                    values.putIfAbsent(fileName, listPoint); // Put the values inside the HashMap
                }else{
                    return Collections.emptyMap();
                }
            }
        }
        return values;
    }

    /**
     * Read all the content from the JSON file
     * @param jsonFile input file
     * @return the entire content of the file, ""  => if happen an error
     */
    private static String getContent(final File jsonFile){
        StringBuilder fileContents = new StringBuilder();
        //Read the entire file and save it inside the fileContents
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line); //read the line
            }
            return fileContents.toString(); //return the content
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Parse the String read from the file such as : {POINTX, POINTY} and parse it as two values : x and y
     * @param point the input string read from the json file
     * @return the Pair of the two values x and y
     */
    private static Pair<Double, Double> getPoint(final String point){
        final String[] numbers = point.substring(1, point.length()-1).split(", ");
        double x = Double.parseDouble(numbers[0]);
        double y = Double.parseDouble(numbers[1]);
        return new Pair<>(x, y);
    }

    /**
     * Returns Target is set inside the json file.
     * @return True if the target is present inside the file.
     */
    public static boolean isTargetPresent(){
        return Objects.nonNull(TARGET_IMAGE_NAME);
    }

    /**
     * Returns the name of the Target.
     * @return the name of the target, if it is not present the value is empty.
     */
    public static String targetName(){
        return Objects.nonNull(TARGET_IMAGE_NAME) ? TARGET_IMAGE_NAME : "";
    }
}
