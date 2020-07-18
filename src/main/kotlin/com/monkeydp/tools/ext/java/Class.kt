package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.getClasses
import org.apache.commons.lang3.reflect.ConstructorUtils

/**
 * @author iPotato
 * @date 2019/10/30
 */
// ==== singleton ====

fun Class<*>.singleton() =
        fields.firstOrNull() { it.name == "INSTANCE" }?.get(this) ?: ierror("${this.name} is not a singleton!")

inline fun <reified C> Class<out C>.singletonX() = singleton() as C

// ==== new instance ====

fun Class<*>.newInstance(vararg args: Any): Any =
        if (isInterface) newIInstance(*args)
        else newCInstance(*args)

fun <T : Any> Class<T>.newInstanceX(vararg args: Any): T =
        if (isInterface) newIInstanceX(*args)
        else newCInstanceX(*args)

// ==== new class instance ====

private fun Class<*>.newCInstance(vararg args: Any): Any =
        newCInstanceX(*args)

private fun <T> Class<T>.newCInstanceX(vararg args: Any): T =
        ConstructorUtils.getMatchingAccessibleConstructor(this, *args.getClasses())
                .newInstance(*args)

// ==== annotation ====

fun Class<*>.hasAnnot(annotClass: Class<out Annotation>) = getAnnotation(annotClass) != null

inline fun <reified A : Annotation> Class<*>.hasAnnot() = hasAnnot(A::class.java)

fun Class<*>.hasSuperclass(except: Class<*>? = null) = superclass != null && superclass != except

fun Class<*>.inPackage(packageName: String): Boolean =
        `package`.name.startsWith(packageName)