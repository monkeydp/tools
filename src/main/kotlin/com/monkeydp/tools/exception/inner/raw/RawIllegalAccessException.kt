package com.monkeydp.tools.exception.inner.raw

/**
 * @author iPotato
 * @date 2019/10/15
 */
class RawIllegalAccessException : AbstractRawException {
    constructor() : super(IllegalAccessException())
}