package com.yuk.miuiHomeR.hook

import android.content.res.Resources
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ResourcesHookData
import com.yuk.miuiHomeR.utils.ResourcesHookMap
import com.yuk.miuiHomeR.utils.ktx.dp2px
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.hookBeforeAllMethods
import com.yuk.miuiHomeR.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook

object ResourcesHook : BaseHook() {
    private val hookMap = ResourcesHookMap<String, ResourcesHookData>()
    private fun hook(param: XC_MethodHook.MethodHookParam) {
        try {
            val resName = appContext.resources.getResourceEntryName(param.args[0] as Int)
            val resType = appContext.resources.getResourceTypeName(param.args[0] as Int)
            if (hookMap.isKeyExist(resName))
                if (hookMap[resName]?.type == resType) {
                    param.result = hookMap[resName]?.afterValue
                }
        } catch (ignore: Exception) {
        }
    }

    override fun init() {

        Resources::class.java.hookBeforeMethod("getBoolean", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimension", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelOffset", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelSize", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getInteger", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getText", Int::class.javaPrimitiveType) { hook(it) }

        val value = mPrefsMap.getInt("task_view_corners", -1).toFloat()
        val value1 = mPrefsMap.getInt("task_view_header_height", -1).toFloat()

        if (mPrefsMap.getBoolean("home_unlock_grids")) {
            val deviceClass = "com.miui.home.launcher.compat.LauncherCellCountCompatDevice".findClass()
            deviceClass.hookBeforeAllMethods("shouldUseDeviceValue") { it.result = false }
            hookMap["config_cell_count_x"] = ResourcesHookData("integer", 3)
            hookMap["config_cell_count_y"] = ResourcesHookData("integer", 4)
            hookMap["config_cell_count_x_min"] = ResourcesHookData("integer", 3)
            hookMap["config_cell_count_y_min"] = ResourcesHookData("integer", 4)
            hookMap["config_cell_count_x_max"] = ResourcesHookData("integer", 16)
            hookMap["config_cell_count_y_max"] = ResourcesHookData("integer", 18)
        }

        if (value != -1f) {
            hookMap["recents_task_view_rounded_corners_radius_min"] = ResourcesHookData("dimen", dp2px(value))
            hookMap["recents_task_view_rounded_corners_radius_max"] = ResourcesHookData("dimen", dp2px(value))
        }

        if (value1 != -1f) hookMap["recents_task_view_header_height"] = ResourcesHookData("dimen", dp2px(value1))

    }
}