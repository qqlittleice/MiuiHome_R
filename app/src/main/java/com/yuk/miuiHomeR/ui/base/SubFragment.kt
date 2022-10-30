package com.yuk.miuiHomeR.ui.base

import android.os.Bundle

abstract class SubFragment : BasePreferenceFragment() {

    private var mContentResId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        mContentResId = getContentResId()
        if (mContentResId != 0) {
            super.onCreate(savedInstanceState, mContentResId)
            addPreferencesFromResource(mContentResId)
        } else {
            super.onCreate(savedInstanceState)
        }
        initPrefs()
    }

    abstract fun getContentResId(): Int
    abstract fun initPrefs()
}