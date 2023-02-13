package com.ds4h.model.util.importProject;

import com.ds4h.model.imageCorners.ImageCorners;
import com.ds4h.model.util.CheckImage;
import com.ds4h.model.util.Pair;
import com.ds4h.model.util.exportProject.ExportProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ImportProject {

    private final static String JSON = ".json";

    private ImportProject(){

    }

    public static List<ImageCorners> importProject(final File directory) throws FileNotFoundException {
        final List<ImageCorners> images = new LinkedList<>();
        File jsonFile = null;
        if(Objects.nonNull(directory)) {
            for (final File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile() && CheckImage.checkImage(file)) {
                    images.add(new ImageCorners(file));
                }else if(ImportProject.isJSON(file)){
                    jsonFile = file;
                }
            }
            if(Objects.nonNull(jsonFile)){
                ImportProject.readJSON(jsonFile).forEach(pair -> {
                    images.forEach(imageCorners -> {
                                if(imageCorners.getImage().getTitle().equals(pair.getFirst())){
                                    pair.getSecond().forEach(imageCorners::addCorner);
                                }
                            });
                });
            }
        }else {
            return Collections.emptyList();

        }
        return images;
    }

    private static boolean isJSON(final File file){
        return file.isFile() && file.getName().endsWith(JSON);
    }

    private static List<Pair<String, List<Point>>> readJSON(final File jsonFile) throws FileNotFoundException {
        final String content = ImportProject.getContent(jsonFile);
        final List<Pair<String, List<Point>>> values = new LinkedList<>();
        if(!content.isEmpty()) {
            final JSONArray imageList = new JSONArray(content);
            for (int i = 0; i < imageList.length(); i++) {
                final JSONObject obj = imageList.getJSONObject(i);
                final JSONArray points = obj.getJSONArray(ExportProject.POINTS_KEY);
                final String fileName = obj.getString(ExportProject.FILE_KEY);
                final List<Point> listPoint = new LinkedList<>();
                for(int k = 0; k < points.length(); k++){
                    final Pair<Double, Double> pairPoint = ImportProject.getPoint(points.getString(k));
                    final Point point = new Point(pairPoint.getFirst(), pairPoint.getSecond());
                    listPoint.add(point);
                }
                values.add(new Pair<>(fileName, listPoint));
            }
        }
        return values;
    }

    //POINT in this format : {XXX, YYY}
    private static Pair<Double, Double> getPoint(final String point){
        final String[] numbers = point.substring(1, point.length()-1).split(", ");
        double x = Double.parseDouble(numbers[0]);
        double y = Double.parseDouble(numbers[1]);
        return new Pair<>(x, y);
    }

    private static String getContent(final File jsonFile){
        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line);
            }
            return fileContents.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
