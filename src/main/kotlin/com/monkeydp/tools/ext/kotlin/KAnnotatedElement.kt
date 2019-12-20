package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/11/9
 */
@Suppress("UNCHECKED_CAST")
fun <T : Annotation> KAnnotatedElement.findAnnot(annotKClass: KClass<out T>): T =
        annotations.matchOne { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> KAnnotatedElement.findAnnot(): T =
        annotations.matchOne { it is T } as T

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> KAnnotatedElement.findAnnotOrNull(annotKClass: KClass<out T>): T? =
        annotations.matchOneOrNull { it.annotationClass == annotKClass } as T?

inline fun <reified T : Annotation> KAnnotatedElement.findAnnotOrNull(): T? =
        annotations.matchOneOrNull { it is T } as T?

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> KAnnotatedElement.firstAnnot(annotKClass: KClass<out T>): T =
        annotations.first() { it.annotationClass == annotKClass } as T

inline fun <reified T : Annotation> KAnnotatedElement.firstAnnot(): T =
        annotations.first() { it is T } as T

@Suppress("UNCHECKED_CAST")
fun <T : Annotation> KAnnotatedElement.firstAnnotOrNull(annotKClass: KClass<out T>): T? =
        annotations.firstOrNull() { it.annotationClass == annotKClass } as T?

inline fun <reified T : Annotation> KAnnotatedElement.firstAnnotOrNull(): T? =
        annotations.firstOrNull { it is T } as T?

fun <T : Annotation> KAnnotatedElement.hasAnnot(annotKClass: KClass<out T>) = firstAnnotOrNull(annotKClass) != null

inline fun <reified T : Annotation> KAnnotatedElement.hasAnnot() = firstAnnotOrNull<T>() != null