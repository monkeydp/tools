package com.monkeydp.tools.ext

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

inline fun <reified E : Enum<*>> KClass<E>.valueOfOrNull(name: String) =
        E::class.java.enumConstants.firstOrNull { it.name == name }

inline fun <reified E : Enum<*>> KClass<E>.valueOf(name: String) = E::class.java.enumConstants.first { it.name == name }