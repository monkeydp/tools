package com.monkeydp.tools.exception.inner

import com.monkeydp.tools.exception.GlobalException

/**
 * @author iPotato
 * @date 2019/10/25
 */
interface InnerException<E> : GlobalException<E>
        where E : InnerException<E>, E : RuntimeException