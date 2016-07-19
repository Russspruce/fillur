package com.epicodus.guest.fillur.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.RecipeAdapter;
import com.epicodus.guest.fillur.adapters.RecipePagerAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.services.Food2ForkService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager) ViewPager mViewPager;
    private RecipePagerAdapter adapterViewPager;
    private String mId;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private int mStartingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Gson gson = new Gson();

        mRecipes = gson.fromJson(getIntent().getStringExtra("recipes"), ArrayList.class);

        mStartingPosition = getIntent().getIntExtra("position", 0);

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


                       adapterViewPager = new RecipePagerAdapter(getSupportFragmentManager(), mRecipe);
                        mViewPager.setAdapter(adapterViewPager);
                        mViewPager.setCurrentItem(mStartingPosition);
                    }
                });
            }
        });
    }
}
