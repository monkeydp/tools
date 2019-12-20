package com.monkeydp.tools.ext.kodein.component.contract

import com.monkeydp.tools.ext.kodein.component.std.StdKodeinKClassComp
import com.monkeydp.tools.ext.kotlin.initInstance
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinKClassComp : KodeinComp {
    /**
     * @see annot The kClass annotated by `annot`
     */
    var annotatedKClass: KClass<*>
}

fun kodeinKClassComp(init: KodeinKClassComp.() -> Unit): KodeinKClassComp = initInstance<StdKodeinKClassComp>(init)