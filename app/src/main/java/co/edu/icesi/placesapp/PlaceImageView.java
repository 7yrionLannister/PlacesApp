package co.edu.icesi.placesapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import co.edu.icesi.placesapp.R;

public class PlaceImageView extends RecyclerView.ViewHolder {

    private View root;
    private ImageView image;


    public PlaceImageView(View root) {
        super(root);
        this.root =root;
        this.image = root.findViewById(R.id.image);
    }

    public ImageView getImage() {
        return image;
    }
}
