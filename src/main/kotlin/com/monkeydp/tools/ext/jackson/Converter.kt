package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.convertValue
import com.monkeydp.tools.global.objectMapper
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/9/10
 */
inline fun <reified T> Any.convertValue() =
    objectMapper.convertValue<T>(this)

fun <T> Any.convertValue(clazz: Class<T>) =
    objectMapper.convertValue(this, clazz)

fun <T : Any> Any.convertValue(kClass: KClass<T>) =
    objectMapper.convertValue(this, kClass.java)

fun <T> Any.convertValue(typeRef: TypeReference<T>) =
    objectMapper.convertValue(this, typeRef)

inline fun <reified T> Iterable<Any>.convertValue() =
    map { objectMapper.convertValue<T>(it) }.toList()


fun Any.toJson() =
    objectMapper.writeValueAsString(this)

fun <T> String.toObject(clazz: Class<T>): T =
    objectMapper.readValue(this, clazz)

fun <T : Any> String.toObject(kClass: KClass<T>): T =
    objectMapper.readValue(this, kClass.java)

inline fun <reified T> String.toObject(): T =
    toObject(T::class.java)


fun String.toJsonNode() =
    objectMapper.readTree(this)!!
