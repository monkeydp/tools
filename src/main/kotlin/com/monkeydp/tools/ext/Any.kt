@file:Suppress("UNCHECKED_CAST")

package com.monkeydp.tools.ext

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

fun Any.toPropMap() = this.javaClass.kotlin.memberProperties.map { it.name to it.get(this) }.toMap()

fun <K, V> Any.toPropMapX() = toPropMap() as Map<K, V>

fun Any.toDeclaredPropMap() = this.javaClass.kotlin.declaredMemberProperties.map { it.name to it.get(this) }.toMap()

fun <K, V> Any.toDeclaredPropMapX() = toDeclaredPropMap() as Map<K, V>

fun Any.toPropList() = this.javaClass.kotlin.memberProperties.map { it.get(this) }.toList()

inline fun <reified T> Any.toPropListX() = toPropList(this.javaClass.kotlin.memberProperties).filterIsInstance<T>()

fun Any.toDeclaredPropList() = toPropList(this.javaClass.kotlin.declaredMemberProperties)

fun Any.toPropList(props: Collection<KProperty1<Any, *>>) =
        props.filter {
            try {
                it.get(this)
                true
            } catch (e: IllegalCallableAccessException) {
                false
            }
        }.map { it.get(this) }

fun <T> Any.toDeclaredPropListX() = toDeclaredPropList() as List<T>

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

fun <T : Any, S : Any> T.copyFieldsFromX(source: S, vararg ignoreProps: KProperty<T>, forceAssess: Boolean = false) {
    copyFieldsFrom(source, *ignoreProps.map { it.javaField!! }.toTypedArray(), forceAssess = forceAssess)
}

fun <T : Any, S : Any> T.copyFieldsFrom(source: S, vararg ignoreFields: Field, forceAssess: Boolean = false) {
    val sourceFields = FieldUtil.getFields(source)
    sourceFields.forEach { sourceField ->
        val field = FieldUtil.getField(this, sourceField.name)
        if (ignoreFields.contains(field)) return
        val sourceValue = sourceField.get(source)
        if (sourceField.type.kotlin.isSubclassOf(field.type.kotlin))
            if (field.isAccessible || forceAssess)
                FieldUtil.setValue(this, field, sourceValue, true)
    }
}

fun <T : Any, R : Any> T.copyFieldsFrom(vararg pairs: Pair<KProperty1<T, R>, R>, forceAssess: Boolean = false) {
    pairs.forEach { pair ->
        FieldUtil.setValue(this, pair.first.name, pair.second, forceAssess)
    }
}

fun Any.toJson() = JsonUtil.toString(this)

inline fun <reified T> Any.convertTo() = JsonUtil.convertTo<T>(this)

fun <T> Any.convertTo(clazz: Class<T>) = JsonUtil.convertTo(this, clazz)

fun <T : Any> Any.convertTo(kClass: KClass<T>) = JsonUtil.convertTo(this, kClass.java)