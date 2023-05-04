package com.optimus.auto;

/* loaded from: classes2.dex */
public class Report {
    private String Content;
    private String Device;
    private String Email;
    private String Username;

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String str) {
        this.Username = str;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String str) {
        this.Email = str;
    }

    public String getDevice() {
        return this.Device;
    }

    public void setDevice(String str) {
        this.Device = str;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String str) {
        this.Content = str;
    }

    public Report() {
    }

    public Report(String str, String str2, String str3, String str4) {
        this.Username = str;
        this.Email = str2;
        this.Device = str3;
        this.Content = str4;
    }
}
