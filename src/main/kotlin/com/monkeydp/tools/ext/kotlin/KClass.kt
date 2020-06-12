package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.java.singleton
import com.monkeydp.tools.ext.java.singletonX
import com.monkeydp.tools.util.FieldUtil
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

/**
 * @author iPotato
 * @date 2019/11/8
 */
inline fun <reified T : Annotation> KClass<*>.getAnnotatedProps() =
        this.memberProperties.filter { it.hasAnnot<T>() }.toList()

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

fun KClass<*>.lowerCameCaseName() = java.simpleName.snakeToLowerCamel()

fun <T : Any> KClass<out T>.enumArray() = java.enumConstants

fun <T : Any> KClass<out T>.enumSet() = enumArray().toSet()

fun KClass<*>.singletonOrNull() = java.singleton()

inline fun <reified C : Any> KClass<out C>.singletonX() = java.singletonX()

// ==== Field ====

fun KClass<*>.getField(fieldName: String): Field =
        FieldUtil.getField(this, fieldName)

fun KClass<*>.getFields(): List<Field> =
        FieldUtil.getFields(this)

fun KClass<*>.getAnnotatedField(annotKClass: KClass<out Annotation>) =
        getFields().single { it.hasAnnot(annotKClass) }

fun KClass<*>.getAnnotatedFields(annotKClass: KClass<out Annotation>) =
        getFields().filter { it.hasAnnot(annotKClass) }

inline fun <reified A : Annotation> KClass<*>.getAnnotatedField() =
        getAnnotatedField(A::class)

inline fun <reified A : Annotation> KClass<*>.getAnnotatedFields() =
        getAnnotatedFields(A::class)
