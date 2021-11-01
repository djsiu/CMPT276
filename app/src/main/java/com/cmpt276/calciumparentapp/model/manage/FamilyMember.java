package com.cmpt276.calciumparentapp.model.manage;

public class FamilyMember {

    String name;

    FamilyMember(String name) {
        this.name = name;
    }

    public String getMemberName() {
        return name;
    }

    public FamilyMember changeName(String name) {
        this.name = name;
        return this;
    }
}
