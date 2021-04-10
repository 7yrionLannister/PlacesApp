package co.edu.icesi.placesapp;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceView extends RecyclerView.ViewHolder {

    private ConstraintLayout root;
    private TextView name;
    private TextView score;
    private ImageView image;


    public PlaceView(ConstraintLayout root) {
        super(root);
        this.root =root;
        this.name = root.findViewById(R.id.placeName);
        this.score = root.findViewById(R.id.placeScore);
        this.image = root.findViewById(R.id.placeImage);
    }

    public TextView getName() {
        return name;
    }

    public TextView getScore() {
        return score;
    }

    public ImageView getImage() {
        return image;
    }
}
