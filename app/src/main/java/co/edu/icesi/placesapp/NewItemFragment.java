package co.edu.icesi.placesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class NewItemFragment extends Fragment implements View.OnClickListener {

    //State
    private EditText siteNameET;
    private ImageButton mapBtn;
    private TextView siteAddress;
    private ImageButton addImageBtn;
    private Button registerBtn;
    public NewItemFragment() {
        // Required empty public constructor
    }


    public static NewItemFragment newInstance() {
        NewItemFragment fragment = new NewItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_item, container, false);
        siteNameET = root.findViewById(R.id.siteNameET);
        mapBtn = root.findViewById(R.id.mapBtn);
        siteAddress = root.findViewById(R.id.siteAddress);
        addImageBtn = root.findViewById(R.id.addImageBtn);
        registerBtn = root.findViewById(R.id.registerBtn);

        mapBtn.setOnClickListener(this);
        addImageBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mapBtn:

            break;
            case R.id.addImageBtn:
                break;
            case R.id.registerBtn:
                break;

        }
    }
}