package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeydp.tools.ext.kotlin.asResource
import java.net.URL

/**
 * @author iPotato-Work
 * @date 2020/5/20
 */
fun ObjectMapper.readTreeByResource(resourcePath: String) = readTree(resourcePath.asResource())

inline fun <reified T> ObjectMapper.readValue(src: URL) = readValue(src, T::class.java)