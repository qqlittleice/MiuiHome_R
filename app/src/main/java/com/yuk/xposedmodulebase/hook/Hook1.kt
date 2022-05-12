package com.yuk.xposedmodulebase.hook

import com.yuk.xposedmodulebase.module.*
import com.yuk.xposedmodulebase.utils.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Hook1 : AppRegister() {
    override val packageName: String = "android"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam, Module1, Module2)
    }
}