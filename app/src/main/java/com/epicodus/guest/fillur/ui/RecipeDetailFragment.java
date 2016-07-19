package com.epicodus.guest.fillur.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.models.Recipe;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private Recipe mRecipe;
    private String mId;


    public static RecipeDetailFragment newInstance(String recipe) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString("recipe", recipe);
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        mRecipe = gson.fromJson(getArguments().getString("recipe"), Recipe.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(view.getContext()).load(mRecipe.getImageUrl()).into(mImageLabel);

        mTitleLabel.setText(mRecipe.getTitle());
        mPublisherLabel.setText(mRecipe.getPublisher());
        mRankLabel.setText("Rank: %" + mRecipe.getRank());

        return view;
    }

}
