package co.edu.icesi.placesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapItemFragment extends Fragment  implements OnMapReadyCallback {

    private GoogleMap map;

    public MapItemFragment() {
        // Required empty public constructor
    }


    public static MapItemFragment newInstance() {
        MapItemFragment fragment = new MapItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_item, container, false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mapfragment);

        MapView mapView = (MapView)  view.findViewById(R.id.mapFragment);
//        if(mapFragment == null){
//            // Create new Map instance if it doesn't exist
//            mapFragment = SupportMapFragment.newInstance();
//        }
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

}