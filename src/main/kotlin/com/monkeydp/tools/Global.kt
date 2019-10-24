package com.monkeydp.tools

import com.monkeydp.tools.exception.inner.StdInnerException

/**
 * @author iPotato
 * @date 2019/10/24
 */
fun ierror(message: String): Nothing = throw StdInnerException(message)