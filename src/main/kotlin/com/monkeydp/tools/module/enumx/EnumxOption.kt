package com.monkeydp.tools.module.enumx

import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/11/8
 */
@Target(CLASS)
annotation class EnumxOption(val parent: KClass<out Enumx<*>> = Nothing::class)
