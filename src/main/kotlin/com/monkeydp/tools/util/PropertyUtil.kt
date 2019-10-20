package com.monkeydp.tools.util

import java.util.*

/**
 * @author iPotato
 * @date 2019/10/20
 */
object PropertyUtil {
    /**
     * Convert object to Properties
     */
    fun from(any: Any): Properties {
        val properties = Properties()
        val fields = FieldUtil.getFields(any)
        fields.forEach { field ->
            properties[field.name] = FieldUtil.getValue(any, field)
        }
        return properties
    }
}