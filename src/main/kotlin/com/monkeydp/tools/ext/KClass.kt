package com.monkeydp.tools.ext

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * @author iPotato
 * @date 2019/11/8
 */
inline fun <reified T : Annotation> KClass<*>.getAnnotatedProps() =
        this.memberProperties.filter { it.findAnnotation<T>() != null }.toList()
