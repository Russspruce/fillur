package com.epicodus.guest.fillur.services;

/**
 * Created by Guest on 7/19/16.
 */
import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Food2ForkService {
    public static void findRecipies(String ingredients, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.SEARCH_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.KEY_PARAMETER, Constants.API_KEY)
                  .addQueryParameter(Constants.QUERY_PARAMETER, ingredients)
                  .addQueryParameter(Constants.SORT_PARAMETER, Constants.SORT_VALUE);
        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }


    public static void getRecipie(String rId, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.RECIPE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.KEY_PARAMETER, Constants.API_KEY)
                .addQueryParameter(Constants.RID_PARAMETER, rId)
                .addQueryParameter(Constants.SORT_PARAMETER, Constants.SORT_VALUE);
        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public ArrayList<Recipe> processRecipes(Response response) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject recipeResponseJSON = new JSONObject(jsonData);
                JSONArray recipesJSON = recipeResponseJSON.getJSONArray("recipes");
                for (int i = 0; i < recipesJSON.length(); i++) {
                    JSONObject recipeJSON = recipesJSON.getJSONObject(i);
                    String title = recipeJSON.getString("title");
                    String imageUrl = recipeJSON.getString("image_url");
                    String id = recipeJSON.getString("recipe_id");
                    String publisher = recipeJSON.getString("publisher");

                    Recipe recipe = new Recipe(title, imageUrl, id, publisher);

                    recipes.add(recipe);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }


}
