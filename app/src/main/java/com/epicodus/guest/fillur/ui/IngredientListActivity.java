package com.epicodus.guest.fillur.ui;

import android.app.Activity;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.IngredientsAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.models.User;
import com.epicodus.guest.fillur.ui.Search.SearchFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IngredientListActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.search) Button mSearch;

    private ArrayList<String> mIngredients = new ArrayList<>();
    private ChildEventListener mChildEventListener;
    private IngredientsAdapter mAdapter;
    private SearchView mSearchView;
    private DatabaseReference mDatabase;
    public static ArrayList<String> myIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        ButterKnife.bind(this);

        mSearch.setOnClickListener(this);
        IngredientListActivity.resetIngredient();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        getIngredients();
    }

    private void getIngredients() {

        Query query = FirebaseDatabase.getInstance().getReference("ingredients");

        mChildEventListener = query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildAdded: " ,dataSnapshot.getValue(String.class));
                mIngredients.add(dataSnapshot.getValue(String.class));
                Collections.reverse(mIngredients);
                mAdapter = new IngredientsAdapter(IngredientListActivity.this, mIngredients);
                mRecyclerView.setAdapter(mAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(IngredientListActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mIngredients.remove(dataSnapshot.getValue(String.class));
                Collections.reverse(mIngredients);
                mAdapter = new IngredientsAdapter(IngredientListActivity.this, mIngredients);
                mRecyclerView.setAdapter(mAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(IngredientListActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void addIngredient(String ingredient){
        myIngredients.add(ingredient);
    }

    public static void removeIngredient(String ingredient){
        myIngredients.remove(ingredient);
    }

    public static void resetIngredient(){
        myIngredients = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v == mSearch){
            Intent intent = new Intent(IngredientListActivity.this, RecipeListActivity.class);
            intent.putExtra("ingredients",android.text.TextUtils.join(",", myIngredients));
            startActivity(intent);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.action_add);
        mSearchView = (SearchView) searchViewMenuItem.getActionView();
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_add_white_24dp);
        mSearchView.setQueryHint(getString(R.string.add_ingredient));
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mSearchView.setOnQueryTextListener(this);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        if(mIngredients.indexOf(query) == -1){
            Collections.reverse(mIngredients);
            Map<String, Object> queryValue = new HashMap<>();
            for(int i = 0; i < mIngredients.size(); i++){
                queryValue.put(i+"", mIngredients.get(i));
            }
            queryValue.put(mIngredients.size()+"", query);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/ingredients/", queryValue);


            mDatabase.updateChildren(childUpdates);
        }else{
            Toast.makeText(IngredientListActivity.this, "Ingredient Alreday Exists", Toast.LENGTH_SHORT).show();
        }

        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
