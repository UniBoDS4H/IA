package com.ds4h.model.util.importProject;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.json.jsonDeserializer.JSONDeserializer;

import java.io.*;
import java.util.*;

public class ImportProject {
    private static Optional<ImageCorners> TARGET_IMAGE = Optional.empty();

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
        final List<File> tmpFiles = new LinkedList<>();
        final List<ImageCorners> images = new LinkedList<>();
        File jsonFile = null;
        //TODO: add the check for the name of the Directory ?
        if(Objects.nonNull(directory) && directory.isDirectory()) {
            // If the directory exists I get all the files inside it
            for (final File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile() && CheckImage.checkImage(file)) {
                    //if the file is an image then we create a new ImageCorners with this File.
                    tmpFiles.add(file); // Because of the order that can be random, I save the images inside a buffer
                }else if(ImportProject.isJSON(file)){
                    //Otherwise if the file is our configuration file, we save it for the future.
                    jsonFile = file;
                }
            }
            if(Objects.nonNull(jsonFile)){
                //If we have found the configuration json, we read it and we assing for each image its points.
                JSONDeserializer.readImportProjectJSON(jsonFile).forEach((key, value) -> tmpFiles.forEach(file -> {
                    if (file.getName().equals(key)) {
                        final ImageCorners imageCorners = new ImageCorners(file);
                        value.forEach(imageCorners::addCorner);
                        images.add(imageCorners);
                        if(JSONDeserializer.isTargetPresent() && JSONDeserializer.targetName().equals(file.getName())){
                            TARGET_IMAGE = Optional.of(imageCorners);
                        }
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

    public static Optional<ImageCorners> getTargetImage(){
        return TARGET_IMAGE;
    }
}
