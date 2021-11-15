package com.cmpt276.calciumparentapp.ui.manage;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.io.ByteArrayOutputStream;

/**
 * Activity to add family members.
 */
public class ManageFamilyAdd extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    private ImageView profilePhotoImageView;
//    private String currentImagePath = null;

    Uri image_uri;

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
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(ManageFamilyAdd.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSIONS_CODE);
            }

        });
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) this).startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        File imageFile = null;

//        try {
//            imageFile = getImageFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        if (imageFile != null)
//        {
            image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            image_uri = FileProvider.getUriForFile(ManageFamilyAdd.this, "${applicationId}.fileprovider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
            ((Activity) this).startActivityForResult(intent, CAMERA_REQUEST_CODE);
//        }

    }

//    // code adapted from :https://www.semicolonworld.com/question/45928/how-to-save-a-bitmap-on-internal-storage
//    private File getImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = new File(Environment.getExternalStorageDirectory()
//                + "/Android/data/"
//                + getApplicationContext().getPackageName()
//                + "/Files");
////        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
////
////        currentImagePath = image.getAbsolutePath();
//        if (! storageDir.exists()){
//            if (! storageDir.mkdirs()){
//                return null;
//            }
//        }
//        // Create a media file name
//        File image;
//        String mImageName="MI_"+ timeStamp +".jpg";
//        image = new File(storageDir.getPath() + File.separator + mImageName);
//        return image;
//    }

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
//            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
//            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,true);
//
//
//            System.out.println("Current IMAGE PATH: " + currentImagePath);
//            System.out.println("bitmapppppp: " + bitmap);
            //profilePhotoImageView.setImageBitmap(bitmap);
            profilePhotoImageView.setImageURI(image_uri);
        }

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            profilePhotoImageView.setImageURI(data.getData());
        }

//        if(requestCode == RESULT_OK) {
//            profilePhotoImageView.setImageURI(image_uri);
//            System.out.println("setting the photo!");
//        }
    }

    private void setupAddBtn() {
        Button addBtn = findViewById(R.id.addNewMemberButton);
        EditText newMemberName = findViewById(R.id.editTextFamilyMemberName);
        addBtn.setOnClickListener(view -> {

            String newMemberNameStr = newMemberName.getText().toString();
            boolean nameAlreadyExists = familyManager.isMemberNameUsed(newMemberNameStr);

            if(!nameAlreadyExists) {
                //int profilePhotoID = R.drawable.default_profile_photo;

                Bitmap profilePhotoID = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.default_profile_photo);

                familyManager.addMember(newMemberNameStr, profilePhotoID);

                String welcomeText = String.format(
                        getString(R.string.family_member_welcome_toast_text_format),
                        newMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        welcomeText,
                        Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else {

                String alreadyPresentText = String.format(
                        getString(R.string.family_member_present_toast_text_format),
                        newMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        alreadyPresentText,
                        Toast.LENGTH_SHORT)
                        .show();
            }

        });
    }

    //convert a bitmap image to uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelAddNewMemberButton);
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
//