package com.epicodus.guest.fillur.ui;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.epicodus.guest.fillur.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.title) TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        Typeface calligraffitiFont = Typeface.createFromAsset(getAssets(), "fonts/Calligraffiti.ttf");
        mTitle.setTypeface(calligraffitiFont);

    }
}
