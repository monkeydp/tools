package com.monkeydp.tools.module.enumx

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.enumSet
import com.monkeydp.tools.ext.kotlin.findAnnotOrNull
import com.monkeydp.tools.ext.kotlin.transformEnumName
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/10/25
 */
interface Enumx<E>
        where E : Enumx<E>, E : Enum<E> {
    @Suppress("UNCHECKED_CAST")
    fun asEnum() = this as Enum<E>
}

fun <C : Enumx<*>, K : KClass<out C>> K.recurFindEnum(
        enumName: String,
        caseSensitive: Boolean = false
): C = recurFindEnumOrNull(enumName, caseSensitive)
        ?: ierror("No such enum named `$enumName` in `$this` and it's parent.")

/**
 * @param C enum contract
 */
@Suppress("UNCHECKED_CAST")
tailrec fun <C : Enumx<*>, K : KClass<out C>> K.recurFindEnumOrNull(
        enumName: String,
        caseSensitive: Boolean = false
): C? {
    var enum = findByNameOrNull(enumName, caseSensitive)
    if (enum != null) return enum as C

    val parent = findAnnotOrNull<EnumxOption>()?.parent
    if (parent == null || parent == Nothing::class) return null

    return (parent as KClass<out C>).recurFindEnumOrNull(enumName, caseSensitive)
}

fun <E : Enumx<out E>> KClass<out E>.findByNameOrNull(name: String, caseSensitive: Boolean = false) =
        enumSet().singleOrNull() { it.asEnum().name == transformEnumName(name, caseSensitive) }

fun <E : Enumx<out E>> KClass<out E>.findByName(name: String, caseSensitive: Boolean = false) =
        enumSet().single() { it.asEnum().name == transformEnumName(name, caseSensitive) }
