package com.monkeydp.tools.exception

import com.monkeydp.tools.exception.inner.InnerExConfigBuilder
import com.monkeydp.tools.exception.inner.InnerException

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun ierror(
        message: CharSequence,
        configInit: (InnerExConfigBuilder.() -> Unit)? = null
): Nothing = throw InnerException(message = message, configInit = configInit)

fun ierror(
        message: CharSequence,
        cause: Throwable,
        configInit: (InnerExConfigBuilder.() -> Unit)? = null
): Nothing = throw InnerException(message = message, cause = cause, configInit = configInit)

fun ierror(
        cause: Throwable,
        configInit: (InnerExConfigBuilder.() -> Unit)? = null
): Nothing = throw InnerException(cause = cause, configInit = configInit)
