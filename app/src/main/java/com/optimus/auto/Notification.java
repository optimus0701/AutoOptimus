package com.optimus.auto;

/* loaded from: classes2.dex */
public class Notification {
    private String Content;
    private String Username;

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String str) {
        this.Username = str;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String str) {
        this.Content = str;
    }

    public Notification() {
    }

    public Notification(String str, String str2) {
        this.Username = str;
        this.Content = str2;
    }
}
