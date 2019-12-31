@file:Suppress("UNCHECKED_CAST")

package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.java.newInstanceX
import com.monkeydp.tools.ext.main.ierror
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.util.FieldUtil
import com.monkeydp.tools.util.JsonUtil
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
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

fun Any.toProperties(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.memberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}

fun Any.toDeclaredProperties(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.declaredMemberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}


// ==== Props ====
private fun <T : Any> T.filterProps(
        props: Iterable<KProperty1<T, *>>,
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = KProperty1Filter.filterProps(this, props, config)

fun <T : Any> T.toProps(
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = javaClass.kotlin.memberProperties.run { filterProps(this, config) }

inline fun <T : Any, reified R> T.toPropsX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): List<KProperty1<T, R>> = toProps(config).filterValueType<T, R>()

fun <T : Any> T.toDeclaredProps(
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = javaClass.kotlin.declaredMemberProperties.run { filterProps(this, config) }

inline fun <T : Any, reified R> T.toDeclaredPropsX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): List<KProperty1<T, R>> = toDeclaredProps(config).filterValueType<T, R>()


// ==== Prop Values ====

fun Any.toPropValues(
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<Any?> = toProps(config).map { it.get(this) }

fun <V : Any> Any.toPropValues(
        kClass: KClass<V>,
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<V> = toPropValues(config).filterIsInstance(kClass)

inline fun <reified V> Any.toPropValuesX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): List<V> = toPropValues(config).filterIsInstance<V>()

fun Any.toDeclaredPropValues(
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<Any?> = toDeclaredProps(config).map { it.get(this) }

fun <V : Any> Any.toDeclaredPropValues(
        kClass: KClass<V>,
        config: (KPropertyFilterConfig.() -> Unit)? = null
): List<V> = toDeclaredPropValues(config).filterIsInstance(kClass)

inline fun <reified V> Any.toDeclaredPropValuesX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): List<V> = toDeclaredPropValues(config).filterIsInstance<V>()


// ==== Prop Map ====

inline fun <K, reified V> Any.toPropMap(
        key: (KProperty1<Any, V>) -> K = { it as K },
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): Map<K, V> = toPropsX<Any, V>(config).map { key(it) to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.buildPropMap(props: Iterable<KProperty1<Any, *>>): Map<K, V> =
        K::class.let { kClass ->
            when {
                kClass.isSubclassOf(String::class) -> props.map { it.name to it.get(this) }.toMap() as Map<K, V>
                kClass.isSubclassOf(KProperty1::class) -> props.map { it to it.get(this) }.toMap() as Map<K, V>
                else -> ierror("Unsupported type: $kClass!")
            }
        }

inline fun <reified K, reified V> Any.toPropMapX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): Map<K, V> = toPropsX<Any, V>(config).run(::buildPropMap)

inline fun <K, reified V> Any.toDeclaredPropMap(
        key: (KProperty1<Any, V>) -> K = { it as K },
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): Map<K, V> = toDeclaredPropsX<Any, V>(config).map { key(it) to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.toDeclaredPropMapX(
        noinline config: (KPropertyFilterConfig.() -> Unit)? = null
): Map<K, V> = toDeclaredPropsX<Any, V>(config).run(::buildPropMap)


// ==== Copy Prop Values ====

fun <T : Any, S : Any> T.copyPropValuesFrom(
        source: S,
        vararg props: KProperty1<S, *>,
        config: (KPropertyFilterConfig.() -> Unit)? = null
) {
    val sourceProps =
            (if (props.isEmpty()) source::class.memberProperties.toList() else props.toList())
                    .let {
                        KProperty1Filter.filterProps(source, it, config)
                    }
    
    this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().also { mutableProps ->
        mutableProps.forEach { targetProp ->
            sourceProps.find { sourceProp ->
                sourceProp.name == targetProp.name &&
                targetProp.returnType.isSupertypeOf(sourceProp.returnType)
            }?.let {
                targetProp.setter.call(this, it.getter.call(source))
            }
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


// ==== Generic ====

fun Any.getActualTypeInSuperclass(index: Int = 0) =
        (javaClass.genericSuperclass as? ParameterizedType ?: ierror("Generic superclass must be parameterized type!"))
                .actualTypeArguments[index]

fun <T> Any.getActualTypeInSuperclassX(index: Int = 0) = getActualTypeInSuperclass(index) as T