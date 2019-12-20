package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.ext.kodein.component.contract.KodeinKClassComp
import com.monkeydp.tools.ext.kotlin.notNullSingleton
import kotlin.properties.Delegates
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/20
 */
abstract class AbstractKodeinKClassComp : KodeinKClassComp, AbstractKodeinComp() {
    override var annotatedKClass: KClass<*> by Delegates.notNullSingleton()
}