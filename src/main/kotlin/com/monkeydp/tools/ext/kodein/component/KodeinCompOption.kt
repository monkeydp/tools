package com.monkeydp.tools.ext.kodein.component

import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/16
 */
@Target(CLASS)
annotation class KodeinCompOption(
        val bindArgsKClass: KClass<out KodeinBindArgs> = Nothing::class
)