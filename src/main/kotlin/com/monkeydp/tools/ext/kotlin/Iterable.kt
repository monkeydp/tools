package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.main.ierror

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


// ==== Match ====

inline fun <T> Iterable<T>.matchOne(predicate: (T) -> Boolean): T {
    val t = matchOneOrNull(predicate)
    if (t != null) return t
    ierror(matchOneErrorMsg(0))
}

inline fun <T> Iterable<T>.matchOneOrNull(predicate: (T) -> Boolean): T? {
    val matched = filter(predicate)
    return when (val size = matched.size) {
        0 -> null
        1 -> matched.first()
        else -> ierror("${matchOneErrorMsg(size)} Following elements are matched: ${matched.linesln()}")
    }
}

fun matchOneErrorMsg(size: Int) = StringBuilder("Element is matched $size times, not once!")


// ==== Lines ====

private val Iterable<*>.lineSeparator
    get() = System.lineSeparator()

fun Iterable<*>.lines(): String {
    val indent = "|    "
    return "$indent${joinToString("$lineSeparator$indent")}".trimMargin()
}

fun Iterable<*>.linesln() = "$lineSeparator${lines()}"