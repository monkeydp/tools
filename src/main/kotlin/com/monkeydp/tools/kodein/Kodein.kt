package com.monkeydp.tools.kodein

import org.kodein.di.*
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/4
 */
enum class KodeinTag {
    NULL, TEST
}

fun <T : Any> Kodein.Builder.bindX(kClass: KClass<out T>, tag: Any? = null, overrides: Boolean? = null) =
        Bind<T>(TT(kClass), tag, overrides)

inline fun <reified T : Any, reified K : KClass<out T>> Kodein.Builder.bindKClass(
        kClass: K,
        tag: Any? = null,
        overrides: Boolean? = null
): Kodein.Builder.TypeBinder<K> {
    val type = ParameterizedTypeImpl.make(KClass::class.java, arrayOf(kClass.java), null)
    @Suppress("UNCHECKED_CAST")
    return Bind<K>(TT(type) as TypeToken<out K>, tag, overrides)
}

fun <T : Any> KodeinAware.instanceX(kClass: KClass<out T>, tag: Any? = null) = Instance<T>(TT(kClass), tag)