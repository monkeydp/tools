package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato
 * @date 2019/12/14
 */
fun Collection<*>.hasIndex(index: Int) = size >= index + 1

fun Collection<*>.hasNoIndex(index: Int) = !hasIndex(index)