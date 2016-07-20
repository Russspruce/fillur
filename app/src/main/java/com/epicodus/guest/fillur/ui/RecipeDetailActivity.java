package com.epicodus.guest.fillur.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.IngredientsAdapter;
import com.epicodus.guest.fillur.adapters.RecipeAdapter;
import com.epicodus.guest.fillur.adapters.RecipePagerAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.services.Food2ForkService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeDetailActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.recipeImageView2) ImageView mImageLabel;
    @Bind(R.id.recipeTitleTextView2) TextView mTitleLabel;
    @Bind(R.id.publisherTextView2) TextView mPublisherLabel;
    @Bind(R.id.rankTextView2) TextView mRankLabel;
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;
    @Bind(R.id.saveRecipeButton) Button mSaveRecipeButton;
    @Bind(R.id.recyclerView2) RecyclerView mRecyclerView;
    private IngredientsAdapter mAdapter;
    private Recipe mRecipe;
    private String mId;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        mId = getIntent().getStringExtra("id");

        mWebsiteLabel.setOnClickListener(this);
        mSaveRecipeButton.setOnClickListener(this);

        getRecipe();

    }

    private void getRecipe() {
        final Food2ForkService food2forkservice = new Food2ForkService();
        food2forkservice.getRecipe(mId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                mRecipe = food2forkservice.processRecipe(response);

                RecipeDetailActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Picasso.with(RecipeDetailActivity.this)
                                .load(mRecipe.getImageUrl())
                                .resize(MAX_WIDTH, MAX_HEIGHT)
                                .centerCrop()
                                .into(mImageLabel);
                        mTitleLabel.setText(mRecipe.getTitle());
                        mPublisherLabel.setText(mRecipe.getPublisher());
                        mRankLabel.setText(mRecipe.getRank().substring(0,5));
                        mAdapter = new IngredientsAdapter(getApplicationContext(), mRecipe.getIngredients());
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeDetailActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mSaveRecipeButton){
        }
        if(v == mWebsiteLabel){
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRecipe.getSourceUrl()));
            startActivity(webIntent);
        }
    }
}
