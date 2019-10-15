package com.monkeydp.tools.exception.inner.raw

/**
 * @author iPotato
 * @date 2019/10/15
 */
class RawNoSuchFieldException : AbstractRawException {
    constructor() : super(NoSuchFieldException())
}