package com.yuk.miuiHomeR.hook

import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethod
import com.yuk.miuiHomeR.utils.ktx.getIntField
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod

object InfiniteScroll : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_infinite_scroll")) return
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "getSnapToScreenIndex", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
        ) {
            if (it.args[0] !== it.result) return@hookAfterMethod
            val screenCount = it.thisObject.callMethod("getScreenCount") as Int
            if (it.args[2] as Int == -1 && it.args[0] as Int == 0) it.result = screenCount
            else if (it.args[2] as Int == 1 && it.args[0] as Int == screenCount - 1) it.result = 0
        }
        "com.miui.home.launcher.ScreenView".hookAfterMethod(
            "getSnapUnitIndex", Int::class.javaPrimitiveType
        ) {
            val mCurrentScreenIndex = it.thisObject.getIntField("mCurrentScreenIndex")
            if (mCurrentScreenIndex != it.result as Int) return@hookAfterMethod
            val screenCount = it.thisObject.callMethod("getScreenCount") as Int
            if (it.result as Int == 0) it.result = screenCount
            else if (it.result as Int == screenCount - 1) it.result = 0
        }

    }
}
