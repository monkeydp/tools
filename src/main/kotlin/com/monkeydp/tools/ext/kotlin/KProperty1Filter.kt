package com.monkeydp.tools.ext.kotlin

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * @author iPotato
 * @date 2019/12/16
 */
object KProperty1Filter {
    
    fun <T : Any, R> filterProps(
            any: T,
            props: Iterable<KProperty1<out T, R>>,
            config: (KPropertyFilterConfig.() -> Unit)? = null
    ): List<KProperty1<T, R>> =
            KPropertyFilter.filterProps(any, props.map { it as KProperty<R> }, config)
                    .map {
                        @Suppress("UNCHECKED_CAST")
                        it as KProperty1<T, R>
                    }
}
