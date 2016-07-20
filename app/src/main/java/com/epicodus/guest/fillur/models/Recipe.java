package com.epicodus.guest.fillur.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Guest on 7/19/16.
 */
@Parcel
public class Recipe {



    public String title;
    public String imageUrl;
    public String id;
    public String publisher;
    public String sourceUrl;
    public String rank;
    public ArrayList<String> ingredients;

    public Recipe() {}

    public Recipe(String title, String imageUrl, String id, String publisher, String rank){
        this.title = title;
        this.imageUrl = imageUrl;
        this.id = id;
        this.publisher = publisher;
        this.rank = rank;
    }

    public Recipe(String title, String imageUrl, String id, String publisher, String sourceUrl, ArrayList<String> ingredients, String rank) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.id = id;
        this.publisher = publisher;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
        this.rank = rank;
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
    public String getRank() {
        return rank;
    }
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

}
