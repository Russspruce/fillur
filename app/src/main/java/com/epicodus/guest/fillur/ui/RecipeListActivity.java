
package com.epicodus.guest.fillur.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.RecipeAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.services.Food2ForkService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeListActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.error) TextView mErrorText;

    private RecipeAdapter mAdapter;
    public ArrayList<Recipe> mRecipes = new ArrayList<>();

    public String mIngredients;
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        mIngredients = getIntent().getStringExtra("ingredients");
        createAuthProgressDialog();

        getRecipes();
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Retrieving Recipes...");
        mAuthProgressDialog.setCancelable(false);
    }

    private void getRecipes() {
        mAuthProgressDialog.show();
        final Food2ForkService food2ForkService = new Food2ForkService();

        food2ForkService.findRecipes(mIngredients, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRecipes = food2ForkService.processRecipes(response);
                RecipeListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAuthProgressDialog.dismiss();
                        if(mRecipes.size() == 0){
                            mErrorText.setVisibility(View.VISIBLE);
                        }else{

                            mAdapter = new RecipeAdapter(RecipeListActivity.this, mRecipes);
                            mRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeListActivity.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
                        }

                    }
                });


            }
        });
    }
}
