package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.java.*
import com.monkeydp.tools.util.FieldUtil
import com.monkeydp.tools.util.MethodUtil
import com.monkeydp.tools.util.MethodUtilConfig
import java.lang.reflect.Field
import java.lang.reflect.Method
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

fun KClass<*>.getAnnotField(annotKClass: KClass<out Annotation>) =
        getFields().single { it.hasAnnot(annotKClass) }

fun KClass<*>.getAnnotFields(annotKClass: KClass<out Annotation>) =
        getFields().filter { it.hasAnnot(annotKClass) }

inline fun <reified A : Annotation> KClass<*>.getAnnotField() =
        getAnnotField(A::class)

inline fun <reified A : Annotation> KClass<*>.getAnnotFields() =
        getAnnotFields(A::class)

// ==== Method ====

fun KClass<*>.getMethodByArgs(name: String, vararg args: Any): Method =
        MethodUtil.getMethod(this, name, *args)

fun KClass<*>.getMethodOrNullByArgs(name: String, vararg args: Any): Method? =
        MethodUtil.getMethodOrNull(this, name, *args)

fun KClass<*>.getMethod(name: String, vararg paramKClasses: KClass<*>): Method =
        MethodUtil.getMethod(this, name, *paramKClasses.map { it.java }.toTypedArray())

fun KClass<*>.getMethodOrNull(name: String, vararg paramKClasses: KClass<*>): Method? =
        MethodUtil.getMethodOrNull(this, name, *paramKClasses.map { it.java }.toTypedArray())

fun KClass<*>.smartGetMethod(
        methodName: String,
        vararg args: Any
): Method =
        MethodUtil.smartGetMethod(this, methodName, *args)

fun KClass<*>.smartGetMethodOrNull(
        methodName: String,
        vararg args: Any
): Method? =
        MethodUtil.smartGetMethodOrNull(this, methodName, *args)

fun KClass<*>.smartGetMethod(
        methodName: String,
        vararg paramClasses: Class<*>
): Method =
        MethodUtil.smartGetMethod(this, methodName, *paramClasses)

fun KClass<*>.smartGetMethodOrNull(
        methodName: String,
        vararg paramClasses: Class<*>
): Method? =
        MethodUtil.smartGetMethodOrNull(this, methodName, *paramClasses)

fun KClass<*>.getMethods(): List<Method> =
        MethodUtil.getMethods(this)

fun <T> Any.invokeMethod(
        methodName: String,
        vararg args: Any,
        config: (MethodUtilConfig.() -> Unit)? = null
): T =
        MethodUtil.invoke(this, methodName, *args, config = config)

fun <T> Any.invokeMethod(
        method: Method,
        vararg args: Any,
        config: (MethodUtilConfig.() -> Unit)? = null
): T =
        MethodUtil.invoke(this, method, *args, config = config)

fun KClass<*>.getAnnotMethods(annotKClass: KClass<out Annotation>): List<Method> =
        getMethods().filter { it.hasAnnot(annotKClass) }

inline fun <reified A : Annotation> KClass<*>.getAnnotMethods(): List<Method> =
        getAnnotMethods(A::class)

// ==== new Instance ====

fun KClass<*>.newInstance(vararg args: Any): Any =
        java.newInstance(*args)

fun <T : Any> KClass<T>.newInstanceX(vararg args: Any): T =
        java.newInstanceX(*args)
