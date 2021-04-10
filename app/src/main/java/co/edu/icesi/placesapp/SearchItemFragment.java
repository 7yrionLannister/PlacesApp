package co.edu.icesi.placesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchItemFragment extends Fragment {
    private RecyclerView placesViewList;
    private PlacesAdapter adapter;
    public SearchItemFragment() {
        // Required empty public constructor
    }


    public static SearchItemFragment newInstance() {
        SearchItemFragment fragment = new SearchItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_item, container, false);
        placesViewList = root.findViewById(R.id.placesViewList);
        placesViewList.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        placesViewList.setLayoutManager(layout);

        adapter = new PlacesAdapter();
        placesViewList.setAdapter(adapter);
        return root;
    }
}