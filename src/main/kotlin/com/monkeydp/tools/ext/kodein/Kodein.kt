package com.monkeydp.tools.ext.kodein

import com.monkeydp.tools.ext.kotlin.classX
import org.kodein.di.*
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun <T : Any> Kodein.Builder.bindX(any: T, tag: Any? = null, overrides: Boolean? = null) =
        @Suppress("UNCHECKED_CAST")
        Bind<T>(TT(any.classX) as TypeToken<out T>, tag, overrides)

fun <T : Any> Kodein.Builder.bindX(type: Type, tag: Any? = null, overrides: Boolean? = null) =
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