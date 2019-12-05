package com.monkeydp.tools.ext

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
    val matched = filter(predicate)
    if (matched.size != 1) {
        val msg = StringBuilder()
        msg.append("Element is matched ${matched.size} times, not once!")
        if (matched.size > 1) msg.append(" Following elements are matched: ${linesln()}")
        ierror(msg)
    }
    return matched.first()
}


// ==== Lines ====

private val Iterable<*>.lineSeparator
    get() = System.lineSeparator()

fun Iterable<*>.lines(): String {
    val indent = "|    "
    return "$indent${joinToString("$lineSeparator$indent")}".trimMargin()
}

fun Iterable<*>.linesln() = "$lineSeparator${lines()}"