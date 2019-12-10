package com.monkeydp.tools.ext

import com.monkeydp.tools.enumx.Enumx
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun <E : Enumx<out E>> KClass<out E>.valueOfOrNullX(name: String, caseSensitive: Boolean = false) =
        enumSet().matchOneOrNull { it.asEnum().name == transformEnumName(name, caseSensitive) }

fun <E : Enumx<out E>> KClass<out E>.valueOfX(name: String, caseSensitive: Boolean = false) =
        enumSet().matchOne { it.asEnum().name == transformEnumName(name, caseSensitive) }