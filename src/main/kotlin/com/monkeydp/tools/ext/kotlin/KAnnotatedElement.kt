package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

/**
 * @author iPotato
 * @date 2019/11/9
 */
inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation() = findAnnotation<T>() != null