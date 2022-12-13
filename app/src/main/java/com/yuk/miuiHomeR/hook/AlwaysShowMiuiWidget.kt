package com.yuk.miuiHomeR.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.yuk.miuiHomeR.mPrefsMap
import de.robv.android.xposed.XC_MethodHook

object AlwaysShowMiuiWidget : BaseHook() {
    override fun init() {

        if (!mPrefsMap.getBoolean("home_widget_miui")) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        try {
            findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
                name == "buildAppWidgetsItems"
            }
        } catch (e: Exception) {
            findMethod("com.miui.home.launcher.widget.BaseWidgetsVerticalAdapter") {
                name == "buildAppWidgetsItems"
            }
        }.hookMethod {
            before {
                hook1 = findMethod("com.miui.home.launcher.widget.MIUIAppWidgetInfo") {
                    name == "initMiuiAttribute" && parameterCount == 1
                }.hookAfter {
                    it.thisObject.putObject("isMIUIWidget", false)
                }
                hook2 = findMethod("com.miui.home.launcher.MIUIWidgetUtil") {
                    name == "isMIUIWidgetSupport"
                }.hookBefore {
                    it.result = false
                }
            }
            after {
                hook1?.unhook()
                hook2?.unhook()
            }
        }
    }
}
