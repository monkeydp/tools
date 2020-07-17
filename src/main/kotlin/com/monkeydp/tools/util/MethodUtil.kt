package com.monkeydp.tools.util

import com.monkeydp.tools.ext.kotlin.classX
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
            vararg params: Any
    ): Method =
            any.classX.getMethod(
                    methodName,
                    *params.map { it.javaClass }
                            .toTypedArray()
            )

    fun getMethodOrNull(
            any: Any,
            methodName: String,
            vararg params: Any
    ): Method? =
            try {
                getMethod(any, methodName, *params)
            } catch (ex: NoSuchMethodException) {
                null
            }

    fun getMethods(any: Any): List<Method> =
            any.classX.methods.toList()

    fun invoke(
            any: Any,
            methodName: String,
            vararg params: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Any? = invokeX(any, methodName, *params, config = config)

    fun invoke(
            any: Any,
            method: Method,
            vararg params: Any,
            config: (MethodUtilConfig.() -> Unit)? = null
    ): Any? = invokeX(any, method, *params, config = config)

    inline fun <reified T> invokeX(
            any: Any,
            methodName: String,
            vararg params: Any,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        val method = getMethod(any, methodName, params)
        return invokeX(any, method, *params, config = config)
    }

    inline fun <reified T> invokeX(
            any: Any,
            method: Method,
            vararg params: Any,
            noinline config: (MethodUtilConfig.() -> Unit)? = null
    ): T {
        with(MethodUtilConfig()) {
            if (config != null) config(this)
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