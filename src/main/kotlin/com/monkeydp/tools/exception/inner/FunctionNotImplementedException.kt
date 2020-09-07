package com.monkeydp.tools.exception.inner

/**
 * @author iPotato
 * @date 2019/12/27
 */
class FunctionNotImplementedException(
        functionName: String = Thread.currentThread().stackTrace[2].methodName
) : InnerEx("Function `$functionName` is not implemented!")
