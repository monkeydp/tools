package com.monkeydp.tools.ext.kotlin

import com.fasterxml.jackson.module.kotlin.convertValue
import com.monkeydp.tools.global.objectMapper
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/11/9
 */

/**
 *  If match once, return matched element
 *  else throw ex
 */
// ==== Has ====

fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean {
    val matched = filter(predicate)
    return matched.size >= 1
}

// ==== Lines ====

val Iterable<*>.indent get() = "|    "
val Iterable<*>.lineSeparator get() = System.lineSeparator()
val Iterable<*>.lineSeparatorWithIndent get() = "$lineSeparator$indent"

fun Iterable<*>.lines() = "$indent${joinToString("$lineSeparatorWithIndent")}".trimMargin()

fun Iterable<*>.linesln() = "$lineSeparator${lines()}"


// ==== Filter Is Instance ====

@Suppress("UNCHECKED_CAST")
fun <T : Any> Iterable<*>.filterIsInstance(kClass: KClass<T>): List<T> =
        filter { it != null && it::class.isSubclassOf(kClass) } as List<T>


// ==== Json ====

inline fun <reified T> Iterable<Any>.convertValue() =
        map { objectMapper.convertValue<T>(it) }.toList()

// ==== Group ====

public inline fun <K, V> Iterable<V>.groupById(
        keySelector: (V) -> K
): Map<K, V> =
        groupBy(keySelector)
                .map { it.key to it.value.first() }
                .toMap()

public inline fun <K, V> Iterable<V>.findById(
        id: K,
        keySelector: (V) -> K
): V =
        groupById(keySelector)
                .getValue(id)
