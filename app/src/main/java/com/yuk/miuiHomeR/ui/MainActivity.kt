package com.yuk.miuiHomeR.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Bundle
import android.os.FileObserver
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.provider.SharedPrefsProvider
import com.yuk.miuiHomeR.ui.base.BaseAppCompatActivity
import com.yuk.miuiHomeR.ui.base.SubFragment
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.PrefsUtils
import com.yuk.miuiHomeR.utils.ktx.execShell
import moralnorm.preference.Preference
import java.io.File

class MainActivity : BaseAppCompatActivity() {

    private var fileObserver: FileObserver? = null
    private var mPreferenceChangeListener: OnSharedPreferenceChangeListener? = null
    private var mActionBarMoreView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActionBar.setDisplayHomeAsUpEnabled(intent.getBooleanExtra("homeAsUpEnabled", false))
        initView()
        initData()
    }

    private fun initView() {
        mActionBarMoreView = ImageView(this)
        mActionBarMoreView!!.setImageResource(R.drawable.ic_settings)
        val actionBar = appCompatActionBar
        actionBar.endView = mActionBarMoreView
        mActionBarMoreView!!.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }
    }

    private fun initData() {
        mPreferenceChangeListener = OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
            Log.i("prefs", "Changed: $s")
            val `val` = sharedPreferences.all[s]
            var path = ""
            if (`val` is String) path = "string/" else if (`val` is Set<*>) path = "stringset/" else if (`val` is Int) path =
                "integer/" else if (`val` is Boolean) path = "boolean/"
            contentResolver.notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/" + path + s), null)
            if (path != "") contentResolver.notifyChange(Uri.parse("content://" + SharedPrefsProvider.AUTHORITY + "/pref/" + path + s), null)
        }
        PrefsUtils.mSharedPreferences?.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener)
        Helpers.fixPermissionsAsync(applicationContext)
        try {
            fileObserver = object : FileObserver(File(PrefsUtils.sharedPrefsPath), CLOSE_WRITE) {
                override fun onEvent(event: Int, path: String?) {
                    Helpers.fixPermissionsAsync(applicationContext)
                }
            }
            fileObserver?.startWatching()
        } catch (t: Throwable) {
            Log.e("prefs", "Failed to start FileObserver!")
        }
    }

    override fun initFragment(): Fragment {
        return MainFragment()
    }

    class MainFragment : SubFragment(), Preference.OnPreferenceClickListener {
        var mRebootHome: Preference? = null
        override fun getContentResId(): Int {
            return R.xml.prefs_main
        }

        override fun initPrefs() {
            mRebootHome = findPreference("reboot_home")
            mRebootHome?.onPreferenceClickListener = this
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (preference === mRebootHome) execShell("am force-stop com.miui.home && am force-stop com.yuk.miuiHomeR")
            return false
        }
    }
}