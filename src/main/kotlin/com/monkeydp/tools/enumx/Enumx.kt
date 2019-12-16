package com.monkeydp.tools.enumx

import com.monkeydp.tools.ext.main.ierror
import com.monkeydp.tools.ext.main.valueOfOrNullX
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

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
    var enum = valueOfOrNullX(enumName, caseSensitive)
    if (enum != null) return enum as C
    
    val parent = findAnnotation<EnumxOption>()?.parent
    if (parent == null || parent == Nothing::class) return null
    
    return (parent as KClass<out C>).recurFindEnumOrNull(enumName, caseSensitive)
}