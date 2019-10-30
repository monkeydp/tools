package com.monkeydp.tools.ext

/**
 * @author iPotato
 * @date 2019/10/24
 */
fun <E> MutableList<E>.append(vararg list: E) {
    this.addAll(list)
}

fun <E> MutableList<E>.replace(old: E, new: E) {
    val index = indexOf(old)
    this[index] = new
}

fun <E> MutableList<E>.replaceLast(new: E) {
    this[this.lastIndex] = new
}