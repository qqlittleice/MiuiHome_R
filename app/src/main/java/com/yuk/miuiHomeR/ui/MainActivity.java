package com.yuk.miuiHomeR.ui;

import android.os.Bundle;
import android.view.Menu;

import androidx.fragment.app.Fragment;


import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionMenuEnabled(true);
        getAppCompatActionBar().setDisplayHomeAsUpEnabled(false);
        initView();
    }

    private void initView() {
    }

    @Override
    public Fragment initFragment() {
        return new MainFragment();
    }


    public static class MainFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_main;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
