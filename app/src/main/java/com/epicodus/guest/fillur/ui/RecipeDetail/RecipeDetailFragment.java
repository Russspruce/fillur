package com.epicodus.guest.fillur.ui.RecipeDetail;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.IngredientsAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.models.User;
import com.epicodus.guest.fillur.services.Food2ForkService;
import com.epicodus.guest.fillur.ui.Search.SearchFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements View.OnClickListener{
    @Bind(R.id.recipeImageView2) ImageView mImageLabel;
    @Bind(R.id.recipeTitleTextView2) TextView mTitleLabel;
    @Bind(R.id.publisherTextView2) TextView mPublisherLabel;
    @Bind(R.id.rankTextView2) TextView mRankLabel;
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;
    @Bind(R.id.saveRecipeButton) Button mSaveRecipeButton;
    @Bind(R.id.recyclerView2) RecyclerView mRecyclerView;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    private int mPosition;
    private Recipe mRecipe;
    private User mCurrentUser;
    private ArrayList<Recipe> mRecipes;
    private String mId;
    private IngredientsAdapter mAdapter;
    private SharedPreferences mSharedPreferences;


    public static RecipeDetailFragment newInstance(ArrayList<Recipe> recipes, Integer position) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(recipes));
        args.putInt(Constants.EXTRA_KEY_POSITION, position);
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipes = Parcels.unwrap(getArguments().getParcelable(Constants.EXTRA_KEY_RECIPES));
        mPosition = getArguments().getInt(Constants.EXTRA_KEY_POSITION);
        mRecipe = mRecipes.get(mPosition);
        Gson gson = new Gson();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = mSharedPreferences.getString("currentUser", null);
        mCurrentUser = gson.fromJson(json, User.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        mWebsiteLabel.setOnClickListener(this);
        mSaveRecipeButton.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        if(v == mSaveRecipeButton){
            String uid = mCurrentUser.getId();

            DatabaseReference restaurantRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_RECIPES)
                    .child(uid);

            DatabaseReference pushRef = restaurantRef.push();
            String pushId = pushRef.getKey();
            mRecipe.setPushId(pushId);
            pushRef.setValue(mRecipe);

            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        if(v == mWebsiteLabel){
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRecipe.getSourceUrl()));
            startActivity(webIntent);
              }
        }
}
