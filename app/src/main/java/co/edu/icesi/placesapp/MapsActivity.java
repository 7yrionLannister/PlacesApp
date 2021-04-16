package co.edu.icesi.placesapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Marker me;
    private boolean track;
    private ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        markers = new ArrayList<>();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, this);

        setInitialPosition();

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @SuppressLint("MissingPermission")
    public void setInitialPosition() {
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null) {
            updateMyLocation(loc);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // TODO aqui se actualiza la distancia de la persona a los sitios que ha registrado
        updateMyLocation(location);
    }

    private void updateMyLocation(Location loc) {
        LatLng myPos = new LatLng(loc.getLatitude(), loc.getLongitude());
        if (me == null) {
            me = mMap.addMarker(new MarkerOptions().position(myPos).title("Yo"));
        } else {
            me.setPosition(myPos);
        }
        if(track) { // solo sigue al usuario si esta habilitado el rastreo, para que pueda manipular el mapa sin que lo obligue a ver su ubicacion siempre
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    public void setTrack(boolean track) {
        this.track = track;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("New marker"));
        markers.add(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}