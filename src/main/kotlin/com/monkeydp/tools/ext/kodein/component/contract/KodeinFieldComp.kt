package com.monkeydp.tools.ext.kodein.component.contract

import com.monkeydp.tools.ext.kodein.component.std.StdKodeinFieldComp
import com.monkeydp.tools.ext.kotlin.initInstance
import java.lang.reflect.Field

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinFieldComp : KodeinComp {
    /**
     * @see annot The field annotated by `annot`
     */
    var field: Field
    
    /**
     * @see field the value of `field`
     */
    var value: Any
}

fun kodeinFieldComp(init: KodeinFieldComp.() -> Unit): KodeinFieldComp = initInstance<StdKodeinFieldComp>(init)