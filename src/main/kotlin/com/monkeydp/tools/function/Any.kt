package com.monkeydp.tools.function

import java.util.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * @author iPotato
 * @date 2019/10/29
 */
inline fun <reified T : Any> T.toProperties(): Properties {
    val properties = Properties()
    T::class.memberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}

inline fun <reified T : Any> T.toDeclaredProperties(any: T): Properties {
    val properties = Properties()
    T::class.declaredMemberProperties.forEach { properties[it.name] = it.get(any) }
    return properties
}