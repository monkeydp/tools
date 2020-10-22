package com.monkeydp.tools.ext.javax.servlet

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper


/**
 * @author iPotato-Work
 * @date 2020/6/2
 */
class MutableHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val customHeaders = mutableMapOf<String, String>()

    fun putHeader(name: String, value: String) {
        customHeaders[name] = value
    }

    override fun getHeader(name: String): String? =
            customHeaders[name] ?: (request as HttpServletRequest).getHeader(name)

    override fun getHeaderNames(): Enumeration<String> {
        val headerKeySet = customHeaders.keys.toMutableSet()

        val headerNames = (request as HttpServletRequest).headerNames
        while (headerNames.hasMoreElements()) {
            headerKeySet.add(headerNames.nextElement())
        }

        return Collections.enumeration(headerKeySet)
    }
}
