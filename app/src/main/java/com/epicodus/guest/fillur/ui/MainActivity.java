package com.epicodus.guest.fillur.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.search) Button mSearch;
    @Bind(R.id.select) Button mSelect;
    @Bind(R.id.save) Button mSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        Typeface calligraffitiFont = Typeface.createFromAsset(getAssets(), "fonts/Calligraffiti.ttf");
        mTitle.setTypeface(calligraffitiFont);

        mSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == mSearch){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    }
}
