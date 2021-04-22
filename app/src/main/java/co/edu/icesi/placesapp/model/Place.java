package co.edu.icesi.placesapp.model;

public class Place {
    private String name;
    private String image;
    private double score;

    public Place(String name, String image, double score) {
        this.name = name;
        this.image = image;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
