package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato
 * @date 2019/10/30
 */
/**
 * clear buffer
 */
fun StringBuilder.clear() {
    this.setLength(0)
}

fun StringBuilder.removeSuffix(suffix: CharSequence) {
    val index = lastIndexOf(suffix.toString())
    delete(index, index + suffix.length)
}