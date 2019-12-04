package com.monkeydp.tools.ext

import com.monkeydp.tools.util.TypeUtil
import org.apache.commons.lang3.reflect.ConstructorUtils

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun Class<*>.singleton() =
        fields.first { it.name == "INSTANCE" }.get(this)

inline fun <reified C> Class<out C>.singletonX() = singleton() as C

fun Class<*>.newInstance(vararg args: Any): Any {
    val argClasses = TypeUtil.getTypes(*args)
    return ConstructorUtils.getMatchingAccessibleConstructor(this, *argClasses)
            .newInstance(*args)
}

fun <T> Class<T>.newInstanceX(vararg args: Any): T {
    val argClasses = TypeUtil.getTypes(*args)
    return ConstructorUtils.getMatchingAccessibleConstructor(this, *argClasses)
            .newInstance(*args)
}

fun Class<*>.hasAnnotation(annotClass: Class<out Annotation>) = getAnnotation(annotClass) != null

inline fun <reified A : Annotation> Class<*>.hasAnnotation() = hasAnnotation(A::class.java)