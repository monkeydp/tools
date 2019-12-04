package com.monkeydp.tools.exception.inner

/**
 * @author iPotato
 * @date 2019/10/14
 */
abstract class AbstractInnerException : InnerException<AbstractInnerException>, RuntimeException {
    constructor(message: CharSequence) : super(message.toString())
    constructor(message: CharSequence, cause: Throwable) : super(message.toString(), cause)
    constructor(cause: Throwable) : super(cause)
}