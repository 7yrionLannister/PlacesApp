package co.edu.icesi.placesapp.model;

import java.util.ArrayList;

public class Place {
    private String name;
    private ArrayList<String> images;
    private double score;

    public Place(String name, ArrayList<String> images, double score) {
        this.name = name;
        this.images = images;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public double getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
