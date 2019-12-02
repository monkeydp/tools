package com.monkeydp.tools.exception.inner

import kotlin.reflect.KProperty

/**
 * @author iPotato
 * @date 2019/12/2
 */
class PropertyUninitializedException(property: KProperty<*>)
    : AbstractInnerException("Property ${property.name} should be initialized before get.")