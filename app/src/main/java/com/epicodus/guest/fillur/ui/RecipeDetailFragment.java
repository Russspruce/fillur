package com.epicodus.guest.fillur.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.IngredientsAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.services.Food2ForkService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {
    @Bind(R.id.recipeImageView2) ImageView mImageLabel;
    @Bind(R.id.recipeTitleTextView2) TextView mTitleLabel;
    @Bind(R.id.publisherTextView2) TextView mPublisherLabel;
    @Bind(R.id.rankTextView2) TextView mRankLabel;
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;
    @Bind(R.id.saveRecipeButton) Button mSaveRecipeButton;
    @Bind(R.id.recyclerView2) RecyclerView mRecyclerView;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;

    private Recipe mRecipe;
    private String mId;
    private IngredientsAdapter mAdapter;


    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("recipe", Parcels.wrap(recipe));
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        getRecipe();

        return view;
    }

    private void getRecipe() { //Move to RecipeListActivity?
        final Food2ForkService food2forkservice = new Food2ForkService();
        food2forkservice.getRecipe(mRecipe.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                mRecipe = food2forkservice.processRecipe(response);

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Picasso.with(getActivity())
                                .load(mRecipe.getImageUrl())
                                .resize(MAX_WIDTH, MAX_HEIGHT)
                                .centerCrop()
                                .into(mImageLabel);
                        mTitleLabel.setText(mRecipe.getTitle());
                        mPublisherLabel.setText(mRecipe.getPublisher());
                        mRankLabel.setText(mRecipe.getRank().substring(0,5));
                        mAdapter = new IngredientsAdapter(getActivity(), mRecipe.getIngredients());
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

}
