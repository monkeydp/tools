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
fun <T : Annotation> Method.findAnnot(annotKClass: KClass<out T>) =
        annotations.single { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> Method.findAnnot() =
        annotations.single { it is T } as T

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Method.findAnnotOrNull(annotKClass: KClass<out T>) =
        annotations.singleOrNull { it.annotationClass == annotKClass } as T?

inline fun <reified T : Annotation> Method.findAnnotOrNull() =
        annotations.singleOrNull { it is T } as T?

fun <T : Annotation> Method.hasAnnot(annotKClass: KClass<out T>) =
        annotations.singleOrNull { it.annotationClass == annotKClass } != null

inline fun <reified T : Annotation> Method.hasAnnot() =
        hasAnnot(T::class)