package com.monkeydp.tools.ext.main

import com.monkeydp.tools.enumx.Enumx
import com.monkeydp.tools.ext.kotlin.enumSet
import com.monkeydp.tools.ext.kotlin.transformEnumName
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun <E : Enumx<out E>> KClass<out E>.valueOfOrNullX(name: String, caseSensitive: Boolean = false) =
        enumSet().singleOrNull() { it.asEnum().name == transformEnumName(name, caseSensitive) }

fun <E : Enumx<out E>> KClass<out E>.valueOfX(name: String, caseSensitive: Boolean = false) =
        enumSet().single() { it.asEnum().name == transformEnumName(name, caseSensitive) }