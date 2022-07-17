package com.yuk.miuiHomeR.ui

import android.content.ComponentName
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
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
import com.yuk.miuiHomeR.utils.DeviceUtils
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.PrefsUtils
import moralnorm.preference.PreferenceCategory
import moralnorm.preference.SwitchPreference

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

        override fun initPrefs() {
            val mPadDockBlur = findPreference<SwitchPreference>("prefs_key_pad_dock_blur")
            val mHomeDockSettings = findPreference<PreferenceCategory>("prefs_key_home_dock_settings")

            mHomeDockSettings.isVisible = !DeviceUtils.isPad()
            mPadDockBlur.isVisible = DeviceUtils.isPad()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val state = packageManager.getComponentEnabledSetting(
            ComponentName(
                this, this.javaClass.name + "Alias"
            )
        )
        menu.findItem(R.id.icon).title = when (state) {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED -> {
                getString(R.string.show_icon)
            }
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> {
                getString(R.string.hide_icon)
            }
            else -> getString(R.string.hide_icon)
        }
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
            R.id.icon -> {
                packageManager.setComponentEnabledSetting(
                    ComponentName(this, this.javaClass.name + "Alias"),
                    if (item.title == "隐藏图标") PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
            R.id.about -> {
                startActivity(this, AboutActivity::class.java)
            }
            R.id.reboot_home -> {
                Shell.cmd("pkill -f com.miui.home", "pkill -f com.yuk.miuiHomeR").exec()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}