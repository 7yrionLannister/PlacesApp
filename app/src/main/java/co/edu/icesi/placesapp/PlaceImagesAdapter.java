package co.edu.icesi.placesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceImagesAdapter extends RecyclerView.Adapter<PlaceImageView> {

    private List<String> images;

    public PlaceImagesAdapter() {
        images = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlaceImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.place_image_row, parent, false);
        PlaceImageView placeImageView = new PlaceImageView(root);
        return placeImageView;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceImageView holder, int position) {
        Bitmap bitMap = BitmapFactory.decodeFile(images.get(position));
        holder.getImage().setImageBitmap(bitMap);
        Log.e(">>>", "onBindViewHolder" + position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImages(List<String> imagePaths) {
        for(String img : imagePaths) {
            if(!images.contains(img)) {
                images.add(img);
            }
        }
        notifyDataSetChanged();
    }

    public void addImages(String[] imagePaths) {
        for(String img : imagePaths) {
            if(!images.contains(img)) {
                images.add(img);
            }
        }
        notifyDataSetChanged();
    }
}
