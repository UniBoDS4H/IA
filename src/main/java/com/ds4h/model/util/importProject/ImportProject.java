package com.ds4h.model.util.importProject;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.exportProject.ExportProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.io.*;
import java.util.*;

public class ImportProject {

    private final static String JSON = ".json";

    private ImportProject(){

    }

    /**
     * Import the DS4H Project.
     * It is a Directory with a bunch of images and a JSON file with the configuration of the project. Inside the JSON file
     * there are all the points foreach image
     * @param directory the directory where is stored the project
     * @return a List of all the ImageCorners
     * @throws FileNotFoundException if the JSON file does not exist
     */
    public static List<ImageCorners> importProject(final File directory) throws FileNotFoundException {
        final List<ImageCorners> images = new LinkedList<>();
        File jsonFile = null;
        //TODO: add the check for the name of the Directory ?
        if(Objects.nonNull(directory) && directory.isDirectory()) {
            // If the directory exists I get all the files inside it
            for (final File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile() && CheckImage.checkImage(file)) {
                    //if the file is an image then we create a new ImageCorners with this File.
                    images.add(new ImageCorners(file));
                }else if(ImportProject.isJSON(file)){
                    //Otherwise if the file is our configuration file, we save it for the future.
                    jsonFile = file;
                }
            }
            if(Objects.nonNull(jsonFile)){
                //If we have found the configuration json, we read it and we assing for each image its points.
                ImportProject.readJSON(jsonFile).forEach((key, value) -> images.forEach(imageCorners -> {
                    if (imageCorners.getImage().getTitle().equals(key)) {
                        //Add the points for the image
                        value.forEach(imageCorners::addCorner);
                    }
                }));
            }
        }else {
            //If the file is not correct
            throw new FileNotFoundException("The File selected is not a directory. Please search for a DS4H_Project-X folder");
        }
        //If the json file does not exists we return all the image without points
        return images;
    }

    /**
     * Check if the file is a JSON.
     * @param file the input File read from the directory
     * @return true if it is JSON, false otherwise
     */
    private static boolean isJSON(final File file){
        return file.isFile() && file.getName().endsWith(JSON);
    }

    /**
     * Read the entire JSON and parse i, in order to create a Map with Key the name of the image and value all the points for the image.
     * @param jsonFile the json file raed from the directory
     * @return the Map<FileName, List of Points>
     * @throws FileNotFoundException if the jsonFile does not exist
     */
    private static Map<String, List<Point>> readJSON(final File jsonFile) throws FileNotFoundException {
        final String content = ImportProject.getContent(jsonFile); //Get the content of the JSON file
        final Map<String, List<Point>> values = new HashMap<>();
        if(!content.isEmpty()) {
            final JSONArray imageList = new JSONArray(content); //Put the entire content inside the JSONArray
            for (int i = 0; i < imageList.length(); i++) {
                //I know because how to export that I have a JSONObject with a JSONArray and a simple String
                final JSONObject obj = imageList.getJSONObject(i); // I get the JSON Object
                final JSONArray points = obj.getJSONArray(ExportProject.POINTS_KEY); //Get the JSONArray of points
                final String fileName = obj.getString(ExportProject.FILE_KEY); // Get the file name which a refered the points
                final List<Point> listPoint = new LinkedList<>();
                for(int k = 0; k < points.length(); k++){
                    //Iterate all the points
                    final Pair<Double, Double> pairPoint = ImportProject.getPoint(points.getString(k)); // Parse the points from string to Pair of double. "{POINTX, POINTY}"
                    final Point point = new Point(pairPoint.getFirst(), pairPoint.getSecond()); // Create the point
                    listPoint.add(point); // Add the points inside the list
                }
                values.putIfAbsent(fileName, listPoint); // Put the values inside the HashMap
            }
        }
        return values;
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
}
