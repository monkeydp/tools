package com.monkeydp.tools.exception.inner

/**
 * @author iPotato
 * @date 2019/10/14
 */
class StdInnerException : AbstractInnerException {
    constructor(message: CharSequence) : super(message)
    constructor(message: CharSequence, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}