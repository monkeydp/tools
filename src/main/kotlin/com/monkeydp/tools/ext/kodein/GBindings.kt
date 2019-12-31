package com.monkeydp.tools.ext.kodein

import org.kodein.di.Kodein
import org.kodein.di.bindings.NoArgSimpleBindingKodein
import org.kodein.di.bindings.RefMaker
import org.kodein.di.bindings.Singleton
import org.kodein.di.generic

/**
 * @author iPotato
 * @date 2019/12/20
 */
@Suppress("UNCHECKED_CAST")
inline fun <C, reified T : Any> Kodein.BindBuilder.WithScope<C>.singletonX(
        ref: RefMaker? = null,
        sync: Boolean = true,
        noinline creator: NoArgSimpleBindingKodein<C>.() -> Any
) = Singleton(scope, contextType, generic(), ref, sync, creator as NoArgSimpleBindingKodein<C>.() -> T)