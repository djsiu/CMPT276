package com.cmpt276.calciumparentapp.ui.manage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to edit family members when clicking on their name
 * user can edit name and delete child
 */
public class ManageFamilyEdit extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    private static final String MANAGE_FAMILY_EDIT_ERROR_TAG = "MANAGE FAMILY EDIT ERROR: ";

    private static final int GALLERY_PERMISSIONS_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSIONS_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 201;

    private Bitmap profilePhotoBitmap = null;
    private File profilePhotoFile = null;

    private ImageView profilePhotoImageView;

    private String currentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_edit);

        familyManager = FamilyMembersManager.getInstance(this);

        // Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // make the current name appear in the editText
        currentName = getFamilyMemberName();
        EditText editTextName = findViewById(R.id.editTextMemberNameForEdit);
        editTextName.setText(currentName);

        // make the current photo appear in the imageView
        profilePhotoImageView = findViewById(R.id.profile_photo_image_view_edit);
        profilePhotoImageView.setImageBitmap(familyManager.getProfilePhotoByName(currentName));

        setupCancelBtn();
        setupSaveBtn();
        setupDeleteBtn();
        setupGalleryBtn();
        setupCameraBtn();
    }

    // retrieving the name that was clicked on in the list view
    private String getFamilyMemberName() {
        Intent intent = getIntent();
        return intent.getStringExtra(ManageFamilyMembers.EDIT_MEMBER);
    }

    private void setupCameraBtn() {
        Button openCameraBtn = findViewById(R.id.take_new_photo_btn_edit);

        openCameraBtn.setOnClickListener(view -> {
            //checking for camera permission
            if(ContextCompat.checkSelfPermission(ManageFamilyEdit.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyEdit.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                openCamera();
            }
            else {
                ActivityCompat.requestPermissions(ManageFamilyEdit.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_CODE );
            }
        });
    }

    private void setupGalleryBtn() {
        Button openGalleryBtn = findViewById(R.id.choose_new_photo_btn_edit);

        openGalleryBtn.setOnClickListener(view -> {
            //checking for gallery permissions
            if (ContextCompat.checkSelfPermission(ManageFamilyEdit.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyEdit.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                ActivityCompat.requestPermissions(ManageFamilyEdit.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSIONS_CODE);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) this).startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    // adapted from: https://vlemon.com/blog/android-capture-image-from-camera-and-get-image-save-path
    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating an empty image file to later store captured image in (pass by intent)
        try {
            profilePhotoFile = createImageFile();
            Log.i("CREATED PHOTO FILE: ", profilePhotoFile.getAbsolutePath());

            if (profilePhotoFile != null) {
                Uri image_uri = FileProvider.getUriForFile(this,
                        "com.cmpt276.calciumparentapp.fileprovider",
                        profilePhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                ((Activity) this).startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        } catch (Exception ex) {
            Log.i(MANAGE_FAMILY_EDIT_ERROR_TAG, "couldn't create image file.");
        }
    }

    // adapted from: https://developer.android.com/training/camera/photobasics
    // create image file for use with camera
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ProfileImage_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    // asking for permissions if they haven't been granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, R.string.camera_permission_toast, Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case GALLERY_PERMISSIONS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, R.string.gallery_permission_toast, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    //display image after being taken or chosen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            profilePhotoBitmap = BitmapFactory.decodeFile(profilePhotoFile.getAbsolutePath());
            profilePhotoBitmap = Bitmap.createScaledBitmap(profilePhotoBitmap,
                    profilePhotoBitmap.getWidth() / 4,
                    profilePhotoBitmap.getHeight() / 4,
                    true);

            profilePhotoImageView.setImageBitmap(profilePhotoBitmap);
        }

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                profilePhotoBitmap = BitmapFactory.decodeStream(imageStream);
                profilePhotoBitmap = Bitmap.createScaledBitmap(profilePhotoBitmap,
                        profilePhotoBitmap.getWidth() / 4,
                        profilePhotoBitmap.getHeight() / 4,
                        true);
                profilePhotoImageView.setImageBitmap(profilePhotoBitmap);

            } catch (FileNotFoundException e) {
                Log.i(MANAGE_FAMILY_EDIT_ERROR_TAG, "couldn't find the file.");
            }
        }
    }

    private void setupDeleteBtn() {
        Button deleteBtn = findViewById(R.id.deleteMemberBtn);

        deleteBtn.setOnClickListener(view -> {
            Log.i("edit", "setupDeleteBtn: before delete");
            familyManager.deleteMember(currentName);

            finish();
        });
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveEditMemberBtn);

        EditText editMemberName = findViewById(R.id.editTextMemberNameForEdit);
        saveBtn.setOnClickListener(view -> {
            String newMemberNameStr = editMemberName.getText().toString();
            boolean nameAlreadyExists = familyManager.isMemberNameUsed(newMemberNameStr);

            if(currentName.equals(newMemberNameStr) || !nameAlreadyExists) {

                if(profilePhotoBitmap != null) {
                    familyManager.changeMemberPhoto(currentName,
                            profilePhotoBitmap);
                }
                familyManager.changeMemberName(
                        editMemberName.getText().toString(),
                        currentName);

                finish();
            }
            else {
                String alreadyPresentText = String.format(
                        getString(R.string.family_member_present_toast_text_format),
                        editMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        alreadyPresentText,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelEditMember);
        cancelBtn.setOnClickListener(view -> finish());
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