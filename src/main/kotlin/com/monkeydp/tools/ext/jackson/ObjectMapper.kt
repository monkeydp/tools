package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeydp.tools.ext.kotlin.asResource

/**
 * @author iPotato-Work
 * @date 2020/5/20
 */
fun ObjectMapper.readTreeByResource(resourcePath: String) = readTree(resourcePath.asResource())