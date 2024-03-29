package co.edu.icesi.placesapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.UUID;

import co.edu.icesi.placesapp.MainActivity;
import co.edu.icesi.placesapp.PlaceImagesAdapter;
import co.edu.icesi.placesapp.R;
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

        String[] options = {"Camera", "Gallery"};
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
            file = new File(getContext().getExternalFilesDir(null), "/photo" + UUID.randomUUID().toString() + ".png");
            Uri uri = data.getData();
            String imagePath = UtilDomi.getPath(getContext(),uri);
            try {
                copyFile(new File(imagePath), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePaths.add(file.getAbsolutePath());
            adapter.addImages(imagePaths);
            Log.e(">>>>", "path original : "+ imagePath);
        }
    }

    /* https://stackoverflow.com/a/21496955/14263660
     * */
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    @Override
    public void onClick(View v) {
        MainActivity activity = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.mapBtn:
                sp.edit().putString("from", "newItemFragment").apply();
                sp.edit().putString("name", siteNameET.getText().toString()).apply();
                sp.edit().putString("imagePath", imagePaths.toString().replace("[", "").replace("]", "").replace(" ", "")).apply();
                activity.setSelectedFragment(R.id.mapItem);
                break;
            case R.id.addImageBtn:
                showAlertDialogButtonClicked();
            break;
            case R.id.registerBtn:
                double lat = Double.parseDouble(sp.getString("lat", "0"));
                double lng = Double.parseDouble(sp.getString("lng", "0"));
                String address =sp.getString("address", "no_address");
                siteAddress.setText(address);

                Place place = new Place(siteNameET.getText().toString(), imagePaths, 0, lat, lng,address);
                sp.edit().putString("from", "navigatorAfterRegister").apply(); // cuando le doy registrar quiero que me lleve a la vista como si diera en el navigator
                sp.edit().putString("latPlace", place.getLat()+"").apply();
                sp.edit().putString("lngPlace", place.getLng()+"").apply();
                activity.registerPlace(place);
                activity.setSelectedFragment(R.id.mapItem);
                break;
        }
    }
}