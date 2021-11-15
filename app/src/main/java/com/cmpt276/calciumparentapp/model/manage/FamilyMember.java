package com.cmpt276.calciumparentapp.model.manage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Storing information relating to individual family members
 */
public class FamilyMember {

    private String name;
    private final int key;
    private boolean deleted;
    private int coinFlipPickPriority; //lower indexes pick before higher indexes
    private Bitmap profilePhotoBitmap;
    private byte[] profilePhotoByteArray;

    FamilyMember(String name, int key, int coinFlipPickPriority, Bitmap profilePhotoBitmap) {
        this.name = name;
        this.key = key;
        this.coinFlipPickPriority = coinFlipPickPriority;
        this.profilePhotoByteArray = convertBitmap(profilePhotoBitmap);
        deleted = false;
    }

    public String getMemberName() {
        String name = "";
        if(!isDeleted()) {
            name = this.name;
        }
        return name;
    }

    public FamilyMember changeName(String name) {
        this.name = name;
        return this;
    }

    public int getCoinFlipPickPriority() {
        return coinFlipPickPriority;
    }

    public void setCoinFlipPickPriority(int coinFlipPickPriority) {
        this.coinFlipPickPriority = coinFlipPickPriority;
    }

    public int getKey(){
        return key;
    }

    public void deleteChild() {
        deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Bitmap getIconBitmap() {

        return profilePhotoBitmap;
    }

    //used to allow saving to sharedpreferences
    private byte[] convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();
    }

//    private Bitmap getBitmapFromByteArray(Byte[] byteArray) {
//        byteArray = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
//        ImageView image = (ImageView) this.findViewById(R.id.ImageView);
//        image.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0 byteArray.length));
//    }
}
