package com.monkeydp.tools.ext.kodein.component

import org.kodein.di.TypeToken

/**
 * @see org.kodein.di.generic.bind
 * @author iPotato
 * @date 2019/12/16
 */
interface KodeinBindArgs {
    operator fun component1() = typeToken
    operator fun component2() = tag
    operator fun component3() = overrides
    
    val typeToken: TypeToken<out Any>
    val tag: Any? get() = null
    val overrides: Boolean? get() = null
}