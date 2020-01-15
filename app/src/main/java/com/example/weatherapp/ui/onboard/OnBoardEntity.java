package com.example.weatherapp.ui.onboard;

public class OnBoardEntity {
    private String title;
    private int img;

    public OnBoardEntity(String title, int img) {
        this.title = title;
        this.img = img;
    }
    public String getTitle() {
        return title;
    }

    public int getImg() {
        return img;
    }
}
