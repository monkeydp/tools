package com.monkeydp.tools.ext.javax.servlet

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.jackson.convertValue
import javax.servlet.http.HttpServletRequest
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/10/13
 */
fun <T : Any> HttpServletRequest.getParam(name: String, kClass: KClass<T>): T =
        getParamOrNull(name, kClass) ?: ierror("Request param `$name` not exist, QueryString: $queryString")

fun <T : Any> HttpServletRequest.getParamOrNull(name: String, kClass: KClass<T>): T? {
    val arg: String? = getParameter(name)
    if (arg == null) return arg
    return arg.convertValue(kClass)
}

inline fun <reified T : Any> HttpServletRequest.getParam(name: String) =
        getParam(name, T::class)

inline fun <reified T : Any> HttpServletRequest.getParamOrNull(name: String) =
        getParamOrNull(name, T::class)
