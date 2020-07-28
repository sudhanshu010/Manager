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
    private long ratingId;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public MechRating()
    {

    }

    public MechRating(float stars, String reviews, String revDate, Manager manager, long ratingId) {
        this.stars = stars;
        this.reviews = reviews;
        this.revDate = revDate;
        this.manager = manager;
        this.ratingId = ratingId;
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

    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String , Object> result = new HashMap<>();
        result.put("stars", stars);
        result.put("reviews", reviews);
        result.put("date", revDate);
        result.put("manager", manager);
        result.put("ratingId", ratingId);

        return result;
    }


}
