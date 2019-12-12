package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * @author iPotato
 * @date 2019/10/29
 */
inline fun <reified E : Enum<*>> E.toPropMap() =
        E::class.memberProperties.map { it.name to it.get(this) }.toMap()

inline fun <reified E : Enum<*>> E.toDeclaredPropMap() =
        E::class.declaredMemberProperties.map { it.name to it.get(this) }.toMap()


fun <E : Enum<E>> KClass<E>.valueOfOrNull(name: String, caseSensitive: Boolean = false) =
        enumSet().matchOneOrNull { it.name == transformEnumName(name, caseSensitive) }

fun <E : Enum<E>> KClass<E>.valueOf(name: String, caseSensitive: Boolean = false) =
        enumSet().matchOne { it.name == transformEnumName(name, caseSensitive) }

fun transformEnumName(enumName: String, caseSensitive: Boolean = false) =
        if (caseSensitive) enumName else enumName.toUpperCase()