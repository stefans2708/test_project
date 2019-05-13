package com.android.stefan.testproject;

public class NetworkInfo {

    private String ssid;
    private String bssid;
    private int level;
    private String password;
    private boolean isSecured;

    public NetworkInfo(String ssid, String bssid, int level, boolean isSecured) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
        this.isSecured = isSecured;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public void setSecured(boolean secured) {
        isSecured = secured;
    }
}
