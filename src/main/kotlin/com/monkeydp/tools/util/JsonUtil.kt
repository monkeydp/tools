package com.monkeydp.tools.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeydp.tools.config.kodein
import org.kodein.di.generic.instance

/**
 * @author iPotato
 * @date 2019/10/20
 */
object JsonUtil {

    val mapper by kodein.instance<ObjectMapper>()

    fun <T> toString(t: T) = mapper.writeValueAsString(t)

    inline fun <reified T> toObject(jsonStr: String) = mapper.readValue(jsonStr, T::class.java)

    fun <T> toObject(jsonStr: String, clazz: Class<T>) = mapper.readValue(jsonStr, clazz)

    inline fun <reified T> toObject(jsonNode: JsonNode) = mapper.treeToValue(jsonNode, T::class.java)

    fun <T> toObject(jsonNode: JsonNode, clazz: Class<T>) = mapper.treeToValue(jsonNode, clazz)

    fun toJsonNode(str: String) = mapper.readTree(str)!!

    fun toJsonNode(bytes: ByteArray) = mapper.readTree(bytes)!!

    inline fun <reified T> convertTo(any: Any) = mapper.convertValue(any, T::class.java)

    fun <T> convertTo(any: Any, clazz: Class<T>) = mapper.convertValue(any, clazz)
}