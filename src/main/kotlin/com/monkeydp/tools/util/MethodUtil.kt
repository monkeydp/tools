package com.monkeydp.tools.util

import com.monkeydp.tools.ext.kotlin.classX
import com.monkeydp.tools.ext.kotlin.getClasses
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2020/4/24
 */
object MethodUtil {

    fun getMethod(
            any: Any,
            methodName: String,
            vararg args: Any
    ): Method =
            getMethod(any, methodName, *args.getClasses())

    fun getMethod(
            any: Any,
            methodName: String,
            vararg paramClasses: Class<*>
    ): Method =
            any.classX.getMethod(methodName, *paramClasses)

    fun getMethodOrNull(
            any: Any,
            methodName: String,
            vararg args: Any
    ): Method? =
            getMethodOrNull(any, methodName, *args.getClasses())

    fun getMethodOrNull(
            any: Any,
            methodName: String,
            vararg paramClasses: Class<*>
    ): Method? =
            try {
                getMethod(any, methodName, *paramClasses)
            } catch (ex: NoSuchMethodException) {
                null
            }

    fun getMethods(any: Any): List<Method> =
            any.classX.methods.toList()

    fun <T> invoke(
            any: Any,
            methodName: String,
            vararg args: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): T =
            getMethod(any, methodName, *args).let {
                invoke(any, it, *args, config = config)
            }

    @Suppress("UNCHECKED_CAST")
    fun <T> invoke(
            any: Any,
            method: Method,
            vararg args: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): T =
            MethodUtilConfig().run {
                if (config != null) config(this)
                if (forceAccess) method.isAccessible = true
                return method.invoke(any, *args) as T
            }

    fun invokeWithMap(
            any: Any,
            methodName: String,
            paramMap: Map<KClass<*>, Any>,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Unit = invokeWithMapX(any, methodName, paramMap, config)

    inline fun <reified T> invokeWithMapX(
            any: Any,
            methodName: String,
            paramMap: Map<KClass<*>, Any>,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
            val method = any.javaClass.getMethod(methodName, *paramMap.keys.map { it.java }.toTypedArray())
            if (forceAccess) method.isAccessible = true
            return method.invoke(any, *paramMap.values.toTypedArray()) as T
        }
    }

    fun invokeDeclared(
            any: Any,
            methodName: String,
            vararg args: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Unit = invokeDeclaredX(any, methodName, *args, config = config)

    inline fun <reified T> invokeDeclaredX(
            any: Any,
            methodName: String,
            vararg args: Any,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
            val method = any.javaClass.getDeclaredMethod(methodName, *args.map { it.javaClass }.toTypedArray())
            if (forceAccess) method.isAccessible = true
            return method.invoke(any, *args) as T
        }
    }

    fun invokeDeclaredWithMap(
            any: Any,
            methodName: String,
            paramMap: Map<KClass<*>, Any>,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Unit = invokeDeclaredWithMap(any, methodName, paramMap, config)

    inline fun <reified T> invokeDeclaredWithMapX(
            any: Any,
            methodName: String,
            paramMap: Map<KClass<*>, Any>,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
            val method = any.classX.getDeclaredMethod(methodName, *paramMap.keys.map { it.java }.toTypedArray())
            if (forceAccess) method.isAccessible = true
            return method.invoke(any, *paramMap.values.toTypedArray()) as T
        }
    }
}

class MethodUtilConfig {
    var forceAccess: Boolean = false
}