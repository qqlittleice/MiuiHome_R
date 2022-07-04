package com.yuk.miuiHomeR.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Bundle
import android.os.FileObserver
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.topjohnwu.superuser.Shell
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.provider.SharedPrefsProvider
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.PrefsUtils

class MainActivity : BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImmersionMenuEnabled(true)
        appCompatActionBar.setDisplayHomeAsUpEnabled(false)
        initData()
    }

    private fun initData() {
        val mPreferenceChangeListener =
            OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
                Log.i("prefs", "Changed: $s")
                val `val` = sharedPreferences.all[s]
                var path = ""
                when (`val`) {
                    is String -> path = "string/"
                    is Set<*> -> path = "stringset/"
                    is Int -> path = "integer/"
                    is Boolean -> path = "boolean/"
                }
                contentResolver.notifyChange(
                    Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null
                )
                if (path != "") contentResolver.notifyChange(
                    Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s),
                    null
                )
            }
        PrefsUtils.mSharedPreferences.registerOnSharedPreferenceChangeListener(
            mPreferenceChangeListener
        )
        Helpers.fixPermissionsAsync(applicationContext)
        try {
            val fileObserver: FileObserver =
                object : FileObserver(PrefsUtils.getSharedPrefsPath(), CLOSE_WRITE) {
                    override fun onEvent(event: Int, path: String?) {
                        Helpers.fixPermissionsAsync(applicationContext)
                    }
                }
            fileObserver.startWatching()
        } catch (t: Throwable) {
            Log.e("prefs", "Failed to start FileObserver!")
        }
    }

    override fun initFragment(): Fragment {
        return MainFragment()
    }

    class MainFragment : SubFragment() {
        override fun getContentResId(): Int {
            return R.xml.prefs_main
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset -> {

            }
            R.id.backup -> {

            }
            R.id.restore -> {

            }
            R.id.about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.reboot_home -> {
                Shell.cmd("pkill -f com.miui.home", "pkill -f com.yuk.miuiHomeR").exec()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}