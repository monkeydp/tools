package com.monkeydp.tools.ext

import org.kodein.di.Kodein
import org.kodein.di.TT
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/4
 */
fun <T : Any> Kodein.Builder.bind(kClass: KClass<out T>, tag: Any? = null, overrides: Boolean? = null) =
        Bind<T>(TT(kClass), tag, overrides)

enum class KodeinTag {
    TEST
}