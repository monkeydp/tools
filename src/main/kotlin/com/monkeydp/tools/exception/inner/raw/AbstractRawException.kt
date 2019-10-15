package com.monkeydp.tools.exception.inner.raw

import com.monkeydp.tools.exception.inner.AbstractInnerException

/**
 * @author iPotato
 * @date 2019/10/15
 */
abstract class AbstractRawException : RawException, AbstractInnerException {
    constructor(cause: Throwable) : super(cause)
}