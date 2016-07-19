package com.epicodus.guest.fillur.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;
import com.epicodus.guest.fillur.models.Recipe;
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

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ingredientTextView) TextView mIngredient;

        private Context mContext;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindIngredient(String ingredient) {
            mIngredient.setText(ingredient);
        }
    }
}
