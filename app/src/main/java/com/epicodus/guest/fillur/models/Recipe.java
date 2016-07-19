package com.epicodus.guest.fillur.models;

import java.util.ArrayList;

/**
 * Created by Guest on 7/19/16.
 */
public class Recipe {



    public String title;
    public String imageUrl;
    public String id;
    public String publisher;
    public String sourceUrl;



    public ArrayList<String> ingredients;

    public Recipe(String title, String imageUrl, String id, String publisher) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.id = id;
        this.publisher = publisher;
    }

    public Recipe(String title, String imageUrl, String id, String publisher, String sourceUrl, ArrayList<String> ingredients) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.id = id;
        this.publisher = publisher;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

}
