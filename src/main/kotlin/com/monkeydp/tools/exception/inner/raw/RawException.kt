package com.monkeydp.tools.exception.inner.raw

import com.monkeydp.tools.exception.inner.InnerException

/**
 * @author iPotato
 * @date 2019/10/25
 */
interface RawException<E> : InnerException<E>
        where E : RawException<E>, E : RuntimeException