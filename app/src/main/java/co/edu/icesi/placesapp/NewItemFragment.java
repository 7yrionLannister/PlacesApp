package co.edu.icesi.placesapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import co.edu.icesi.placesapp.model.Place;
import co.edu.icesi.placesapp.utils.UtilDomi;


public class NewItemFragment extends Fragment implements View.OnClickListener {

    private RecyclerView selectedImages;
    private PlaceImagesAdapter adapter;

    //State
    private EditText siteNameET;
    private ImageButton mapBtn;
    private TextView siteAddress;
    private ImageButton addImageBtn;
    private Button registerBtn;
    private File file;

    private static final int CAMERA_CALLBACK=11;
    private static final int GALLERY_CALLBACK =12;

    private String from;
    private ArrayList<String> imagePaths;

    private SharedPreferences sp;
    
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
        
        // get shared preferences
        sp = getActivity().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        
        siteNameET = root.findViewById(R.id.siteNameET);
        mapBtn = root.findViewById(R.id.mapBtn);
        siteAddress = root.findViewById(R.id.siteAddress);
        addImageBtn = root.findViewById(R.id.addImageBtn);
        registerBtn = root.findViewById(R.id.registerBtn);

        mapBtn.setOnClickListener(this);
        addImageBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        selectedImages = root.findViewById(R.id.place_images_list_view);
        selectedImages.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        selectedImages.setLayoutManager(layout);

        adapter = new PlaceImagesAdapter();
        selectedImages.setAdapter(adapter);

        imagePaths = new ArrayList<>();
        from = sp.getString("from", "no_from");

        if(from.equals("MapsFragment")){
            String address =sp.getString("address", "no_address");
            siteAddress.setText(address);
            String name = sp.getString("name", "no_name");
            siteNameET.setText(name);
            String imageP = sp.getString("imagePath", "no_path");
            Log.e(">>>>", imageP);
            if(!imageP.isEmpty()) {
                adapter.addImages(imageP.split(","));
            }
        }else if(from.equals("startApp")){

        }
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
                       file = new File(getContext().getExternalFilesDir(null), "/photo" + UUID.randomUUID().toString() + ".png");
                       Uri uri = FileProvider.getUriForFile(getContext(),getContext().getPackageName(), file);
                       i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                       startActivityForResult(i, CAMERA_CALLBACK);
                        break;
                    case 1: //galery
                        Intent j = new Intent(Intent.ACTION_GET_CONTENT);
                        j.setType("image/*");
                        startActivityForResult(j, GALLERY_CALLBACK);
                        break;
                }
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_CALLBACK && resultCode == getActivity().RESULT_OK){
            String imagePath = file.getPath();
            imagePaths.add(imagePath);
            adapter.addImages(imagePaths);
        }else if(requestCode == GALLERY_CALLBACK && resultCode == getActivity().RESULT_OK){
            Uri uri = data.getData();
            String imagePath = UtilDomi.getPath(getContext(),uri);
            imagePaths.add(imagePath);
            adapter.addImages(imagePaths);
            Log.e(">>>>", "path original : "+ imagePath);
        }
    }

    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.mapBtn:
                sp.edit().putString("from", "newItemFragment").apply();
                activity.showFragment(activity.getMapsFragment());
                sp.edit().putString("name", siteNameET.getText().toString()).apply();
                sp.edit().putString("imagePath", imagePaths.toString().replace("[", "").replace("]", "").replace(" ", "")).apply();
                break;
            case R.id.addImageBtn:
                showAlertDialogButtonClicked();
            break;
            case R.id.registerBtn:
                Place place = new Place(siteNameET.getText().toString(), imagePaths, 0);
                activity.registerPlace(place);
                break;
        }
    }
}