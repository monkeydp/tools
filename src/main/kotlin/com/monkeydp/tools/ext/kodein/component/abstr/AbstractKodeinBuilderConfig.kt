package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.exception.inner.FunctionMustBeOverriddenException
import com.monkeydp.tools.ext.kodein.component.contract.KodeinBuilderConfig
import com.monkeydp.tools.ext.kodein.component.contract.KodeinComp
import org.kodein.di.Kodein

/**
 * @author iPotato
 * @date 2019/12/18
 */
abstract class AbstractKodeinBuilderConfig<T : KodeinComp> : KodeinBuilderConfig<T> {
    
    override fun Kodein.Builder.config(comps: Collection<T>): Unit = comps.forEach { configOne(it) }
    
    protected open fun Kodein.Builder.configOne(comp: T): Unit = throw FunctionMustBeOverriddenException()
}