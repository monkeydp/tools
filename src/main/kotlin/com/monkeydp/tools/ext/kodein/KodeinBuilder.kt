package com.monkeydp.tools.ext.kodein

import com.monkeydp.tools.ext.kotlin.classX
import org.kodein.di.Kodein
import org.kodein.di.TT
import org.kodein.di.TypeToken
import java.lang.reflect.Type

fun <T : Any> Kodein.Builder.bindX(any: T, tag: Any? = null, overrides: Boolean? = null) =
        @Suppress("UNCHECKED_CAST")
        Bind<T>(TT(any.classX) as TypeToken<out T>, tag, overrides)

fun <T : Any> Kodein.Builder.bindX(type: Type, tag: Any? = null, overrides: Boolean? = null) =
        @Suppress("UNCHECKED_CAST")
        Bind<T>(TT(type) as TypeToken<out T>, tag, overrides)

fun <T : Any> Kodein.Builder.bindX(typeToken: TypeToken<out T>, tag: Any? = null, overrides: Boolean? = null) =
        Bind(typeToken, tag, overrides)