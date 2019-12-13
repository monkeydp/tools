package com.monkeydp.tools.ext.kodein.component

import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/9
 */
interface KodeinComponentConfig {
    val componentsMap: Map<KClass<out Annotation>, Collection<Any>>
}