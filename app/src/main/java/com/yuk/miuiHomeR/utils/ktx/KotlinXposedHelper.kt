@file:Suppress("unused")

package com.yuk.miuiHomeR.utils.ktx

import android.content.res.XResources
import com.github.kyuubiran.ezxhelper.init.InitFields.ezXClassLoader
import com.github.kyuubiran.ezxhelper.utils.Log
import dalvik.system.BaseDexClassLoader
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge.hookAllConstructors
import de.robv.android.xposed.XposedBridge.hookAllMethods
import de.robv.android.xposed.XposedBridge.hookMethod
import de.robv.android.xposed.XposedBridge.invokeOriginalMethod
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError
import de.robv.android.xposed.XposedHelpers.callMethod
import de.robv.android.xposed.XposedHelpers.callStaticMethod
import de.robv.android.xposed.XposedHelpers.findAndHookConstructor
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.XposedHelpers.findClassIfExists
import de.robv.android.xposed.XposedHelpers.findField
import de.robv.android.xposed.XposedHelpers.findFieldIfExists
import de.robv.android.xposed.XposedHelpers.findFirstFieldByExactType
import de.robv.android.xposed.XposedHelpers.getBooleanField
import de.robv.android.xposed.XposedHelpers.getIntField
import de.robv.android.xposed.XposedHelpers.getLongField
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.XposedHelpers.getStaticObjectField
import de.robv.android.xposed.XposedHelpers.newInstance
import de.robv.android.xposed.XposedHelpers.setBooleanField
import de.robv.android.xposed.XposedHelpers.setFloatField
import de.robv.android.xposed.XposedHelpers.setIntField
import de.robv.android.xposed.XposedHelpers.setLongField
import de.robv.android.xposed.XposedHelpers.setObjectField
import de.robv.android.xposed.XposedHelpers.setStaticObjectField
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Modifier
import java.util.Enumeration

typealias MethodHookParam = MethodHookParam
typealias Replacer = (MethodHookParam) -> Any?
typealias Hooker = (MethodHookParam) -> Unit

fun Class<*>.hookMethod(method: String?, vararg args: Any?) = try {
    findAndHookMethod(this, method, *args)
} catch (e: NoSuchMethodError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

fun Member.hookMethod(callback: XC_MethodHook) = try {
    hookMethod(this, callback)
} catch (e: Throwable) {
    Log.ex(e)
    null
}

inline fun MethodHookParam.callHooker(crossinline hooker: Hooker) = try {
    hooker(this)
} catch (e: Throwable) {
    Log.ex("Error occurred calling hooker on ${this.method}")
    Log.ex(e)
}

inline fun MethodHookParam.callReplacer(crossinline replacer: Replacer) = try {
    replacer(this)
} catch (e: Throwable) {
    Log.ex("Error occurred calling replacer on ${this.method}")
    Log.ex(e)
    null
}

inline fun Member.replaceMethod(crossinline replacer: Replacer) =
    hookMethod(object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
    })

inline fun Member.hookAfterMethod(crossinline hooker: Hooker) =
    hookMethod(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Member.hookBeforeMethod(crossinline hooker: (MethodHookParam) -> Unit) =
    hookMethod(object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookBeforeMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = hookMethod(method, *args, object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
})

inline fun Class<*>.hookAfterMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = hookMethod(method, *args, object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
})

inline fun Class<*>.replaceMethod(
    method: String?,
    vararg args: Any?,
    crossinline replacer: Replacer
) = hookMethod(method, *args, object : XC_MethodReplacement() {
    override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
})

fun Class<*>.hookAllMethods(methodName: String?, hooker: XC_MethodHook): Set<XC_MethodHook.Unhook> =
    try {
        hookAllMethods(this, methodName, hooker)
    } catch (e: NoSuchMethodError) {
        Log.ex(e)
        emptySet()
    } catch (e: ClassNotFoundError) {
        Log.ex(e)
        emptySet()
    } catch (e: ClassNotFoundException) {
        Log.ex(e)
        emptySet()
    }

inline fun Class<*>.hookBeforeAllMethods(methodName: String?, crossinline hooker: Hooker) =
    hookAllMethods(methodName, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookAfterAllMethods(methodName: String?, crossinline hooker: Hooker) =
    hookAllMethods(methodName, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)

    })

inline fun Class<*>.replaceAfterAllMethods(methodName: String?, crossinline replacer: Replacer) =
    hookAllMethods(methodName, object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
    })

