package com.cmpt276.calciumparentapp.model.manage;

/**
 * Storing information relating to individual family members
 */
public class FamilyMember {

    private String name;
    private final int key;
    private boolean deleted;
    private int coinFlipPickPriority; //lower indexes pick before higher indexes

    FamilyMember(String name, int key, int coinFlipPickPriority) {
        this.name = name;
        this.key = key;
        this.coinFlipPickPriority = coinFlipPickPriority;
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
}
