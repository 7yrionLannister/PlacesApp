package co.edu.icesi.placesapp;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean track;
    private ArrayList<Marker> markers;
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
        markers = new ArrayList<>(); // FIXME pedir de persistencia cuando este implementada

        sp = getContext().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        from = sp.getString("from", "from");
        // verifico de donde viene el usuario
        if(from.equals("newItemFragment")){     // si viene desde newItemFragment debo dejarle el mapa libre para que escoja la ubicacion
            op1();
        }else if(from.equals("navigator")){     // si viene desde el click en el navigator se le muestran los lugares
            op2();
        }
        setInitialPosition();

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.setMyLocationEnabled(true); // poner un punto azul en la ubicacion actual
    }
    // viene de click en el icono mapa de newItemFragment
    public void op1(){
        confirmationPopup.setVisibility(View.VISIBLE);
    }
    // viene de dar click en mapa del navigation bar
    public void op2(){
//        Toast toast =Toast.makeText(getContext(),"From navigator",Toast.LENGTH_SHORT);
//        toast.show();
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    public void setTrack(boolean track) {
        this.track = track;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
       if(from.equals("newItemFragment")) {
           Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("New marker"));
           markers.add(marker);

           continueBtn.setVisibility(View.VISIBLE);
           continueBtn.setOnClickListener(
                   v -> {
                       Geocoder geocoder = new Geocoder(getContext());
                       try {
                           List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                           String add = addresses.get(0).getAddressLine(0)+"\n";
                           sp.edit().putString("from", "MapsFragment").apply();
                           sp.edit().putString("address", add).apply();
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