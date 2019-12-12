package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

/**
 * @author iPotato
 * @date 2019/11/8
 */
inline fun <reified T : Annotation> KClass<*>.getAnnotatedProps() =
        this.memberProperties.filter { it.hasAnnotation<T>() }.toList()

/**
 * Recursively matches all superclass
 */
fun KClass<*>.getInterfaces(): Set<KClass<*>> {
    val set = mutableSetOf<KClass<*>>()
    superclasses.forEach {
        if (it.java.isInterface) set.add(it)
        if (it.superclasses.isNotEmpty())
            set.addAll(it.getInterfaces())
    }
    return set.toSet()
}

fun KClass<*>.getJavaInterfaces() = getInterfaces().map { it.java }.toSet()

fun KClass<*>.lowerCameCaseName() = java.simpleName.toLowerCamelCase()

fun <T : Any> KClass<out T>.enumArray() = java.enumConstants

fun <T : Any> KClass<out T>.enumSet() = enumArray().toSet()