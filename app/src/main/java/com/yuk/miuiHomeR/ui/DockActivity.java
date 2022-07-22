package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class DockActivity extends BaseAppCompatActivity {

    @Override
    public Fragment initFragment() {
        return new DockFragment();
    }

    public static class DockFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_dock;
        }

        @Override
        public void initPrefs() {

        }
    }
}
