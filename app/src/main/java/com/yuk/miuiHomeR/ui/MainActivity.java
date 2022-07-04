package com.yuk.miuiHomeR.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.yuk.miuiHomeR.R;
import com.yuk.miuiHomeR.provider.SharedPrefsProvider;
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity;
import com.yuk.miuiHomeR.ui.base.SubFragment;
import com.yuk.miuiHomeR.utils.Helpers;
import com.yuk.miuiHomeR.utils.PrefsUtils;

import java.util.Set;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionMenuEnabled(true);
        getAppCompatActionBar().setDisplayHomeAsUpEnabled(false);
        initData();
    }

    private void initData() {
        /*requestBackup();*/
        SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = (sharedPreferences, s) -> {
            Log.i("prefs", "Changed: " + s);
            /*requestBackup();*/
            Object val = sharedPreferences.getAll().get(s);
            String path = "";
            if (val instanceof String)
                path = "string/";
            else if (val instanceof Set<?>)
                path = "stringset/";
            else if (val instanceof Integer)
                path = "integer/";
            else if (val instanceof Boolean)
                path = "boolean/";
            getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null);
            if (!path.equals(""))
                getContentResolver().notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s), null);
        };

        PrefsUtils.mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
        Helpers.fixPermissionsAsync(getApplicationContext());

        try {
            FileObserver fileObserver = new FileObserver(PrefsUtils.getSharedPrefsPath(), FileObserver.CLOSE_WRITE) {
                @Override
                public void onEvent(int event, String path) {
                    Helpers.fixPermissionsAsync(getApplicationContext());
                }
            };
            fileObserver.startWatching();
        } catch (Throwable t) {
            Log.e("prefs", "Failed to start FileObserver!");
        }
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
