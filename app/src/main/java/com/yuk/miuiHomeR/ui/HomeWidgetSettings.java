package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class HomeWidgetSettings extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new HomeWidgetFragment();
    }


    public static class HomeWidgetFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_home_widget;
        }

        @Override
        public void initPrefs() {

        }
    }
}
