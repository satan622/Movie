package com.example.dk.movie;

import java.util.Date;

class MovieItem {
    String title;
    String pubDate;
    String director;
    String actor;
    float userRating;
    String image;

    //생성자
    public MovieItem(String title) {
        this.title = title;
    }

    public MovieItem(String title, String pubDate) {
        this.title = title;
        this.pubDate = pubDate;
    }

    public MovieItem(String title, String pubDate, String director, String actor) {
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor  = actor;
    }

    public MovieItem(String title, String pubDate, String director, String actor, float userRating) {
        this.title = title;
        this.pubDate = pubDate;
        this.director = director;
        this.actor  = actor;
        this.userRating = userRating;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}