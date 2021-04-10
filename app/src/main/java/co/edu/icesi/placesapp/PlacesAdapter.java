package co.edu.icesi.placesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlaceView> {

    private List<Place> places;

    public PlacesAdapter() {
        places = new ArrayList<>();
        places.add(new Place("ches", "im", 4.5));
    }

    public void addPlace(Place place){
        places.add(place);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PlaceView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.placerow, null);
        ConstraintLayout rowroot= (ConstraintLayout) root;
        PlaceView placeView = new PlaceView(rowroot);
        return placeView;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceView holder, int position) {
        holder.getName().setText(places.get(position).getName());
        holder.getScore().setText(""+places.get(position).getScore());
        Bitmap bitMap = BitmapFactory.decodeFile("/drawable/add.png");
        holder.getImage().setImageBitmap(bitMap);

    }

    @Override
    public int getItemCount() {
        return places.size();
    }




}
