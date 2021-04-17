package co.edu.icesi.placesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/** <a target="_blank" href="https://icons8.com/icons/set/shop-department">Department Shop icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
 * <a href="https://iconscout.com/icons/import-contacts" target="_blank">Import contacts Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * <a href="https://iconscout.com/icons/add" target="_blank">Add Icon</a> on <a href="https://iconscout.com">Iconscout</a>
 * */
public class MainActivity extends AppCompatActivity {

    // la actividad debe tener los fragmentos que hostea
    private NewItemFragment newItemFragment;
    private MapItemFragment mapItemFragment;
    private SearchItemFragment searchItemFragment;

    private BottomNavigationView navigator;

    private static final int PERMISSIONS_CALLBACK = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigator = findViewById(R.id.navigator);

        newItemFragment = NewItemFragment.newInstance();
        mapItemFragment = MapItemFragment.newInstance();
        searchItemFragment = SearchItemFragment.newInstance();

        // se recuperan los extras en caso de que se venga de MapsActivity
        Bundle extras = getIntent().getExtras();
        Fragment toFragment = newItemFragment;
        if(extras != null) {
            String to = extras.getString("searchitem");
            if (to != null) {
                toFragment = searchItemFragment;
                navigator.setSelectedItemId(R.id.searchItem);
            }
        }

        showFragment(toFragment);
        navigator.setOnNavigationItemSelectedListener(
                (menuItem)->{
                    switch(menuItem.getItemId()){
                        case R.id.newItem:
                            showFragment(newItemFragment);
                            break;
                        case R.id.mapItem:
                            //showFragment(mapItemFragment);
                            Intent i = new Intent(this, MapsActivity.class);
                            startActivity(i);
                            finish(); // FIXME hago esto para evitar la pila de ventanas (que te puedas devolver con el boton de regresar) pero no se si es correcto
                            break;
                        case R.id.searchItem:
                            showFragment(searchItemFragment);
                            break;
                    }

                    return true; // le estoy diciendo que si estoy manejando la acción de la barra
                }
        );

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
}