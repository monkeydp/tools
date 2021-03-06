package com.monkeydp.tools.ext.kotlin

import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/12/16
 */
object KPropertyFilter {

    class FilterConfig {
        var ignoreIllegalAccess: Boolean = true
        var ignoreUninitialized: Boolean = true
    }

    fun <R> filterProps(
            any: Any,
            props: Iterable<KProperty<R>>,
            configInit: (FilterConfig.() -> Unit)? = null
    ): List<KProperty<R>> =
            FilterConfig().run {
                configInit?.invoke(this)
                var filteredProps = props
                if (ignoreIllegalAccess) filteredProps =
                        filterException(any, props, IllegalCallableAccessException::class)
                if (ignoreUninitialized) filteredProps =
                        filterInvocationTargetException(any, filteredProps, PropertyUninitializedException::class)
                filteredProps
            }.toList()

    private fun <R> filterException(
            any: Any,
            props: Iterable<KProperty<R>>,
            exKClass: KClass<out Throwable>
    ) = props.filter {
        try {
            it.getter.call(any)
            true
        } catch (e: Throwable) {
            if (e::class.isSubclassOf(exKClass)) false
            else true
        }
    }

    private fun <R> filterInvocationTargetException(
            any: Any,
            props: Iterable<KProperty<R>>,
            exKClass: KClass<out Throwable>
    ) = props.filter {
        try {
            it.getter.call(any)
            true
        } catch (e: InvocationTargetException) {
            if (e.targetException::class.isSubclassOf(exKClass)) false
            else true
        }
    }
}
