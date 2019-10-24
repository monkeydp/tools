package com.monkeydp.tools.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author iPotato
 * @date 2019/10/20
 */
object JsonUtil {
    
    val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    
    fun <T> toString(t: T): String {
        return mapper.writeValueAsString(t)
    }
    
    inline fun <reified T> toObject(jsonStr: String): T {
        return mapper.readValue(jsonStr, T::class.java)
    }
    
    fun <T> toObject(jsonStr: String, typeReference: TypeReference<T>): T {
        return mapper.readValue<T>(jsonStr, typeReference)
    }
    
    /**
     * Convert object to another class
     */
    inline fun <reified T> convertTo(any: Any): T {
        return mapper.convertValue(any, T::class.java)
    }
}