fun Class<*>.hookConstructor(vararg args: Any?) = try {
    findAndHookConstructor(this, *args)
} catch (e: NoSuchMethodError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

inline fun Class<*>.hookBeforeConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookAfterConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.replaceConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

fun Class<*>.hookAllConstructors(hooker: XC_MethodHook): Set<XC_MethodHook.Unhook> = try {
    hookAllConstructors(this, hooker)
} catch (e: NoSuchMethodError) {
    Log.ex(e)
    emptySet()
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    emptySet()
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    emptySet()
}

inline fun Class<*>.hookAfterAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookBeforeAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.replaceAfterAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

fun String.hookMethod(method: String?, vararg args: Any?) = try {
    findClass().hookMethod(method, *args)
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

inline fun String.hookBeforeMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = try {
    findClass().hookBeforeMethod(method, *args, hooker = hooker)
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

inline fun String.hookAfterMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = try {
    findClass().hookAfterMethod(method, *args, hooker = hooker)
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

inline fun String.replaceMethod(
    method: String?,
    vararg args: Any?,
    crossinline replacer: Replacer
) = try {
    findClass().replaceMethod(method, *args, replacer = replacer)
} catch (e: ClassNotFoundError) {
    Log.ex(e)
    null
} catch (e: ClassNotFoundException) {
    Log.ex(e)
    null
}

fun MethodHookParam.invokeOriginalMethod(): Any? = invokeOriginalMethod(method, thisObject, args)

inline fun <T, R> T.runCatchingOrNull(func: T.() -> R?) = try {
    func()
} catch (e: Throwable) {
    null
}

fun Any.getObjectField(field: String?): Any? = getObjectField(this, field)

fun Any.getObjectFieldOrNull(field: String?): Any? = runCatchingOrNull {
    getObjectField(this, field)
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldAs(field: String?) = getObjectField(this, field) as T

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldOrNullAs(field: String?) = runCatchingOrNull {
    getObjectField(this, field) as T
}

fun Any.getIntField(field: String?) = getIntField(this, field)

fun Any.getIntFieldOrNull(field: String?) = runCatchingOrNull {
    getIntField(this, field)
}

fun Any.getLongField(field: String?) = getLongField(this, field)

fun Any.getLongFieldOrNull(field: String?) = runCatchingOrNull {
    getLongField(this, field)
}

fun Any.getBooleanField(field: String?) = getBooleanField(this, field)

fun Any.getBooleanFieldOrNull(field: String?) = runCatchingOrNull {
    getBooleanField(this, field)
}

fun Any.callMethod(methodName: String?, vararg args: Any?): Any? =
    callMethod(this, methodName, *args)

fun Any.callMethodOrNull(methodName: String?, vararg args: Any?): Any? = runCatchingOrNull {
    callMethod(this, methodName, *args)
}

fun Class<*>.callStaticMethod(methodName: String?, vararg args: Any?): Any? =
    callStaticMethod(this, methodName, *args)

fun Class<*>.callStaticMethodOrNull(methodName: String?, vararg args: Any?): Any? =
    runCatchingOrNull {
        callStaticMethod(this, methodName, *args)
    }

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.callStaticMethodAs(methodName: String?, vararg args: Any?) =
    callStaticMethod(this, methodName, *args) as T

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.callStaticMethodOrNullAs(methodName: String?, vararg args: Any?) =
    runCatchingOrNull {
        callStaticMethod(this, methodName, *args) as T
    }

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectFieldAs(field: String?) = getStaticObjectField(this, field) as T

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectFieldOrNullAs(field: String?) = runCatchingOrNull {
    getStaticObjectField(this, field) as T
}

fun Class<*>.getStaticObjectField(field: String?): Any? = getStaticObjectField(this, field)

fun Class<*>.getStaticObjectFieldOrNull(field: String?): Any? = runCatchingOrNull {
    getStaticObjectField(this, field)
}

fun Class<*>.setStaticObjectField(field: String?, obj: Any?) = apply {
    setStaticObjectField(this, field, obj)
}

fun Class<*>.setStaticObjectFieldIfExist(field: String?, obj: Any?) = apply {
    try {
        setStaticObjectField(this, field, obj)
    } catch (ignored: Throwable) {
    }
}

inline fun <reified T> Class<*>.findFieldByExactType(): Field? =
    findFirstFieldByExactType(this, T::class.java)

fun Class<*>.findFieldByExactType(type: Class<*>): Field? =
    findFirstFieldByExactType(this, type)

@Suppress("UNCHECKED_CAST")
fun <T> Any.callMethodAs(methodName: String?, vararg args: Any?) =
    callMethod(this, methodName, *args) as T

@Suppress("UNCHECKED_CAST")
fun <T> Any.callMethodOrNullAs(methodName: String?, vararg args: Any?) = runCatchingOrNull {
    callMethod(this, methodName, *args) as T
}

fun Any.callMethod(methodName: String?, parameterTypes: Array<Class<*>>, vararg args: Any?): Any? =
    callMethod(this, methodName, parameterTypes, *args)

fun Any.callMethodOrNull(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = runCatchingOrNull {
    callMethod(this, methodName, parameterTypes, *args)
}

fun Class<*>.callStaticMethod(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = callStaticMethod(this, methodName, parameterTypes, *args)

fun Class<*>.callStaticMethodOrNull(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = runCatchingOrNull {
    callStaticMethod(this, methodName, parameterTypes, *args)
}

fun String.findClass(classLoader: ClassLoader = ezXClassLoader): Class<*> =
    findClass(this, classLoader)

fun String.findClassOrNull(classLoader: ClassLoader = ezXClassLoader): Class<*>? =
    findClassIfExists(this, classLoader)

fun Class<*>.new(vararg args: Any?): Any = newInstance(this, *args)

fun Class<*>.new(parameterTypes: Array<Class<*>>, vararg args: Any?): Any =
    newInstance(this, parameterTypes, *args)

fun Class<*>.findField(field: String?): Field = findField(this, field)

fun Class<*>.findFieldOrNull(field: String?): Field? = findFieldIfExists(this, field)

fun <T> T.setIntField(field: String?, value: Int) = apply {
    setIntField(this, field, value)
}

fun <T> T.setLongField(field: String?, value: Long) = apply {
    setLongField(this, field, value)
}

fun <T> T.setFloatField(field: String?, value: Float) = apply {
    setFloatField(this, field, value)
}

fun <T> T.setObjectField(field: String?, value: Any?) = apply {
    setObjectField(this, field, value)
}

fun <T> T.setBooleanField(field: String?, value: Boolean) = apply {
    setBooleanField(this, field, value)
}

inline fun XResources.hookLayout(
    id: Int,
    crossinline hooker: (XC_LayoutInflated.LayoutInflatedParam) -> Unit
) {
    try {
        hookLayout(id, object : XC_LayoutInflated() {
            override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
                try {
                    hooker(liparam)
                } catch (e: Throwable) {
                    Log.ex(e)
                }
            }
        })
    } catch (e: Throwable) {
        Log.ex(e)
    }
}

inline fun XResources.hookLayout(
    pkg: String,
    type: String,
    name: String,
    crossinline hooker: (XC_LayoutInflated.LayoutInflatedParam) -> Unit
) {
    try {
        val id = getIdentifier(name, type, pkg)
        hookLayout(id, hooker)
    } catch (e: Throwable) {
        Log.ex(e)
    }
}

fun Class<*>.findFirstFieldByExactType(type: Class<*>): Field =
    findFirstFieldByExactType(this, type)

fun Class<*>.findFirstFieldByExactTypeOrNull(type: Class<*>?): Field? = runCatchingOrNull {
    findFirstFieldByExactType(this, type)
}

fun Any.getFirstFieldByExactType(type: Class<*>): Any? =
    javaClass.findFirstFieldByExactType(type).get(this)

@Suppress("UNCHECKED_CAST")
fun <T> Any.getFirstFieldByExactTypeAs(type: Class<*>) =
    javaClass.findFirstFieldByExactType(type).get(this) as? T

inline fun <reified T : Any> Any.getFirstFieldByExactType() =
    javaClass.findFirstFieldByExactType(T::class.java).get(this) as? T

fun Any.getFirstFieldByExactTypeOrNull(type: Class<*>?): Any? = runCatchingOrNull {
    javaClass.findFirstFieldByExactTypeOrNull(type)?.get(this)
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.getFirstFieldByExactTypeOrNullAs(type: Class<*>?) =
    getFirstFieldByExactTypeOrNull(type) as? T

inline fun <reified T> Any.getFirstFieldByExactTypeOrNull() =
    getFirstFieldByExactTypeOrNull(T::class.java) as? T

inline fun ClassLoader.findDexClassLoader(crossinline delegator: (BaseDexClassLoader) -> BaseDexClassLoader = { x -> x }): BaseDexClassLoader? {
    var classLoader = this
    while (classLoader !is BaseDexClassLoader) {
        if (classLoader.parent != null) classLoader = classLoader.parent
        else return null
    }
    return delegator(classLoader)
}

inline fun ClassLoader.allClassesList(crossinline delegator: (BaseDexClassLoader) -> BaseDexClassLoader = { x -> x }): List<String> {
    return findDexClassLoader(delegator)?.getObjectField("pathList")
        ?.getObjectFieldAs<Array<Any>>("dexElements")
        ?.flatMap {
            it.getObjectField("dexFile")?.callMethodAs<Enumeration<String>>("entries")?.toList()
                .orEmpty()
        }.orEmpty()
}

val Member.isStatic: Boolean
    inline get() = Modifier.isStatic(this.modifiers)
val Member.isNotStatic: Boolean
    inline get() = !this.isStatic

fun Any.getFieldByClassOrObject(
    fieldName: String,
    isStatic: Boolean = false,
    fieldType: Class<*>? = null
): Field {
    if (fieldName.isEmpty()) throw IllegalArgumentException("Field name must not be null or empty!")
    var clz: Class<*> = if (this is Class<*>) this else this.javaClass
    do {
        clz.declaredFields
            .filter {
                !(isStatic && !Modifier.isStatic(it.modifiers)) || !(!isStatic && Modifier.isStatic(
                    it.modifiers
                ))
            }
            .firstOrNull {
                (fieldType == null || it.type == fieldType) && (it.name == fieldName)
            }?.let { it.isAccessible = true;return it }
    } while (clz.superclass.also { clz = it } != null)
    throw NoSuchFieldError()
}

fun Class<*>.getStaticFiledByClass(fieldName: String, type: Class<*>? = null): Field {
    if (fieldName.isEmpty()) throw IllegalArgumentException("Field name must not be null or empty!")
    return this.getFieldByClassOrObject(fieldName, true, type)
}