package com.monkeydp.tools.util

import com.monkeydp.tools.ext.kotlin.classX
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2020/4/24
 */
object MethodUtil {

    fun invoke(
            any: Any,
            methodName: String,
            vararg params: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Unit = invokeX(any, methodName, *params, config = config)

    inline fun <reified T> invokeX(
            any: Any,
            methodName: String,
            vararg params: Any,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
            val method = any.javaClass.getMethod(methodName, *params.map { it.javaClass }.toTypedArray())
            if (forceAccess) method.isAccessible = true
            return method.invoke(any, *params) as T
        }
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
            vararg params: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Unit = invokeDeclaredX(any, methodName, *params, config = config)

    inline fun <reified T> invokeDeclaredX(
            any: Any,
            methodName: String,
            vararg params: Any,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
            val method = any.javaClass.getDeclaredMethod(methodName, *params.map { it.javaClass }.toTypedArray())
            if (forceAccess) method.isAccessible = true
            return method.invoke(any, *params) as T
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