package com.yuk.miuiHomeR.ui

import android.content.ComponentName
import android.content.Intent
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
import com.yuk.miuiHomeR.utils.BackupUtils
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
        val mPreferenceChangeListener = OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
            Log.i("prefs", "Changed: $s")
            val `val` = sharedPreferences.all[s]
            var path = ""
            when (`val`) {
                is String -> path = "string/"
                is Set<*> -> path = "stringset/"
                is Int -> path = "integer/"
                is Boolean -> path = "boolean/"
            }
            contentResolver.notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null)
            if (path != "") contentResolver.notifyChange(
                Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s),
                null
            )
        }
        PrefsUtils.mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener)
        Helpers.fixPermissionsAsync(applicationContext)
        try {
            val fileObserver: FileObserver = object : FileObserver(PrefsUtils.getSharedPrefsPath(), CLOSE_WRITE) {
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

        override fun initPrefs() {}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val state = packageManager.getComponentEnabledSetting(ComponentName(this, this.javaClass.name + "Alias"))
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
            R.id.icon -> {
                packageManager.setComponentEnabledSetting(
                    ComponentName(this, this.javaClass.name + "Alias"),
                    if (item.title == getString(R.string.hide_icon)) PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                Shell.cmd("am force-stop com.miui.home", "am force-stop com.yuk.miuiHomeR").exec()
            }

            R.id.backup -> {
                BackupUtils.backup(this, PrefsUtils.mSharedPreferences)
            }

            R.id.restore -> {
                BackupUtils.recovery(this, PrefsUtils.mSharedPreferences)
            }

            R.id.reboot_home -> {
                Shell.cmd("am force-stop com.miui.home", "am force-stop com.yuk.miuiHomeR").exec()
            }

            R.id.about -> {
                startActivity(this, AboutActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        when (requestCode) {
            BackupUtils.CREATE_DOCUMENT_CODE -> {
                BackupUtils.handleCreateDocument(this, data.data)
            }

            BackupUtils.OPEN_DOCUMENT_CODE -> {
                BackupUtils.handleReadDocument(this, data.data)
            }
        }
    }
}