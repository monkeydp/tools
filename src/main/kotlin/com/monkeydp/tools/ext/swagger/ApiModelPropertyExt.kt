package com.monkeydp.tools.ext.swagger

import com.monkeydp.tools.ext.kotlin.changeAttrs
import com.monkeydp.tools.ext.kotlin.findAnnotOrNull
import com.monkeydp.tools.ext.reflections.getAnnotatedKClasses
import com.monkeydp.tools.ext.reflections.reflections
import io.swagger.annotations.ApiModelProperty
import org.reflections.Reflections

/**
 * @author iPotato-Work
 * @date 2020/5/16
 */
class ApiModelPropertyExt(
        private val reflections: Reflections
) {
    constructor(packageName: String) : this(reflections(packageName))

    fun fixedPosition() {
        val apiModelKClasses = reflections.getAnnotatedKClasses(ApiFixedPosition::class)
        apiModelKClasses.forEach { apiModelKClass ->
            var position = 1
            apiModelKClass.java.declaredFields
                    .mapNotNull { it.findAnnotOrNull<ApiModelProperty>() }
                    .forEach start@{
                        it.changeAttrs(ApiModelProperty::position to position++)
                    }
        }
    }
}