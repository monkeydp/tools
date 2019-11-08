package com.monkeydp.tools.ext

import org.reflections.Reflections
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/11/8
 */
@Suppress("UNCHECKED_CAST")
fun <T> Reflections.getAnnotSingletonsX(annotClass: KClass<out Annotation>) =
        getAnnotSingletons(annotClass) as List<T>

fun Reflections.getAnnotSingletons(annotClass: KClass<out Annotation>) =
        getTypesAnnotatedWith(annotClass.java)
                .map { it.singletonInstance() }.toList()

fun Reflections.getAnnotKClasses(annotClass: KClass<out Annotation>) =
        getTypesAnnotatedWith(annotClass.java).map { it.kotlin }.toList()