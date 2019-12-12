package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/12/3
 */
inline fun <reified T> Iterable<KProperty1<Any, *>>.filterValueType() =
        filter { it.getValueKClass().isSubclassOf(T::class) }

fun Iterable<KProperty1<Any, *>>.toMap() = map { it.name to it.get(this) }