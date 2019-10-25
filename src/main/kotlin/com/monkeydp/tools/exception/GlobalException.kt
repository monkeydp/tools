package com.monkeydp.tools.exception

/**
 * @author iPotato
 * @date 2019/10/25
 */
interface GlobalException<E>
        where E : GlobalException<E>, E : RuntimeException