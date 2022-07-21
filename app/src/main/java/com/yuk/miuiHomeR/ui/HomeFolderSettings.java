package com.yuk.miuiHomeR.ui;

import androidx.fragment.app.Fragment;

import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;

public class HomeFolderSettings extends BaseAppCompatActivity {
    @Override
    public Fragment initFragment() {
        return new HomeFolderFragment();
    }

    public static class HomeFolderFragment extends SubFragment {

        @Override
        public int getContentResId() {
            return R.xml.prefs_home_folder;
        }

        @Override
        public void initPrefs() {

        }
    }
}
