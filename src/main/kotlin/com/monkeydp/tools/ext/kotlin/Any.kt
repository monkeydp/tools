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

private fun <T : Any> T.filterProps(
        props: Iterable<KProperty1<T, *>>,
        config: (KProperty1FilterConfig.() -> Unit)? = null
): Iterable<KProperty1<T, *>> = let { KProperty1Filter.filterProps(this, props, config) }

fun <T : Any> T.toPropIterable(config: (KProperty1FilterConfig.() -> Unit)? = null) =
        javaClass.kotlin.memberProperties.run { filterProps(this, config) }

inline fun <reified T> Any.toPropIterableX(noinline config: (KProperty1FilterConfig.() -> Unit)? = null) =
        toPropIterable(config).filterValueType<T>()

fun <T : Any> T.toDeclaredPropIterable(config: (KProperty1FilterConfig.() -> Unit)? = null) =
        javaClass.kotlin.declaredMemberProperties.run { filterProps(this, config) }

inline fun <reified T> Any.toDeclaredPropIterableX(noinline config: (KProperty1FilterConfig.() -> Unit)? = null) =
        toDeclaredPropIterable(config).filterValueType<T>()

fun <T : Any> T.toPropValueIterable(config: (KProperty1FilterConfig.() -> Unit)? = null): List<Any?> =
        toPropIterable(config).map { it.get(this) }

inline fun <reified T> Any.toPropValueIterableX(noinline config: (KProperty1FilterConfig.() -> Unit)? = null) =
        toPropValueIterable(config).filterIsInstance<T>() as Iterable<T>

fun <T : Any> T.toDeclaredPropValueIterable(config: (KProperty1FilterConfig.() -> Unit)? = null): List<Any?> =
        toDeclaredPropIterable(config).map { it.get(this) }

inline fun <reified T> Any.toDeclaredPropValueIterableX(noinline config: (KProperty1FilterConfig.() -> Unit)? = null) =
        toDeclaredPropValueIterable(config).filterIsInstance<T>() as Iterable<T>


// ==== Prop List ====

fun <T : Any> T.toPropValueList() = toPropValueIterable().toList()

inline fun <reified T> Any.toPropValueListX() = toPropValueIterableX<T> { ignoreIllegalAccess = true }.toList()

fun <T : Any> T.toDeclaredPropValueList() = toDeclaredPropValueIterable().toList()

inline fun <reified T> Any.toDeclaredPropValueListX() =
        toDeclaredPropValueIterableX<T> { ignoreIllegalAccess = true }.toList()


// ==== Prop Set ====

fun <T : Any> T.toPropValueSet() = toPropValueIterable().toSet()

inline fun <reified T> Any.toPropValueSetX() = toPropValueIterableX<T> { ignoreIllegalAccess = true }.toSet()

fun <T : Any> T.toDeclaredPropValueSet() = toDeclaredPropValueIterable().toList()

inline fun <reified T> Any.toDeclaredPropValueSetX() =
        toDeclaredPropValueIterableX<T> { ignoreIllegalAccess = true }.toSet()


// ==== Prop Map ====

fun <T : Any, U : Any?> Any.toPropMap(
        key: (KProperty1<Any, *>) -> T = { it as T },
        config: (KProperty1FilterConfig.() -> Unit)? = null
): Map<T, U> = toPropIterable(config).map { key(it) to it.get(this) as U }.toMap()

inline fun <reified K, reified V> Any.buildPropMap(props: Iterable<KProperty1<Any, *>>): Map<K, V> {
    val kClass = K::class
    return when {
        kClass.isSubclassOf(String::class) -> props.map { it.name to it.get(this) }.toMap() as Map<K, V>
        kClass.isSubclassOf(KProperty1::class) -> props.map { it to it.get(this) }.toMap() as Map<K, V>
        else -> ierror("Unsupported type: $kClass!")
    }
}


inline fun <reified K, reified V> Any.toPropMapX(
        noinline config: (KProperty1FilterConfig.() -> Unit)? = null): Map<K, V> =
        toPropIterableX<V>(config).run(::buildPropMap)

fun <T : Any> T.toDeclaredPropMap(config: (KProperty1FilterConfig.() -> Unit)? = null) =
        toDeclaredPropIterable(config).map { it.name to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.toDeclaredPropMapX(
        noinline config: (KProperty1FilterConfig.() -> Unit)? = null
): Map<K, V> = toDeclaredPropIterableX<V>(config).run(::buildPropMap)


// ==== Copy Prop Values ====

fun <T : Any, S : Any> T.copyPropValuesFrom(source: S, vararg props: KProperty1<S, *>) {
    val sourceProps = if (props.isEmpty()) source::class.memberProperties else props.toList()
    this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().also { mutableProps ->
        mutableProps.forEach { targetProp ->
            sourceProps.find { sourceProp ->
                sourceProp.name == targetProp.name &&
                targetProp.returnType.isSupertypeOf(sourceProp.returnType)
            }?.let { targetProp.setter.call(this, it.getter.call(source)) }
        }
    }
}

fun <T : Any> T.copyPropValuesFrom(map: Map<String, Any?>) {
    this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().also { mutableProps ->
        mutableProps.forEach { targetProp ->
            val key = targetProp.name
            if (!map.containsKey(key)) return@forEach
            val anyOrNull = map[key]
            if (anyOrNull != null && !targetProp.returnType.isSupertypeOf(anyOrNull::class.createType())) return@forEach
            targetProp.setter.call(this, anyOrNull)
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

fun <T : Any> T.toJson() = JsonUtil.toString(this)

inline fun <reified T> Any.convertTo() = JsonUtil.convertTo<T>(this)

fun <T> Any.convertTo(clazz: Class<T>) = JsonUtil.convertTo(this, clazz)

fun <T : Any> Any.convertTo(kClass: KClass<T>) = JsonUtil.convertTo(this, kClass.java)


// ==== Reflections ====

fun <T : Any> T.getReflections() = reflections(this::class)

fun <T : Any> T.getPropValue(propName: String) =
        this::class.memberProperties.first { it.name == propName }.getter.call(this)

fun <T : Any, U : Any> T.getPropValueX(propName: String) = getPropValue(propName) as U


// ==== Init ====

inline fun <reified T : Any> initInstance(noinline init: (T.() -> Unit)? = null, vararg args: Any): T {
    val instance = T::class.java.newInstanceX(*args)
    init?.invoke(instance)
    return instance
}