package com.yuk.miuiHomeR.ui.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;

import moralnorm.appcompat.app.AppCompatActivity;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (initFragment() != null ) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, initFragment())
                    .commit();
        }
    }

    public abstract Fragment initFragment();


    public void startActivity(AppCompatActivity activity, Class<?> cls) {
        startActivity(new Intent(activity,cls));
    }
}
