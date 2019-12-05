package com.monkeydp.tools.exception.inner

/**
 * @author iPotato
 * @date 2019/10/14
 */
open class InnerException : Exception {
    constructor(message: CharSequence) : super(message.toString())
    constructor(message: CharSequence, cause: Throwable) : super(message.toString(), cause)
    constructor(cause: Throwable) : super(cause)
}