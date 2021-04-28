package co.edu.icesi.placesapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.edu.icesi.placesapp.MainActivity;
import co.edu.icesi.placesapp.PlacesAdapter;
import co.edu.icesi.placesapp.R;

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

        adapter = new PlacesAdapter((MainActivity) getActivity());
        placesViewList.setAdapter(adapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        adapter.setPlaces(activity.getPlaces());
    }
}