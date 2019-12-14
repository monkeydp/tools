package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato
 * @date 2019/12/14
 */
fun Collection<*>.hasIndex(index: Int) = size >= index + 1

fun Collection<*>.hasNoIndex(index: Int) = !hasIndex(index)

fun Collection<*>.equalsX(another: Collection<*>, ignoreSorting: Boolean = false) =
        if (ignoreSorting)
            size == another.size && containsAll(another)
        else this == another