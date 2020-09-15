package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/11/9
 */
// ==== Has ====

fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean =
        firstOrNull(predicate) != null

fun <T> Iterable<T>.hasNo(predicate: (T) -> Boolean): Boolean =
        !has(predicate)

fun <T> Iterable<T>.hasSingle(predicate: (T) -> Boolean): Boolean =
        singleOrNull(predicate) != null

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


// ==== Group ====

inline fun <K, V> Iterable<V>.groupById(
        keySelector: (V) -> K
): Map<K, V> =
        groupBy(keySelector)
                .map { it.key to it.value.first() }
                .toMap()

inline fun <K, V> Iterable<V>.findById(
        id: K,
        keySelector: (V) -> K
): V =
        groupById(keySelector)
                .getValue(id)

inline fun <K, V> Iterable<V>.mergeById(
        another: Iterable<V>,
        keySelector: (V) -> K
): List<V> =
        groupById(keySelector)
                .toMutableMap()
                .apply {
                    putAll(another.groupById(keySelector))
                }.values.toList()

inline fun <K, V> Iterable<V>.mergeById(
        vararg elements: V,
        keySelector: (V) -> K
): List<V> =
        groupById(keySelector)
                .toMutableMap()
                .apply {
                    putAll(elements.toList().groupById(keySelector))
                }.values.toList()
