@file:Suppress("UNCHECKED_CAST")

package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.java.newInstanceX
import com.monkeydp.tools.ext.main.ierror
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.util.FieldUtil
import com.monkeydp.tools.util.JsonUtil
import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField

/**
 * @author iPotato
 * @date 2019/10/29
 */
// ==== Class X ====

val <T : Any> T.classX
    get() =
        when (this) {
            is Class<*> -> this
            is KClass<*> -> this.java
            else -> this.javaClass
        }

val <T : Any> T.kClassX
    get() = classX.kotlin


// ==== Properties ====

fun Any.toProps(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.memberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}

fun Any.toDeclaredProps(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.declaredMemberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}


// ==== Prop Iterable ====

private fun Any.filterIllegalAccessible(props: Iterable<KProperty1<Any, *>>) =
        props.filter {
            try {
                it.get(this)
                true
            } catch (e: IllegalCallableAccessException) {
                false
            }
        }

fun Any.toPropIterable(ignoreIllegalAccess: Boolean = false) =
        _toPropIterable(javaClass.kotlin.memberProperties, ignoreIllegalAccess)

inline fun <reified T> Any.toPropIterableX(ignoreIllegalAccess: Boolean = false) =
        toPropIterable(ignoreIllegalAccess).filterValueType<T>()

fun Any.toDeclaredPropIterable(ignoreIllegalAccess: Boolean = false) =
        _toPropIterable(javaClass.kotlin.declaredMemberProperties, ignoreIllegalAccess)

inline fun <reified T> Any.toDeclaredPropIterableX(ignoreIllegalAccess: Boolean = false) =
        toDeclaredPropIterable(ignoreIllegalAccess).filterValueType<T>()

private fun Any._toPropIterable(
        props: Iterable<KProperty1<Any, *>>,
        ignoreIllegalAccess: Boolean = false
): Iterable<KProperty1<Any, *>> {
    var localProps = props
    if (ignoreIllegalAccess) localProps = filterIllegalAccessible(localProps)
    return localProps
}

fun Any.toPropValueIterable(ignoreIllegalAccess: Boolean = false) =
        _toPropValueIterable(javaClass.kotlin.memberProperties, ignoreIllegalAccess)

inline fun <reified T> Any.toPropValueIterableX(ignoreIllegalAccess: Boolean = false) =
        toPropValueIterable(ignoreIllegalAccess).filterIsInstance<T>() as Iterable<T>

fun Any.toDeclaredPropValueIterable(ignoreIllegalAccess: Boolean = false) =
        _toPropValueIterable(javaClass.kotlin.declaredMemberProperties, ignoreIllegalAccess)

inline fun <reified T> Any.toDeclaredPropValueIterableX(ignoreIllegalAccess: Boolean = false) =
        toDeclaredPropValueIterable(ignoreIllegalAccess).filterIsInstance<T>() as Iterable<T>

private fun Any._toPropValueIterable(
        props: Iterable<KProperty1<Any, *>>,
        ignoreIllegalAccess: Boolean = false
): List<Any?> {
    var localProps = props
    if (ignoreIllegalAccess) localProps = filterIllegalAccessible(localProps)
    return localProps.map { it.get(this) }
}


// ==== Prop List ====

fun Any.toPropValueList() = toPropValueIterable().toList()

inline fun <reified T> Any.toPropValueListX() = toPropValueIterableX<T>(true).toList()

fun Any.toDeclaredPropValueList() = toDeclaredPropValueIterable().toList()

inline fun <reified T> Any.toDeclaredPropValueListX() =
        toDeclaredPropValueIterableX<T>(true).toList()


// ==== Prop Set ====

fun Any.toPropValueSet() = toPropValueIterable().toSet()

inline fun <reified T> Any.toPropValueSetX() = toPropValueIterableX<T>(true).toSet()

fun Any.toDeclaredPropValueSet() = toDeclaredPropValueIterable().toList()

inline fun <reified T> Any.toDeclaredPropValueSetX() =
        toDeclaredPropValueIterableX<T>(true).toSet()


