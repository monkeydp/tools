package com.monkeydp.tools.ext.kotlin

import java.util.concurrent.ConcurrentMap

/**
 * @author iPotato-Work
 * @date 2020/9/18
 */
fun <K, V> ConcurrentMap<K, V>.removeAllByKeys(keys: Iterable<K>) {
    keys.forEach { remove(it) }
}
