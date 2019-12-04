package com.monkeydp.tools.ext

import com.monkeydp.tools.exception.inner.StdInnerException

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun ierror(message: CharSequence): Nothing = throw StdInnerException(message)

fun ierror(message: CharSequence, cause: Throwable): Nothing = throw StdInnerException(message, cause)

fun ierror(cause: Throwable): Nothing = throw StdInnerException(cause)