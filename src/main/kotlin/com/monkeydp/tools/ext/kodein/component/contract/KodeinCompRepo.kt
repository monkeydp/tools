package com.monkeydp.tools.ext.kodein.component.contract

import org.kodein.di.Kodein

/**
 * Kodein component repository
 *
 * @author iPotato
 * @date 2019/12/9
 */
interface KodeinCompRepo {
    val modules: Array<Kodein.Module>
    val comps: Collection<KodeinComp>
}