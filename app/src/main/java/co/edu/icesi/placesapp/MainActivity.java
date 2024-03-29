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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.edu.icesi.placesapp.fragments.MapsFragment;
import co.edu.icesi.placesapp.fragments.NewItemFragment;
import co.edu.icesi.placesapp.fragments.SearchItemFragment;
import co.edu.icesi.placesapp.model.Place;

/** <a target="_blank" href="https://icons8.com/icons/set/shop-department">Department Shop icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
 * <a href="https://iconscout.com/icons/import-contacts" target="_blank">Import contacts Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * <a href="https://iconscout.com/icons/add" target="_blank">Add Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * <a target="_blank" href="undefined/icons/set/plus-math">Plus Math icon</a> icon by <a target="_blank" href="">Icons8</a>
 * */
public class MainActivity extends AppCompatActivity {

    // la actividad debe tener los fragmentos que hostea
    private NewItemFragment newItemFragment;
    private MapsFragment mapsFragment;
    private SearchItemFragment searchItemFragment;

    private BottomNavigationView navigator;
    private boolean fromFragmentFlow;

    private List<Place> places;

    private SharedPreferences sp;
    public static final String PREFERENCES = "places-data";

    private static final int PERMISSIONS_CALLBACK = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        // get shared preferences
        sp = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString("from", "startApp").apply();

        newItemFragment = NewItemFragment.newInstance();
        mapsFragment = MapsFragment.newInstance();
        searchItemFragment = SearchItemFragment.newInstance();

        loadPersistentData();
        configureNavigator();
        fromFragmentFlow = false;
        showFragment(newItemFragment);
    }

    private void loadPersistentData() {
        Gson gson = new Gson();
        String json = sp.getString("places", "no_places");
        Log.e(">>>", "places_json = " + json);
        if(json.equals("no_places")) {
            places = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<Place>>(){}.getType();
            places = gson.fromJson(json, type);
        }
        for(Place p : places) {
            Log.e(">>>", p.getName());
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

    private void showFragment(Fragment fragment){
        // todas las actividades vienen con el fragmentManager solo lo debemos llamar
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

        String from = sp.getString("from", "no_from");
        if(!fromFragmentFlow && !from.equals("navigatorAfterRegister")) {
            sp.edit().putString("from", "navigator").apply();
        }
        fromFragmentFlow = false;
    }

    public void registerPlace(Place place) {
        places.add(place);

        Gson gson = new Gson();
        String json = gson.toJson(places);
        Log.e(">>>", "places_json = " + json);

        sp.edit().putString("places", json).apply();
    }

    public List<Place> getPlaces() {
        return this.places;
    }

    public void setSelectedFragment(int id) {
        fromFragmentFlow = true;
        navigator.setSelectedItemId(id);
    }
}