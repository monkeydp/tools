package com.monkeydp.tools.function

/**
 * @author iPotato
 * @date 2019/10/24
 */
fun <T> MutableList<T>.append(vararg list: T) {
    this.addAll(list)
}