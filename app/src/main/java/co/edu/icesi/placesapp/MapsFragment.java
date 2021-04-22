package co.edu.icesi.placesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean track;
    private ArrayList<Marker> markers;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, this);
        markers = new ArrayList<>(); // FIXME pedir de persistencia cuando este implementada

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