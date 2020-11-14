package com.monkeydp.tools.ext.google.common.collect

import com.google.common.collect.Multimap

/**
 * @author iPotato-Work
 * @date 2020/11/14
 */
fun <K, V> Multimap<K, V>.toPairs(): List<Pair<K, V>> =
    asMap().flatMap { entry ->
        values().map { Pair(entry.key, it) }
    }