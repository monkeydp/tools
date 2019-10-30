@file:Suppress("UNCHECKED_CAST")

package com.monkeydp.tools.ext

import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

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

fun <T> Any.toPropListX() = toPropList() as List<T>

fun Any.toDeclaredPropList() = this.javaClass.kotlin.declaredMemberProperties.map { it.get(this) }.toList()

fun <T> Any.toDeclaredPropListX() = toDeclaredPropList() as List<T>