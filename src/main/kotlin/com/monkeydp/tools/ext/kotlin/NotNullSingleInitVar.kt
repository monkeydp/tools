package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.exception.inner.PropertyUninitializedException
import com.monkeydp.tools.ext.main.ierror
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author iPotato
 * @date 2019/10/28
 */
internal class NotNullSingleInitVar<T : Any>(
        private val getDefaultValue: () -> T?,
        private val ignoreAlreadyInitializedError: Boolean = false
) : ReadWriteProperty<Any?, T> {
    
    constructor(defaultValue: T? = null,
                ignoreAlreadyInitializedError: Boolean = false
    ) : this({ defaultValue }, ignoreAlreadyInitializedError)
    
    private var value: T? = null
    public override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: getDefaultValue() ?: throw PropertyUninitializedException(property)
    }
    
    public override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (null == this.value) this.value = value
        else if (!ignoreAlreadyInitializedError) ierror("Property ${property.name} is already initialized.")
    }
}