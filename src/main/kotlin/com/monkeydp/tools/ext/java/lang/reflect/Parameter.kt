package com.monkeydp.tools.ext.java

import java.lang.reflect.Parameter
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/11
 */
fun Parameter.hasAnnot(annotKClass: KClass<out Annotation>) =
        getAnnotation(annotKClass.java) != null

inline fun <reified A : Annotation> Parameter.hasAnnot() =
        hasAnnot(A::class)

fun <A : Annotation> Parameter.findAnnot(annotKClass: KClass<out A>): A =
        getAnnotation(annotKClass.java)!!

inline fun <reified A : Annotation> Parameter.findAnnot(): A =
        findAnnot(A::class)

fun <A : Annotation> Parameter.findAnnotOrNull(annotKClass: KClass<out A>): A? =
        getAnnotation(annotKClass.java)

inline fun <reified A : Annotation> Parameter.findAnnotOrNull(): A? =
        findAnnotOrNull(A::class)