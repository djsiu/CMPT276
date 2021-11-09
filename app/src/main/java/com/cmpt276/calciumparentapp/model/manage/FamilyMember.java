package com.cmpt276.calciumparentapp.model.manage;

public class FamilyMember {

    private String name;
    private final int key;
    private boolean deleted;

    FamilyMember(String name, int key) {
        this.name = name;
        this.key = key;
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
