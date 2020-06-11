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