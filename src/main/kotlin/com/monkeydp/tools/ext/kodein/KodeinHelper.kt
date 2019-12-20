package com.monkeydp.tools.ext.kodein

import com.monkeydp.tools.ext.kodein.component.contract.KodeinComp
import com.monkeydp.tools.ext.kotlin.singletonX
import org.kodein.di.Kodein

/**
 * @author iPotato
 * @date 2019/12/14
 */
object KodeinHelper {
    
    fun initKodein(comps: Collection<KodeinComp>, vararg modules: Kodein.Module) =
            Kodein {
                importAll(*modules)
                bindAllComps(comps)
            }
    
    private fun Kodein.Builder.bindAllComps(comps: Collection<KodeinComp>): Unit =
            comps.groupBy { it.annot::class }.values.forEach { bindComps(it) }
    
    private fun Kodein.Builder.bindComps(comps: Collection<KodeinComp>) {
        if (comps.isEmpty()) return
        val builder = this
        val builderConfig = comps.first().compAnnot.builderConfigKClass.singletonX()
        with(builderConfig) { builder.configWrapper(comps) }
    }
}