package co.edu.icesi.placesapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import co.edu.icesi.placesapp.model.Place;

/** <a target="_blank" href="https://icons8.com/icons/set/shop-department">Department Shop icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
 * <a href="https://iconscout.com/icons/import-contacts" target="_blank">Import contacts Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * <a href="https://iconscout.com/icons/add" target="_blank">Add Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * */
public class MainActivity extends AppCompatActivity {

    // la actividad debe tener los fragmentos que hostea
    private NewItemFragment newItemFragment;
    private MapsFragment mapsFragment;
    private SearchItemFragment searchItemFragment;

    private BottomNavigationView navigator;
    private List<Place> places;

    private SharedPreferences sp;
    public static final String PREFERENCES = "places-data";

    private static final int PERMISSIONS_CALLBACK = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        places = new ArrayList<>();
        // get shared preferences
        sp = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString("from", "startApp").apply();
//        sp.edit().clear().apply();
        newItemFragment = NewItemFragment.newInstance();
        mapsFragment = MapsFragment.newInstance();
        searchItemFragment = SearchItemFragment.newInstance();


        loadPersistentData();
        configureNavigator();
        showFragment(newItemFragment);

    }

    private void loadPersistentData() {
        places = new ArrayList<>();

        String placesStrings = sp.getString("places", "no_places");
        if(!placesStrings.equals("no_places")) {
            placesStrings = placesStrings.replace("[", "").replace("]", "");
            String[] placesArray = placesStrings.split(",");
            for(String place : placesArray) {
                Log.e(">>> Place = ", place);
                String[] placeSplit = place.split(";");
                String name = placeSplit[0];
                String[] images = placeSplit[1].split(":");
                List<String> imgs = new ArrayList<>();
                for(String img : images) {
                    imgs.add(img);
                }
                String score = placeSplit[2];

                Place p = new Place(name, imgs, Double.parseDouble(score));
                places.add(p);
            }
        }
    }

    private void configureNavigator() {
        navigator = findViewById(R.id.navigator);

        navigator.setOnNavigationItemSelectedListener(
                (menuItem)->{
                    switch(menuItem.getItemId()){
                        case R.id.newItem:
                            showFragment(newItemFragment);
                            break;
                        case R.id.mapItem:
                            sp.edit().putString("from", "navigator").apply();
                            showFragment(mapsFragment);
                            break;
                        case R.id.searchItem:
                            showFragment(searchItemFragment);
                            break;
                    }
                    return true; // le estoy diciendo que si estoy manejando la acción de la barra
                }
        );
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_CALLBACK);
    }

    public void showFragment(Fragment fragment){
        // todas las actividades vienen con el fragmentManager solo lo debemos llamar
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    public MapsFragment getMapsFragment() {
        return this.mapsFragment;
    }
    public NewItemFragment getNewItemFragment() {
        return this.newItemFragment;
    }

    public void registerPlace(Place place) {
        places.add(place);
        sp.edit().putString("places", places.toString()).apply();
    }

    public List<Place> getPlaces(){
        return this.places;
    }
}