// ==== Prop Map ====

fun Any.toPropMap(ignoreIllegalAccess: Boolean = false) =
        toPropIterable(ignoreIllegalAccess).map { it.name to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.buildPropMap(props: Iterable<KProperty1<Any, *>>): Map<K, V> {
    val kClass = K::class
    return when {
        kClass.isSubclassOf(String::class) -> props.map { it.name to it.get(this) }.toMap() as Map<K, V>
        kClass.isSubclassOf(KProperty1::class) -> props.map { it to it.get(this) }.toMap() as Map<K, V>
        else -> ierror("Unsupported type: $kClass!")
    }
}


inline fun <reified K, reified V> Any.toPropMapX(ignoreIllegalAccess: Boolean = false): Map<K, V> {
    val props = toPropIterableX<V>(ignoreIllegalAccess)
    return buildPropMap<K, V>(props)
}

fun Any.toDeclaredPropMap(ignoreIllegalAccess: Boolean = false) =
        toDeclaredPropIterable(ignoreIllegalAccess).map { it.name to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.toDeclaredPropMapX(ignoreIllegalAccess: Boolean = false): Map<K, V> {
    val props = toDeclaredPropIterableX<V>(ignoreIllegalAccess)
    return buildPropMap<K, V>(props)
}


// ==== Copy Prop Values ====

fun <T : Any, S : Any> T.copyPropsFrom(source: S, vararg props: KProperty<*>) {
    val mutableProps = this::class.memberProperties.filterIsInstance<KMutableProperty<*>>()
    val sourceProps = if (props.isEmpty()) source::class.memberProperties else props.toList()
    mutableProps.forEach { targetProp ->
        sourceProps.find {
            it.name == targetProp.name &&
            targetProp.returnType.isSupertypeOf(it.returnType)
        }?.let { matchingProp ->
            targetProp.setter.call(this, matchingProp.getter.call(source))
        }
    }
}


// ==== Copy Field Values ====

fun <T : Any, S : Any> T.copyFieldValuesFromX(
        source: S,
        vararg ignoreProps: KProperty<T>,
        forceAssess: Boolean = false
) = copyFieldValuesFrom(source, *ignoreProps.map { it.javaField!! }.toTypedArray(), forceAssess = forceAssess)

fun <T : Any, S : Any> T.copyFieldValuesFrom(
        source: S,
        vararg ignoreFields: Field,
        forceAssess: Boolean = false
): Unit =
        FieldUtil.getFields(source).forEach { sourceField ->
            val field = FieldUtil.getField(this, sourceField.name)
            if (ignoreFields.contains(field)) return
            val sourceValue = sourceField.get(source)
            if (sourceField.type.kotlin.isSubclassOf(field.type.kotlin))
                FieldUtil.setValue(this, field, sourceValue, forceAssess)
        }

fun <T : Any, R : Any> T.copyFieldValuesFrom(
        vararg pairs: Pair<KProperty1<T, R>, R>,
        forceAssess: Boolean = false
): Unit = pairs.forEach { FieldUtil.setValue(this, it.first.name, it.second, forceAssess) }


// ==== Json ====

fun Any.toJson() = JsonUtil.toString(this)

inline fun <reified T> Any.convertTo() = JsonUtil.convertTo<T>(this)

fun <T> Any.convertTo(clazz: Class<T>) = JsonUtil.convertTo(this, clazz)

fun <T : Any> Any.convertTo(kClass: KClass<T>) = JsonUtil.convertTo(this, kClass.java)


// ==== Reflections ====

fun Any.getReflections() = reflections(this::class)

fun Any.getPropValue(propName: String) =
        this::class.memberProperties.first { it.name == propName }.getter.call(this)

fun <T> Any.getPropValueX(propName: String) = getPropValue(propName) as T


// ==== Init ====

inline fun <reified T> initInstance(init: T.() -> Unit, vararg args: Any): T {
    val instance = T::class.java.newInstanceX(*args)
    instance.init()
    return instance
}