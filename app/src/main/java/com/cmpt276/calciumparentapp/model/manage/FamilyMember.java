package com.cmpt276.calciumparentapp.model.manage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Storing information relating to individual family members
 */
public class FamilyMember {

    private String name;
    private final int key;
    private boolean deleted;
    private int coinFlipPickPriority; //lower indexes pick before higher indexes
    private String encodedBitmap;

    FamilyMember(String name, int key, Bitmap profilePhotoBitmap) {
        this.name = name;
        this.key = key;
        this.coinFlipPickPriority = coinFlipPickPriority;
        encodedBitmap = encodeToBase64(profilePhotoBitmap);
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

    public FamilyMember changeImage(Bitmap photo) {
        this.encodedBitmap = encodeToBase64(photo);
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

    public Bitmap getProfileBitmap() {
        return decodeToBase64(encodedBitmap);
    }

    // encode/decode bitmap so it can be opened between saves of the app
    // adapted from: https://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android
    public  String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public  Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
