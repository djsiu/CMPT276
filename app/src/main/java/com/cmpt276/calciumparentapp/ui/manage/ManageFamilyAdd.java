package com.cmpt276.calciumparentapp.ui.manage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to add family members.
 */
public class ManageFamilyAdd extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    private ImageView profilePhotoImageView;
//    private String currentImagePath = null;
    private String mCurrentPhotoPath = "";

    Uri image_uri;
    File profilePhotoFile = null;

    private static final int GALLERY_PERMISSIONS_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSIONS_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_add);

        familyManager = FamilyMembersManager.getInstance(this);
        profilePhotoImageView = (ImageView) findViewById(R.id.profile_photo_image_view);


        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setupCameraBtn();
        setupGalleryBtn();
        setupCancelBtn();
        setupAddBtn();
    }


    private void setupCameraBtn() {
        Button openCameraBtn = findViewById(R.id.takeNewPhotoBtn);

        openCameraBtn.setOnClickListener(view -> {
            //checking for camera permission
            if(ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    openCamera();
            } else {
                ActivityCompat.requestPermissions(ManageFamilyAdd.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_CODE );
            }
        });
    }

    private void setupGalleryBtn() {
        Button openGalleryBtn = findViewById(R.id.choosePhotoBtn);

        openGalleryBtn.setOnClickListener(view -> {
            //checking for gallery permissions
            if (ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(ManageFamilyAdd.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSIONS_CODE);
            }

        });
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        //creating an empty image file to later store image in (pass by intent)
        try {
            profilePhotoFile = createImageFile();
            Log.i("CREATED PHOTO FILE: ", profilePhotoFile.getAbsolutePath());

            if (profilePhotoFile != null) {
                image_uri = FileProvider.getUriForFile(this,
                        "com.cmpt276.calciumparentapp.fileprovider",
                        profilePhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                ((Activity) this).startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        } catch (Exception ex) {
            Log.i("ERROR CREATING IMAGE FILE", "catch, unable to create image file");
        }
        ((Activity) this).startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    private void openCamera() {

        System.out.println("opening camera");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating an empty image file to later store image in (pass by intent)
        try {
            profilePhotoFile = createImageFile();
            Log.i("CREATED PHOTO FILE: ", profilePhotoFile.getAbsolutePath());

            if (profilePhotoFile != null) {
                image_uri = FileProvider.getUriForFile(this,
                        "com.cmpt276.calciumparentapp.fileprovider",
                        profilePhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                ((Activity) this).startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        } catch (Exception ex) {
            Log.i("ERROR CREATING IMAGE FILE", "catch, unable to create image file");
        }
    }

    // adapted from: https://vlemon.com/blog/android-capture-image-from-camera-and-get-image-save-path
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ProfileImage_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Need camera permissions! Change this in settings.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case GALLERY_PERMISSIONS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Need gallery permissions! Change this in settings.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(profilePhotoFile.getAbsolutePath());

            profilePhotoImageView.setImageBitmap(bitmap);
        }

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            profilePhotoImageView.setImageURI(data.getData());
        }
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelAddNewMemberButton);
        cancelBtn.setOnClickListener(view -> finish());
    }

    private void setupAddBtn() {
        Button addBtn = findViewById(R.id.addNewMemberButton);
        EditText newMemberName = findViewById(R.id.editTextFamilyMemberName);
        addBtn.setOnClickListener(view -> {

            String newMemberNameStr = newMemberName.getText().toString();
            addNewMember(newMemberNameStr);

        });
    }

    private void addNewMember(String newMemberNameStr) {

        boolean nameAlreadyExists = familyManager.isMemberNameUsed(newMemberNameStr);

        if(!nameAlreadyExists) {

            // save chosen/captured image, if no image save default image
            Bitmap profilePhotoBitmap;
            if(profilePhotoFile != null) {
                profilePhotoBitmap = BitmapFactory.decodeFile(profilePhotoFile.getAbsolutePath());
            } else {
                profilePhotoBitmap = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.default_profile_photo);
            }

            familyManager.addMember(newMemberNameStr, profilePhotoBitmap);

            String welcomeText = String.format(
                    getString(R.string.family_member_welcome_toast_text_format),
                    newMemberNameStr);

            Toast.makeText(getApplicationContext(),
                    welcomeText,
                    Toast.LENGTH_SHORT)
                    .show();
            finish();
        } else {

            String alreadyPresentText = String.format(
                    getString(R.string.family_member_present_toast_text_format),
                    newMemberNameStr);

            Toast.makeText(getApplicationContext(),
                    alreadyPresentText,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

}
//