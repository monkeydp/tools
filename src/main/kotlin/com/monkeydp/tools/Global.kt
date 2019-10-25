package com.monkeydp.tools

import com.monkeydp.tools.exception.inner.StdInnerException

/**
 * @author iPotato
 * @date 2019/10/24
 */
fun ierror(message: String): Nothing = throw StdInnerException(message)

fun ierror(message: String, cause: Throwable): Nothing = throw StdInnerException(message, cause)

fun ierror(cause: Throwable): Nothing = throw StdInnerException(cause)

fun <T> MutableList<T>.append(vararg list: T) {
    this.addAll(list)
}