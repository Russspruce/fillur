package com.epicodus.guest.fillur.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.Constants;
import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.models.Recipe;
import com.epicodus.guest.fillur.ui.IngredientListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 7/19/16.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private ArrayList<String> mIngredients = new ArrayList<>();
    private Context mContext;

    public IngredientsAdapter(Context context, ArrayList<String> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public IngredientsAdapter.IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        IngredientsViewHolder viewHolder = new IngredientsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsViewHolder holder, int position) {
        holder.bindIngredient(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.ingredientTextView) TextView mIngredient;

        private Context mContext;
        private ArrayList<String> mIngredientsList;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            mIngredientsList = new ArrayList<>();
            itemView.setOnClickListener(this);
        }

        public void bindIngredient(String ingredient) {
            mIngredient.setText(ingredient);
        }

        @Override
        public void onClick(View view) {
            if(mContext.getClass().getSimpleName().equals(Constants.INGREDIENT_LIST_ACTIVITY)){
                    if (IngredientListActivity.myIngredients.indexOf(mIngredient.getText().toString()) != -1) {
                        mIngredient.setTextColor(ContextCompat.getColor(mContext, R.color.primary_dark));
                        IngredientListActivity.removeIngredient(mIngredient.getText().toString());

                    } else {
                        mIngredient.setTextColor(ContextCompat.getColor(mContext, R.color.accent));
                        IngredientListActivity.addIngredient(mIngredient.getText().toString());
                    }

            }
        }
    }
}
