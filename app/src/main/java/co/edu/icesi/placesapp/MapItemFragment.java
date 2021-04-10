package co.edu.icesi.placesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapItemFragment extends Fragment {


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
        return inflater.inflate(R.layout.fragment_map_item, container, false);
    }
}