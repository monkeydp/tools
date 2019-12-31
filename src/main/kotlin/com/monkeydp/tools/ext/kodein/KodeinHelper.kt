package com.monkeydp.tools.ext.kodein

import com.monkeydp.tools.ext.kodein.component.KodeinComp
import com.monkeydp.tools.ext.kotlin.singletonX
import org.kodein.di.Kodein

/**
 * @author iPotato
 * @date 2019/12/14
 */
object KodeinHelper {
    
    fun Kodein.Builder.bindComps(comps: Collection<KodeinComp>): Unit =
            comps.groupBy { it.annot.annotationClass }.values.forEach { bindCompsWithSameAnnot(it) }
    
    private fun Kodein.Builder.bindCompsWithSameAnnot(comps: Collection<KodeinComp>) {
        if (comps.isEmpty()) return
        val builder = this
        val builderConfig = comps.first().compAnnot.builderConfigKClass.singletonX()
        with(builderConfig) { builder.configWrapper(comps) }
    }
}