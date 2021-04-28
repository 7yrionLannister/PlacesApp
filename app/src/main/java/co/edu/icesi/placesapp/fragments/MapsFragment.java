package co.edu.icesi.placesapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.icesi.placesapp.MainActivity;
import co.edu.icesi.placesapp.R;
import co.edu.icesi.placesapp.model.Place;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean track;
    private ArrayList<Marker> markers;

    private Marker chooser;

    private ConstraintLayout confirmationPopup;

    private SharedPreferences sp;

    private Button continueBtn;
    private String from;  // me dice de donde viene el usuario si de newItemFragment o de click en el navigator

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
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        continueBtn = view.findViewById(R.id.continueBtn);
        confirmationPopup = view.findViewById(R.id.confirm_location_popup);
        return view;

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
        markers = new ArrayList<>();

        sp = getContext().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        from = sp.getString("from", "from");
        // verifico de donde viene el usuario
        if(from.equals("newItemFragment")){     // si viene desde newItemFragment debo dejarle el mapa libre para que escoja la ubicacion
            confirmationPopup.setVisibility(View.VISIBLE);
        } else if(from.equals("navigator")) {     // si viene desde el click en el navigator se activa el rastreo
            track = true;
        }

        if(from.equals("searchItemFragment")) {
            double latPlace = Double.parseDouble(sp.getString("latPlace","0"));
            double lngPlace = Double.parseDouble(sp.getString("lngPlace","0"));
            Log.e(">>>", "Center camera at " + latPlace + "," + lngPlace);
            updateMyLocation(latPlace, lngPlace);
        } else {
            setInitialPosition();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        List<Place> places = mainActivity.getPlaces();
        for(Place place : places) {
            markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())).title(place.getName())));
        }

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.setMyLocationEnabled(true); // poner un punto azul en la ubicacion actual
    }

    @SuppressLint("MissingPermission")
    public void setInitialPosition() {
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null) {
            updateMyLocation(loc.getLatitude(), loc.getLongitude());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // TODO aqui se actualiza la distancia de la persona a los sitios que ha registrado
        if(track) { // solo sigue al usuario si esta habilitado el rastreo, para que pueda manipular el mapa sin que lo obligue a ver su ubicacion siempre
            updateMyLocation(location.getLatitude(), location.getLongitude());
        }
    }

    private void updateMyLocation(double lat, double lng) {
        LatLng myPos = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 13));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    public void setTrack(boolean track) {
        this.track = track;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
       if(from.equals("newItemFragment")) {
           if(chooser == null) {
               chooser = mMap.addMarker(new MarkerOptions().position(latLng).title("Chosen place"));
           } else {
               chooser.setPosition(latLng);
           }

           continueBtn.setVisibility(View.VISIBLE);
           continueBtn.setOnClickListener(
                   v -> {
                       Geocoder geocoder = new Geocoder(getContext());
                       try {
                           List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                           String add = addresses.get(0).getAddressLine(0)+"\n";
                           sp.edit().putString("from", "MapsFragment").apply();
                           sp.edit().putString("address", add).apply();
                           LatLng pos = chooser.getPosition();
                           sp.edit().putString("lat", pos.latitude+"").apply();
                           sp.edit().putString("lng", pos.longitude+"").apply();
                           ((MainActivity) this.getActivity()).showFragment(((MainActivity) this.getActivity()).getNewItemFragment());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   });
       }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}