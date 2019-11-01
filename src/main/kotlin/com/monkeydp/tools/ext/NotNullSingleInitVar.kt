package com.monkeydp.tools.ext

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author iPotato
 * @date 2019/10/28
 */
internal class NotNullSingleInitVar<T : Any>() : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    public override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: ierror("Property ${property.name} should be initialized before get.")
    }
    
    public override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (null == this.value) this.value = value
        else ierror("Property ${property.name} is already initialized.")
    }
}