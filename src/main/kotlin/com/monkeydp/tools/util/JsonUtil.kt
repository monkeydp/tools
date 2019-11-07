package com.monkeydp.tools.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * @author iPotato
 * @date 2019/10/20
 */
object JsonUtil {
    
    var mapper = ObjectMapper()
            .registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    
    fun <T> toString(t: T): String {
        return if (t is String) t
        else mapper.writeValueAsString(t)
    }
    
    inline fun <reified T> toObject(jsonStr: String) = mapper.readValue(jsonStr, T::class.java)
    
    fun <T> toObject(jsonStr: String, clazz: Class<T>) = mapper.readValue(jsonStr, clazz)
    
    inline fun <reified T> toObject(jsonNode: JsonNode) = mapper.treeToValue(jsonNode, T::class.java)
    
    fun <T> toObject(jsonNode: JsonNode, clazz: Class<T>) = mapper.treeToValue(jsonNode, clazz)
    
    fun toJsonNode(jsonStr: String) = mapper.readTree(jsonStr)
    
    inline fun <reified T> convertTo(any: Any) = mapper.convertValue(any, T::class.java)
    
    fun <T> convertTo(any: Any, clazz: Class<T>) = mapper.convertValue(any, clazz)
}