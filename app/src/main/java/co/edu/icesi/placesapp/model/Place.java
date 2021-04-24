package co.edu.icesi.placesapp.model;

import androidx.annotation.NonNull;

import java.util.List;

public class Place {
    private String name;
    private List<String> images;
    private double score;

    public Place(String name, List<String> images, double score) {
        this.name = name;
        this.images = images;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public List<String> getImages() {
        return images;
    }

    public double getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ";"
                + images.toString().replace(",", ":").replace("[", "").replace("]", "").replace(" ", "")
                + ";" + score;
    }
}
