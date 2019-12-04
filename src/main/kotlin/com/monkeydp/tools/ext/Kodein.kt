package com.monkeydp.tools.ext

import org.kodein.di.Kodein
import org.kodein.di.generic

/**
 * @author iPotato
 * @date 2019/12/4
 */
inline fun <reified T : Any> Kodein.Builder.bind(tag: Any? = null, overrides: Boolean? = null) = Bind<T>(generic(), tag, overrides)