package com.monkeydp.tools.ext.kodein.component.contract

import com.monkeydp.tools.ext.main.ierror
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