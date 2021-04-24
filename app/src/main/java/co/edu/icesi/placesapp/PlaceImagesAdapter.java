package co.edu.icesi.placesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceImagesAdapter extends RecyclerView.Adapter<PlaceImageView> {

    private List<String> images;

    public PlaceImagesAdapter() {
        images = new ArrayList<>();
    }

    /*public void addImage(String imagePath){
        images.add(imagePath);
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public PlaceImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.place_image_row, null);
        ConstraintLayout rowroot= (ConstraintLayout) root;
        PlaceImageView placeImageView = new PlaceImageView(rowroot);
        return placeImageView;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceImageView holder, int position) {
        Bitmap bitMap = BitmapFactory.decodeFile(images.get(position));
        holder.getImage().setImageBitmap(bitMap);
        Log.e(">>>", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImages(List<String> imagePaths) {
        images.addAll(imagePaths);
        notifyDataSetChanged();
    }

    public void addImages(String[] imagePaths) {
        for(String img : imagePaths) {
            images.add(img);
        }
        notifyDataSetChanged();
    }
}
