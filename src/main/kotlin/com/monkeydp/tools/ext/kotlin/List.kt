package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun <E> List<E>.lastOf(index: Int) =
        get(size - index - 1)