package com.yuk.miuiHomeR.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yuk.miuiHomeR.R
import com.yuk.miuiHomeR.utils.ktx.getLocale
import com.yuk.miuiHomeR.utils.ktx.setLocale
import moralnorm.appcompat.app.AppCompatActivity
import moralnorm.internal.utils.ViewUtils

abstract class BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (ViewUtils.isNightMode(this)) R.style.AppTheme_Dark else R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, initFragment())
            .commit()
        setLocale(resources, getLocale(applicationContext))
    }

    abstract fun initFragment(): Fragment

    fun startActivity(activity: AppCompatActivity, cls: Class<*>) {
        startActivity(Intent(activity, cls))
    }
}