package com.yuk.xposedmodulebase.utils

import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class HookRegister {
    private lateinit var param: XC_LoadPackage.LoadPackageParam
    var isInit: Boolean = false
    abstract fun init()

    fun setLoadPackageParam(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        param = loadPackageParam
    }

    protected fun getLoadPackageParam(): XC_LoadPackage.LoadPackageParam {
        if (!this::param.isInitialized) {
            throw RuntimeException("lpparam should be initialized")
        }
        return param
    }

    protected fun getDefaultClassLoader(): ClassLoader {
        return getLoadPackageParam().classLoader
    }

}