package com.monkeydp.tools.util

import com.monkeydp.tools.ext.kotlin.classX
import com.monkeydp.tools.ext.kotlin.getClasses
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * @author iPotato
 * @date 2020/4/24
 */
object MethodUtil {

    // ==== get method ====

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

    // ==== get methods ====

    fun getMethods(any: Any): List<Method> =
            any.classX.methods.toList()


    // ==== smart get method, include parent param class ====

    fun smartGetMethod(
            any: Any,
            methodName: String,
            vararg args: Any
    ): Method =
            smartGetMethod(any, methodName, *args.getClasses())

    fun smartGetMethodOrNull(
            any: Any,
            methodName: String,
            vararg args: Any
    ): Method? =
            smartGetMethodOrNull(any, methodName, *args.getClasses())

    fun smartGetMethod(
            any: Any,
            methodName: String,
            vararg paramClasses: Class<*>
    ): Method =
            smartGetMethodOrNull(any, methodName, *paramClasses)
                    ?: throw NoSuchMethodException()

    fun smartGetMethodOrNull(
            any: Any,
            methodName: String,
            vararg paramClasses: Class<*>
    ): Method? =
            getMethodOrNull(any, methodName, *paramClasses)
                    ?: any.classX.methods
                            .filter {
                                it.name == methodName &&
                                        it.parameterCount == paramClasses.size &&
                                        it.let {
                                            var matched = true
                                            it.parameterTypes.forEachIndexed { index, clazz ->
                                                if (!clazz.kotlin.isSuperclassOf(paramClasses[index].kotlin)) {
                                                    matched = false
                                                    return@forEachIndexed
                                                }
                                            }
                                            matched
                                        }
                            }.let {
                                when (it.size) {
                                    0 -> null
                                    1 -> return@let it.first()
                                    else -> TODO("Not yet implemented")
                                }
                            }

    // ==== invoke ====

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
