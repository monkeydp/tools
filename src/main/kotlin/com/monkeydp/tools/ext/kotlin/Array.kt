package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato-Work
 * @date 2020/12/7
 */
inline fun <reified T> Array<*>.firstInstanceOrNull() =
        firstOrNull {
            if (it == null) return@firstOrNull false
            it.javaClass.kotlin.isSubclassOf(T::class)
        } as? T

