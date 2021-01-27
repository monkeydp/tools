package com.monkeydp.tools.ext.java

import com.monkeydp.tools.ext.kotlin.setFieldValue
import com.monkeydp.tools.global.objectMapper
import java.net.URL
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/4
 */
inline fun <reified T> URL.readValue() =
    objectMapper.readValue(this, T::class.java)

fun <T> URL.readValue(clazz: Class<T>) =
    objectMapper.convertValue(this, clazz)

fun <T : Any> URL.readValue(kClass: KClass<T>) =
    objectMapper.convertValue(this, kClass.java)

fun URL.clone() = URL(toString())

fun URL.setPath(path: String) {
    setFieldValue("path", "$path") { forceAccess = true }
}

fun URL.appendPathPrefix(pathPrefix: String) {
    setPath("/${pathPrefix.removePrefix("/").removeSuffix("/")}/${path.removePrefix("/")}")
}