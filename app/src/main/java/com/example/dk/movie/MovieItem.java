package com.example.dk.movie;

import android.graphics.Bitmap;

import java.util.Date;

class MovieItem {
    String title;
    String pubDate;
    String director;
    String actor;
    float userRating;
    Bitmap image;

    //생성자
    public MovieItem(String title) {
        this.title = title;
    }

    public MovieItem(String title, String pubDate, String director, String actor, float v) {
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor  = actor;
        this.userRating = userRating;
    }

    public MovieItem(String title, String pubDate, String director, String actor, float userRating, Bitmap image) {
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor  = actor;
        this.userRating = userRating;
        this.image = image;
    }




    //getter, setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
