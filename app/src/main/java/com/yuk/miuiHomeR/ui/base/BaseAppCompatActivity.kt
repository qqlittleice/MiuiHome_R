package com.yuk.miuiHomeR.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.utils.AppManager
import com.yuk.miuiHomeR.utils.ktx.getLocale
import com.yuk.miuiHomeR.utils.ktx.setLocale
import moralnorm.appcompat.app.AppCompatActivity
import moralnorm.internal.utils.ViewUtils
import java.util.Locale

abstract class BaseAppCompatActivity : AppCompatActivity() {

    private var mCurrentLocale: Locale? = null
    override fun onStart() {
        super.onStart()
        mCurrentLocale = resources.configuration.locales[0]
    }

    override fun onRestart() {
        super.onRestart()
        val locale: Locale = getLocale(this)
        if (locale != mCurrentLocale) {
            mCurrentLocale = locale
            recreate()
        }
    }

    override fun attachBaseContext(baseContext: Context) {
        super.attachBaseContext(setLocale(baseContext, getLocale(baseContext)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (ViewUtils.isNightMode(this)) R.style.AppTheme_Dark else R.style.AppTheme)
        super.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)
        setContentView(R.layout.activity_main)
        val res = resources
        val conf = res.configuration
        window.decorView.layoutDirection = conf.layoutDirection
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, initFragment()).commit()
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