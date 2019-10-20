package com.monkeydp.tools.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author iPotato
 * @date 2019/10/20
 */
object JsonUtil {
    private val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * Convert object to another class
     */
    fun <T> convertTo(any: Any, clazz: Class<T>): T {
        return mapper.convertValue(any, clazz)
    }
}