package com.optimus.auto;

/* loaded from: classes2.dex */
public class Actor {
    private String folderName;
    private String name;

    public Actor(String str, String str2) {
        this.name = str;
        this.folderName = str2;
    }

    public Actor() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }
}
