package com.monkeydp.tools.ext.kodein

import org.kodein.di.Kodein
import org.kodein.di.TypeToken

fun <T : Any> Kodein.Builder.bindX(typeToken: TypeToken<out T>, tag: Any? = null, overrides: Boolean? = null) =
        Bind(typeToken, tag, overrides)