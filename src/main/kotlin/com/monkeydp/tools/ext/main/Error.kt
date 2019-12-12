package com.monkeydp.tools.ext.main

import com.monkeydp.tools.exception.inner.InnerException

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun ierror(message: CharSequence): Nothing = throw InnerException(message)

fun ierror(message: CharSequence, cause: Throwable): Nothing = throw InnerException(message, cause)

fun ierror(cause: Throwable): Nothing = throw InnerException(cause)