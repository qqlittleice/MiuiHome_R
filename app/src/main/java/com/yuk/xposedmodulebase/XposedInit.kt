package com.yuk.xposedmodulebase

import com.yuk.xposedmodulebase.hook.Hook1
import com.yuk.xposedmodulebase.hook.Hook2
import com.yuk.xposedmodulebase.utils.AppRegister
import com.yuk.xposedmodulebase.utils.EasyXposedInit

class XposedInit : EasyXposedInit() {

    override val registeredApp: List<AppRegister> = listOf(Hook1, Hook2)

}