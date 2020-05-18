package com.monkeydp.tools.ext.kotlin

import java.lang.reflect.Field
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/5/16
 */
@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Field.findAnnot(annotKClass: KClass<out T>): T =
        annotations.single() { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> Field.findAnnot(): T =
        annotations.single() { it is T } as T

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Field.findAnnotOrNull(annotKClass: KClass<out T>): T? =
        annotations.singleOrNull() { it.annotationClass == annotKClass } as T?

inline fun <reified T : Annotation> Field.findAnnotOrNull(): T? =
        annotations.singleOrNull { it is T } as T?

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Field.firstAnnot(annotKClass: KClass<out T>): T =
        annotations.first() { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> Field.firstAnnot(): T =
        annotations.first() { it is T } as T

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> Field.firstAnnotOrNull(annotKClass: KClass<out T>): T? =
        annotations.firstOrNull() { it.annotationClass == annotKClass } as T?

inline fun <reified T : Annotation> Field.firstAnnotOrNull(): T? =
        annotations.firstOrNull { it is T } as T?

fun <T : Annotation> Field.hasAnnot(annotKClass: KClass<out T>) = firstAnnotOrNull(annotKClass) != null

inline fun <reified T : Annotation> Field.hasAnnot() = firstAnnotOrNull<T>() != null

inline fun <reified T : Annotation> Field.replaceAnnot(annot: T) {
    val rootField = getFieldValue<Field>("root") { forceAccess = true }
    val annotMap: MutableMap<Class<out Annotation>, Annotation> =
            rootField.getFieldValue("declaredAnnotations") { forceAccess = true }
    annotMap[T::class.java] = annot
}