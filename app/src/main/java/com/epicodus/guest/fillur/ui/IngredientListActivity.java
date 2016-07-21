package com.epicodus.guest.fillur.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.adapters.IngredientsAdapter;
import com.epicodus.guest.fillur.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IngredientListActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.search) Button mSearch;

    private ArrayList<String> mIngredients = new ArrayList<>();
    private ChildEventListener mChildEventListener;
    private IngredientsAdapter mAdapter;
    private static ArrayList<String> myIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        ButterKnife.bind(this);

        mSearch.setOnClickListener(this);
        IngredientListActivity.resetIngredient();
        
        getIngredients();
    }

    private void getIngredients() {

        Query query = FirebaseDatabase.getInstance().getReference("ingredients");

        mChildEventListener = query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mIngredients.add(dataSnapshot.getValue(String.class));
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
}
