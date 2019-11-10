package com.monkeydp.tools.ext

/**
 * @author iPotato
 * @date 2019/11/10
 */
fun <K, V> mapOfAll(vararg maps: Map<K, V>): Map<K, V> {
    val map = mutableMapOf<K, V>()
    maps.forEach { map.putAll(it) }
    return map.toMap()
}