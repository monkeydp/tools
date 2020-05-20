package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author iPotato-Work
 * @date 2020/5/20
 */
inline fun <reified T> ObjectMapper.convertValueX(any: Any) = convertValue(any, T::class.java)