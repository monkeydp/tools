package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/12/3
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any, reified R> Iterable<KProperty1<T, *>>.filterValueType(): List<KProperty1<T, R>> =
        filter { it.getValueKClass().isSubclassOf(R::class) } as List<KProperty1<T, R>>

fun Iterable<KProperty1<Any, *>>.toMap() = map { it.name to it.get(this) }