package co.edu.icesi.placesapp;

import android.content.Context;
import android.content.SharedPreferences;
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

import co.edu.icesi.placesapp.model.Place;

public class PlacesAdapter extends RecyclerView.Adapter<PlaceView> {

    private List<Place> places;
    private MainActivity mainActivity;

    public PlacesAdapter(MainActivity mainActivity) {
        places = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    public void addPlace(Place place){
        places.add(place);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.place_row, null);
        ConstraintLayout rowroot= (ConstraintLayout) root;
        PlaceView placeView = new PlaceView(rowroot);
        return placeView;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceView holder, int position) {
        holder.getName().setText(places.get(position).getName());
        holder.getScore().setText(""+places.get(position).getScore());
        Bitmap bitMap = BitmapFactory.decodeFile(places.get(position).getImages().get(0));
        holder.getImage().setImageBitmap(bitMap);

        holder.getSeeBtn().setOnClickListener(
                (view) -> {
                    // ir al mapa centrado en la posicion del place
                    SharedPreferences sp = mainActivity.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
                    sp.edit().putString("from", "searchItemFragment").apply();
                    Place place = places.get(position);
                    sp.edit().putString("latPlace", place.getLat()+"").apply();
                    sp.edit().putString("lngPlace", place.getLng()+"").apply();
                    Log.e(">>>", "Go to " + place.getLat() + "," + place.getLng());
                    mainActivity.setSelectedFragment(R.id.mapItem);
                }
        );
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
        this.notifyDataSetChanged();
    }
}
