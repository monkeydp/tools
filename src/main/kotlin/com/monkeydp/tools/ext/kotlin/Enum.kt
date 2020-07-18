package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * @author iPotato
 * @date 2019/10/29
 */
inline fun <reified E : Enum<*>> E.toMemberPropMap() =
        E::class.memberProperties.map { it.name to it.get(this) }.toMap()

inline fun <reified E : Enum<*>> E.toDeclaredPropMap() =
        E::class.declaredMemberProperties.map { it.name to it.get(this) }.toMap()


fun <E : Enum<E>> KClass<E>.valueOfOrNull(name: String?, caseSensitive: Boolean = false) =
        if (name == null) null
        else enumSet().singleOrNull() { it.name == transformEnumName(name, caseSensitive) }

fun <E : Enum<E>> KClass<E>.valueOf(name: String, caseSensitive: Boolean = false) =
        enumSet().single { it.name == transformEnumName(name, caseSensitive) }

fun transformEnumName(enumName: String, caseSensitive: Boolean = false) =
        if (caseSensitive) enumName else enumName.toUpperCase()

fun <E : Enum<E>> KClass<E>.random(): E = java.enumConstants.toList().shuffled().first()

fun <E : Enum<E>, ID> KClass<E>.groupById(getId: (E) -> ID): Map<ID, E> =
        java.enumConstants.map { getId(it) to it }.toMap()