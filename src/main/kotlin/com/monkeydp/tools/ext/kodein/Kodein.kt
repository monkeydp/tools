package com.monkeydp.tools.ext.kodein

import org.kodein.di.*
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/4
 */
enum class KodeinTag {
    NULL, TEST
}

fun <T : Any> Kodein.Builder.bindX(any: T, tag: Any? = null, overrides: Boolean? = null) =
        bindX(any::class, tag, overrides)

fun <T : Any> Kodein.Builder.bindX(kClass: KClass<out T>, tag: Any? = null, overrides: Boolean? = null) =
        Bind<T>(TT(kClass), tag, overrides)

fun <T : Any> Kodein.Builder.bindX(type: ParameterizedType, tag: Any? = null, overrides: Boolean? = null) =
        @Suppress("UNCHECKED_CAST")
        Bind<T>(TT(type) as TypeToken<out T>, tag, overrides)

inline fun <reified T : KClass<*>> Kodein.Builder.bindKClass(
        kClass: T,
        tag: Any? = null,
        overrides: Boolean? = null
): Kodein.Builder.TypeBinder<T> {
    val type = ParameterizedTypeImpl.make(KClass::class.java, arrayOf(kClass.java), null)
    return bindX(type, tag, overrides)
}

fun <T : Any> KodeinAware.instanceX(kClass: KClass<out T>, tag: Any? = null) = Instance<T>(TT(kClass), tag)