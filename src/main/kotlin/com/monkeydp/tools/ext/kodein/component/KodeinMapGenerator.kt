package com.monkeydp.tools.ext.kodein.component

/**
 * @author iPotato
 * @date 2019/12/13
 */
interface KodeinMapGenerator<K, V> {
    fun generate(components: Iterable<V>): Map<K, V>
}