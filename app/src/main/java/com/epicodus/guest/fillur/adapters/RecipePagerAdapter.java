package com.epicodus.guest.fillur.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.ui.RecipeDetail.RecipeDetailFragment;

import java.util.ArrayList;

/**
 * Created by Guest on 7/19/16.
 */
public class RecipePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Recipe> mRecipes;
    private Context mContext;
    private Recipe mRecipe;

    public RecipePagerAdapter(FragmentManager fm, ArrayList<Recipe> restaurants) {
        super(fm);
        mRecipes = restaurants;
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeDetailFragment.newInstance(mRecipes, position);
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mRecipes.get(position).getTitle();
    }

}
