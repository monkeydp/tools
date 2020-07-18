package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.kotlin.KPropertyFilter.FilterConfig
import kotlin.reflect.KProperty1

/**
 * @author iPotato
 * @date 2019/12/16
 */
object KProperty1Filter {

    fun <T : Any, R> filterMemberProps(
            any: T,
            props: Iterable<KProperty1<out T, R>>,
            config: (FilterConfig.() -> Unit)? = null
    ): List<KProperty1<T, R>> =
            KPropertyFilter.filterProps(any, props, config)
                    .map {
                        @Suppress("UNCHECKED_CAST")
                        it as KProperty1<T, R>
                    }
}
