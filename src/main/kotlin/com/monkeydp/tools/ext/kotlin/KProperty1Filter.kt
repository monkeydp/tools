package com.monkeydp.tools.ext.kotlin

import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.IllegalCallableAccessException

/**
 * @author iPotato
 * @date 2019/12/16
 */
object KProperty1Filter {
    
    fun <T : Any> filterProps(
            any: T,
            props: Iterable<KProperty1<T, *>>,
            config: (KProperty1FilterConfig.() -> Unit)? = null
    ): Iterable<KProperty1<T, *>> =
            with(KProperty1FilterConfig()) {
                if (config != null) config(this)
                var filteredProps = props
                if (ignoreIllegalAccess) filteredProps = filterIllegalAccessible(any, props)
                if (ignoreUninitialized) filteredProps = filterUninitialized(any, props)
                filteredProps
            }
    
    private fun <T : Any> filterIllegalAccessible(
            any: T,
            props: Iterable<KProperty1<T, *>>
    ) = props.filter {
        try {
            it.get(any)
            true
        } catch (e: IllegalCallableAccessException) {
            false
        }
    }
    
    private fun <T : Any> filterUninitialized(
            any: T,
            props: Iterable<KProperty1<T, *>>
    ) = props.filter {
        try {
            it.get(any)
            true
        } catch (e: InvocationTargetException) {
            if (e.targetException is PropertyUninitializedException) false
            else throw e
        }
    }
}

class KProperty1FilterConfig {
    var ignoreIllegalAccess: Boolean = false
    var ignoreUninitialized: Boolean = true
}