package com.monkeydp.tools.ext.kotlin

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun <T : Any> Delegates.notNullSingleton(
        defaultValue: T? = null,
        ignoreAlreadyInitialized: Boolean = false
): ReadWriteProperty<Any?, T> =
        NotNullSingleInitVar(
                defaultValue,
                ignoreAlreadyInitialized
        )

fun <T : Any> Delegates.notNullSingleton(
        getDefaultValue: () -> T?,
        ignoreAlreadyInitialized: Boolean = false
): ReadWriteProperty<Any?, T> =
        NotNullSingleInitVar(
                getDefaultValue,
                ignoreAlreadyInitialized
        )