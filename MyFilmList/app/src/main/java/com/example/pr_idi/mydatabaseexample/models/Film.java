package com.example.pr_idi.mydatabaseexample.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Film
 * Created by pr_idi on 10/11/16.
 */
public class Film implements Serializable {

    // Basic film data manipulation class
    // Contains basic information on the film

    private long id;
    private String title;
    private String director;
    private String country;
    private int year;
    private String protagonist;
    private float critics_rate;
    private byte[] cover;
    private String comment;


    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }

    public float getCritics_rate() {
        return critics_rate;
    }

    public void setCritics_rate(float critics_rate) {
        this.critics_rate = critics_rate;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return String.format("%s - %s", title, director);
    }
}