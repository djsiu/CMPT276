package com.cmpt276.calciumparentapp.ui.manage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import androidx.exifinterface.media.ExifInterface;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to add family members.
 * Can add a name and profile photo from camera or gallery
 */
public class ManageFamilyAdd extends AppCompatActivity {

    private FamilyMembersManager familyManager;
    private ImageView profilePhotoImageView;

    private static final String MANAGE_FAMILY_ADD_ERROR_TAG = "MANAGE FAMILY ADD ERROR: ";

    private Bitmap profilePhotoBitmap = null;
    private File profilePhotoFile = null;
    private Uri image_uri = null;

    private static final int GALLERY_PERMISSIONS_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSIONS_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_add);

        familyManager = FamilyMembersManager.getInstance(this);
        profilePhotoImageView = findViewById(R.id.profile_photo_image_view_add);

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
        Button openCameraBtn = findViewById(R.id.take_new_photo_btn_add);

        openCameraBtn.setOnClickListener(view -> {
            //checking for camera permission
            if(ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    openCamera();
            }
            else {
                ActivityCompat.requestPermissions(ManageFamilyAdd.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_CODE );
            }
        });
    }

    private void setupGalleryBtn() {
        Button openGalleryBtn = findViewById(R.id.choose_photo_btn_add);

        openGalleryBtn.setOnClickListener(view -> {
            //checking for gallery permissions
            if (ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ManageFamilyAdd.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                ActivityCompat.requestPermissions(ManageFamilyAdd.this,
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

        //creating an empty image file to later store image in (pass by intent)
        try {
            profilePhotoFile = createImageFile();
            Log.i("CREATED PHOTO FILE: ", profilePhotoFile.getAbsolutePath());

            if (profilePhotoFile != null) {
                image_uri = FileProvider.getUriForFile(this,
                        "com.cmpt276.calciumparentapp.fileprovider",
                        profilePhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                ((Activity) this).startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        } catch (Exception ex) {
            Log.e(MANAGE_FAMILY_ADD_ERROR_TAG, "couldn't create image file.");
        }
    }

    // adapted from: https://developer.android.com/training/camera/photobasics
    // create file for use with camera
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

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && image_uri != null) {
            profilePhotoBitmap = BitmapFactory.decodeFile(profilePhotoFile.getAbsolutePath());

            // Rotates bitmap to match image sensor rotation data
            try {
                profilePhotoBitmap = fixPhotoRotation(profilePhotoFile.getAbsolutePath(), profilePhotoBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            profilePhotoBitmap = Bitmap.createScaledBitmap(profilePhotoBitmap,
                    profilePhotoBitmap.getWidth() / getCompressionRatio(profilePhotoBitmap),
                    profilePhotoBitmap.getHeight() / getCompressionRatio(profilePhotoBitmap),
                    true);
            profilePhotoImageView.setImageBitmap(profilePhotoBitmap);
        }

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && image_uri != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                profilePhotoBitmap = BitmapFactory.decodeStream(imageStream);

                // Rotates bitmap to match image sensor rotation data
                try {
                    profilePhotoBitmap = fixPhotoRotation(imageUri.getPath(), profilePhotoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                profilePhotoBitmap = Bitmap.createScaledBitmap(profilePhotoBitmap,
                        profilePhotoBitmap.getWidth() / getCompressionRatio(profilePhotoBitmap),
                        profilePhotoBitmap.getHeight() / getCompressionRatio(profilePhotoBitmap),
                        true);
                profilePhotoImageView.setImageBitmap(profilePhotoBitmap);

            } catch (FileNotFoundException e) {
                Log.i(MANAGE_FAMILY_ADD_ERROR_TAG, "couldn't find the file.");
            }
        }
    }

    private Bitmap fixPhotoRotation(String path, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private int getCompressionRatio(Bitmap bitmap) {
        final int imageResolution = bitmap.getWidth() * bitmap.getHeight();
        final int desiredResolution = 500 * 500;

        return imageResolution / desiredResolution;
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
            if(profilePhotoBitmap == null) {
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
        }
        else {

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