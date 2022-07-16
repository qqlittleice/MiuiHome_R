package com.yuk.miuiHomeR.utils

class ResourcesHookMap<String, ResourcesHookData> : HashMap<String, ResourcesHookData>() {
    fun isKeyExist(key: String): Boolean = getOrDefault(key, null) != null
}