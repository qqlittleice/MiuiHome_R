package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class WidgetActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new WidgetFragment();
    }


    public static class WidgetFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_widget;
        }

        @Override
        public void initPrefs() {

        }
    }
}
