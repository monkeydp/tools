package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.ext.kodein.component.contract.KodeinComp
import com.monkeydp.tools.ext.kotlin.notNullSingleton
import kotlin.properties.Delegates

/**
 * @author iPotato
 * @date 2019/12/20
 */
abstract class AbstractKodeinComp : KodeinComp {
    override var annot: Annotation by Delegates.notNullSingleton()
    
    override fun toString() = "(annot = $annot)"
}