package com.monkeydp.tools.ext.kotlin

import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.isSubclassOf

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
                if (ignoreIllegalAccess) filteredProps =
                        filterException(any, props, IllegalCallableAccessException::class)
                if (ignoreUninitialized) filteredProps =
                        filterInvocationTargetException(any, props, PropertyUninitializedException::class)
                filteredProps
            }
    
    private fun <T : Any> filterException(
            any: T,
            props: Iterable<KProperty1<T, *>>,
            exKClass: KClass<out Throwable>
    ) = props.filter {
        try {
            it.get(any)
            true
        } catch (e: Throwable) {
            if (e::class.isSubclassOf(exKClass)) false
            else throw e
        }
    }
    
    private fun <T : Any> filterInvocationTargetException(
            any: T,
            props: Iterable<KProperty1<T, *>>,
            exKClass: KClass<out Throwable>
    ) = props.filter {
        try {
            it.get(any)
            true
        } catch (e: InvocationTargetException) {
            if (e.targetException::class.isSubclassOf(exKClass)) false
            else throw e
        }
    }
}

class KProperty1FilterConfig {
    var ignoreIllegalAccess: Boolean = false
    var ignoreUninitialized: Boolean = true
}