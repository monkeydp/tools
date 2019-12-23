package com.monkeydp.tools.ext.kodein.component

import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/16
 */
@Target(ANNOTATION_CLASS)
annotation class KodeinComponent(
        val builderConfigKClass: KClass<out KodeinBuilderConfig<out KodeinComp>>
)