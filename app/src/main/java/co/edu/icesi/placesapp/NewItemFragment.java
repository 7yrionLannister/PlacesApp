package co.edu.icesi.placesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;


public class NewItemFragment extends Fragment implements View.OnClickListener {

    //State
    private EditText siteNameET;
    private ImageButton mapBtn;
    private TextView siteAddress;
    private ImageButton addImageBtn;
    private Button registerBtn;

    private File file;

    private static final int CAMERA_CALLBACK=11;
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
    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose");


        String[] options = {"Camera", "Galery"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:  //camera
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       file = new File(getContext().getExternalFilesDir(null), "photo.png");
                       Uri uri = FileProvider.getUriForFile(getContext(),getContext().getPackageName(), file);
                       i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                       startActivityForResult(i, CAMERA_CALLBACK);
                        break;
                    case 1: //galery

                        break;
                }
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapBtn:

                break;
            case R.id.addImageBtn:
                showAlertDialogButtonClicked();
            break;
            case R.id.registerBtn:
                break;

        }
    }
}