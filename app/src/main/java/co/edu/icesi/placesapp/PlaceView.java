package co.edu.icesi.placesapp;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceView extends RecyclerView.ViewHolder {

    private ConstraintLayout root;
    private TextView name;
    private TextView score;
    private ImageView image;
    private ImageButton seeBtn;

    public PlaceView(ConstraintLayout root) {
        super(root);
        this.root =root;
        this.name = root.findViewById(R.id.placeName);
        this.score = root.findViewById(R.id.placeScore);
        this.image = root.findViewById(R.id.placeImage);
        this.seeBtn = root.findViewById(R.id.showPlaceBtn);
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

    public ImageButton getSeeBtn() {
        return seeBtn;
    }
}
