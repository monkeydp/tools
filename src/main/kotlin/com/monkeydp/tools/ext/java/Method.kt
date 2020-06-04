package com.monkeydp.tools.ext.java

import com.monkeydp.tools.util.FieldUtil
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/4
 */
val Method.inClass get() = FieldUtil.getValue<Class<*>>(this, "clazz") { forceAccess = true }

val Method.inKClass get() = inClass.kotlin

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Method.findAnnot(annotKClass: KClass<out T>): T =
        annotations.single() { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> Method.findAnnot(): T =
        annotations.single() { it is T } as T