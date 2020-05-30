package com.monkeydp.tools.ext.kodein.component

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.exception.inner.FunctionMustBeOverriddenException
import org.kodein.di.Kodein

/**
 * @author iPotato
 * @date 2019/12/18
 */
interface KodeinBuilderConfig<T : KodeinComp> {
    
    fun Kodein.Builder.configWrapper(comps: Collection<KodeinComp>) {
        if (comps.isEmpty()) return
        @Suppress("UNCHECKED_CAST")
        config(comps as? Collection<T> ?: ierror("Invalid type `${comps.first().javaClass}`"))
    }
    
    fun Kodein.Builder.config(comps: Collection<T>)
}

/**
 * @author iPotato
 * @date 2019/12/18
 */
abstract class AbstractKodeinBuilderConfig<T : KodeinComp> : KodeinBuilderConfig<T> {
    
    override fun Kodein.Builder.config(comps: Collection<T>): Unit = comps.forEach { configOne(it) }
    
    protected open fun Kodein.Builder.configOne(comp: T): Unit = throw FunctionMustBeOverriddenException()
}