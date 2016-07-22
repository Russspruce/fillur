package com.epicodus.guest.fillur.ui.Search;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.RecipeAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.services.Food2ForkService;

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
public class SearchFragment extends Fragment {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.error) TextView mErrorText;

    private RecipeAdapter mAdapter;
    public ArrayList<Recipe> mRecipes = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentSearch;

    private ProgressDialog mAuthProgressDialog;

    public String mIngredients;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();

        createAuthProgressDialog();

        setHasOptionsMenu(true);

    }
    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Retrieving Recipes...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mRecentSearch = mSharedPreferences.getString(Constants.LAST_SEARCH, null);

        if (mRecentSearch != null) {
            getRecipes(mRecentSearch);
        }
        return view;
    }

    private void getRecipes(String search) {
        mAuthProgressDialog.show();
        final Food2ForkService food2ForkService = new Food2ForkService();

        food2ForkService.findRecipes(search, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRecipes = food2ForkService.processRecipes(response);
                Log.d("onResponse: " , mRecipes.size()+"");
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAuthProgressDialog.dismiss();
                        if(mRecipes.size() == 0){
                            mErrorText.setVisibility(View.VISIBLE);
                        }else{
                            mAdapter = new RecipeAdapter(getActivity(), mRecipes);
                            mRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
                        }

                    }
                });


            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_search, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addToSharedPreferences(String lastSearch) {
        mEditor.putString(Constants.LAST_SEARCH, lastSearch).apply();
    }

}
