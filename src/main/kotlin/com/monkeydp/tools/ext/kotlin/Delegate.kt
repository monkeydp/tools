package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.exception.inner.InnerException
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author iPotato
 * @date 2019/10/29
 */
// ==== Nullable Singleton ====

fun <T : Any> Delegates.singletonOrNull(
        getDefaultValue: (() -> T?)? = null,
        ignoreAlreadyInitialized: Boolean = false
): ReadWriteProperty<Any?, T?> =
        NullableSingleInitVar(
                getDefaultValue,
                ignoreAlreadyInitialized
        )

private class NullableSingleInitVar<T : Any>(
        private val getDefaultValue: (() -> T?)? = null,
        private val ignoreAlreadyInitializedEx: Boolean = false
) : ReadWriteProperty<Any?, T?> {
    
    constructor(defaultValue: T? = null,
                ignoreAlreadyInitializedError: Boolean = false
    ) : this({ defaultValue }, ignoreAlreadyInitializedError)
    
    private var isInitialized: Boolean = false
    private var value: T? = null
    
    public override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
            when {
                isInitialized -> value
                getDefaultValue != null -> getDefaultValue.let { it() }
                else -> throw PropertyUninitializedException(property)
            }
    
    @Synchronized
    public override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        when {
            !isInitialized -> {
                this.value = value
                isInitialized = true
            }
            !ignoreAlreadyInitializedEx -> throw PropertyAlreadyInitializedException(property)
            else -> {
                // ignore
            }
        }
    }
}


// ==== Not Null Singleton ====

fun <T : Any> Delegates.singleton(
        defaultValue: T? = null,
        ignoreAlreadyInitialized: Boolean = false
): ReadWriteProperty<Any?, T> =
        NotNullSingleInitVar(
                defaultValue,
                ignoreAlreadyInitialized
        )

fun <T : Any> Delegates.singleton(
        getDefaultValue: () -> T?,
        ignoreAlreadyInitialized: Boolean = false
): ReadWriteProperty<Any?, T> =
        NotNullSingleInitVar(
                getDefaultValue,
                ignoreAlreadyInitialized
        )

private class NotNullSingleInitVar<T : Any>(
        private val getDefaultValue: () -> T?,
        private val ignoreAlreadyInitializedEx: Boolean = false
) : ReadWriteProperty<Any?, T> {
    
    constructor(defaultValue: T? = null,
                ignoreAlreadyInitializedError: Boolean = false
    ) : this({ defaultValue }, ignoreAlreadyInitializedError)
    
    private val isInitialized: Boolean get() = this.value != null
    private var value: T? = null
    public override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            value ?: getDefaultValue() ?: throw PropertyUninitializedException(property)
    
    public override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (!isInitialized) this.value = value
        else if (!ignoreAlreadyInitializedEx)
            throw PropertyAlreadyInitializedException(property)
    }
}

class PropertyUninitializedException(property: KProperty<*>)
    : InnerException("Property `${property.name}` should be initialized before get.")

class PropertyAlreadyInitializedException(property: KProperty<*>)
    : InnerException("Property `${property.name}` is already initialized.")