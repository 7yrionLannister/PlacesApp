package co.edu.icesi.placesapp.model;

import java.util.ArrayList;

public class Place {
    private String name;
    private ArrayList<String> images;
    private double score;
    private double lat;
    private double lng;

    public Place(String name, ArrayList<String> images, double score, double lat, double lng) {
        this.name = name;
        this.images = images;
        this.score = score;
        this.lat = lat;
        this.lng = lng;
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

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
