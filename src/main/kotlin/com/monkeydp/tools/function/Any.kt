package com.monkeydp.tools.function

import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * @author iPotato
 * @date 2019/10/29
 */
inline fun <reified T : Any> T.toProps(): Properties {
    val properties = Properties()
    T::class.memberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}

inline fun <reified T : Any> T.toDeclaredProps(any: T): Properties {
    val properties = Properties()
    T::class.declaredMemberProperties.forEach { properties[it.name] = it.get(any) }
    return properties
}

inline fun <reified T : Any> T.toPropMap() =
        T::class.memberProperties.map { it.name to it.get(this) }.toMap()

inline fun <reified T : Any> T.toDeclaredPropMap() =
        T::class.declaredMemberProperties.map { it.name to it.get(this) }.toMap()