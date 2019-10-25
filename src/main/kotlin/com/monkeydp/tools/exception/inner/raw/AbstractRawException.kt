package com.monkeydp.tools.exception.inner.raw

/**
 * @author iPotato
 * @date 2019/10/15
 */
abstract class AbstractRawException : RawException<AbstractRawException>, RuntimeException {
    constructor(cause: Throwable) : super(cause)
}