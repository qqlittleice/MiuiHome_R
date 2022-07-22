package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class RecentActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new RecentFragment();
    }

    public static class RecentFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_recent;
        }

        @Override
        public void initPrefs() {

        }
    }
}
