package com.yuk.miuiHomeR.hook

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuiHomeR.mPrefsMap
import com.yuk.miuiHomeR.utils.ktx.callMethodAs
import com.yuk.miuiHomeR.utils.ktx.findClass
import com.yuk.miuiHomeR.utils.ktx.getObjectFieldAs
import com.yuk.miuiHomeR.utils.ktx.hookAfterMethod

object AppDrawer : BaseHook() {
    override fun init() {

        if (mPrefsMap.getBoolean("home_hide_category_all")) {
            try {
                findMethod("com.miui.home.launcher.allapps.category.BaseAllAppsCategoryListContainer") {
                    name == "buildSortCategoryList"
                }
            } catch (e: Exception) {
                findMethod("com.miui.home.launcher.allapps.category.AllAppsCategoryListContainer") {
                    name == "buildSortCategoryList"
                }
            }.hookAfter {
                val list = it.result as ArrayList<*>
                if (list.size > 1) {
                    list.removeAt(0)
                    it.result = list
                }
            }
        }

        if (mPrefsMap.getBoolean("home_hide_category_paging_edit")) {
            "com.miui.home.launcher.allapps.AllAppsGridAdapter".hookAfterMethod(
                "onBindViewHolder", "com.miui.home.launcher.allapps.AllAppsGridAdapter.ViewHolder".findClass(), Int::class.javaPrimitiveType
            ) {
                if (it.args[0].callMethodAs<Int>("getItemViewType") == 64) {
                    it.args[0].getObjectFieldAs<View>("itemView").visibility = View.INVISIBLE
                }
            }
        }
    }
}