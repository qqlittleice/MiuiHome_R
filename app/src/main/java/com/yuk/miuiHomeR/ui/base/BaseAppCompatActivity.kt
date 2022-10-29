package com.yuk.miuiHomeR.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.utils.AppManager
import com.yuk.miuiHomeR.utils.Helpers
import com.yuk.miuiHomeR.utils.ktx.getLocale
import moralnorm.appcompat.app.AppCompatActivity
import moralnorm.internal.utils.ViewUtils
import java.util.*

abstract class BaseAppCompatActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(Helpers.getLocaleContext(newBase))
    }

    private var mCurrentLocale: Locale? = null
    override fun onStart() {
        super.onStart()
        mCurrentLocale = resources.configuration.locale
    }

    override fun onRestart() {
        super.onRestart()
        val locale: Locale = getLocale(this)
        if (locale != mCurrentLocale) {
            mCurrentLocale = locale
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (ViewUtils.isNightMode(this)) R.style.AppTheme_Dark else R.style.AppTheme)
        super.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, initFragment())
            .commit()
    }

    abstract fun initFragment(): Fragment

    fun startActivity(activity: AppCompatActivity, cls: Class<*>) {
        startActivity(Intent(activity, cls))
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance().removeActivity(this)
    }
}