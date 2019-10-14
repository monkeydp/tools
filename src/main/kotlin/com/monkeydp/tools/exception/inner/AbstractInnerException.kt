package com.monkeydp.tools.exception.inner

import com.monkeydp.tools.exception.AbstractGlobalException

/**
 * @author iPotato
 * @date 2019/10/14
 */
abstract class AbstractInnerException : InnerException, AbstractGlobalException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}