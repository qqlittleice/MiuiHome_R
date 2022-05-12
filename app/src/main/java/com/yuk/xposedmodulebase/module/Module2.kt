package com.yuk.xposedmodulebase.module

import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.xposedmodulebase.utils.HookRegister
import com.yuk.xposedmodulebase.utils.hookBeforeMethod

object Module2 : HookRegister() {

    override fun init() {
        try {
            // TODO
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }

}