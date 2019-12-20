package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.ext.kodein.component.contract.KodeinFieldComp
import com.monkeydp.tools.ext.kotlin.notNullSingleton
import java.lang.reflect.Field
import kotlin.properties.Delegates

/**
 * @author iPotato
 * @date 2019/12/20
 */
abstract class AbstractKodeinFieldComp : KodeinFieldComp, AbstractKodeinComp() {
    override var field: Field by Delegates.notNullSingleton()
    override var value: Any by Delegates.notNullSingleton()
}