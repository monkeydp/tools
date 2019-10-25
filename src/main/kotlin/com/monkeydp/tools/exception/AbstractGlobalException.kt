package com.monkeydp.tools.exception

/**
 * @author iPotato
 * @date 2019/10/14
 */
abstract class AbstractGlobalException : GlobalException<AbstractGlobalException>, RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}