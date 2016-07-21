package com.epicodus.guest.fillur.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.ui.RecipeDetail.RecipeDetailActivity;
import com.epicodus.guest.fillur.ui.RecipeDetail.RecipeDetailFragment;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 7/19/16.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private Context mContext;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position) {
        holder.bindRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.recipeImageView) ImageView mRecipeImageView;
        @Bind(R.id.recipeTitleTextView) TextView mTitleTextView;
        @Bind(R.id.publisherTextView) TextView mPublisherTextView;
        @Bind(R.id.rankTextView) TextView mRankTextView;

        private Context mContext;
        private int mOrientation;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mOrientation = itemView.getResources().getConfiguration().orientation;

        }

        public void bindRecipe(Recipe recipe) {
            Picasso.with(mContext).load(recipe.getImageUrl()).into(mRecipeImageView);
            mTitleTextView.setText(recipe.getTitle());
            mPublisherTextView.setText(recipe.getPublisher());
            mRankTextView.setText("Rank: %" + recipe.getRank().substring(0,5));
        }

        private void createDetailFragment(int position) {
            // Creates new RestaurantDetailFragment with the given position:
            RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(mRecipes, position);
            // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            //  Replaces the FrameLayout with the RestaurantDetailFragment:
            ft.replace(R.id.restaurantDetailContainer, detailFragment);
            // Commits these changes:
            ft.commit();
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(itemPosition);
            } else{
                Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("recipes", Parcels.wrap(mRecipes));
                mContext.startActivity(intent);
            }

        }
    }
}
