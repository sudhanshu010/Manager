package com.example.manager.models;

import com.google.firebase.database.Exclude;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

@Parcel
public class MechRating implements Cloneable{

    private float stars;
    private String reviews, revDate;
    private Manager manager;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public MechRating()
    {

    }

    public MechRating(float stars, String reviews, String revDate, Manager manager) {
        this.stars = stars;
        this.reviews = reviews;
        this.revDate = revDate;
        this.manager = manager;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getRevDate() {
        return revDate;
    }

    public void setRevDate(String revDate) {
        this.revDate = revDate;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String , Object> result = new HashMap<>();
        result.put("stars", stars);
        result.put("reviews", reviews);
        result.put("date", revDate);
        result.put("manager", manager);

        return result;
    }


}
