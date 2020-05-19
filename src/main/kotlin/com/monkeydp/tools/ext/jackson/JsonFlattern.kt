package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.annotation.JsonUnwrapped
import kotlin.annotation.AnnotationTarget.PROPERTY

/**
 * Flatten collection
 *
 * @see JsonUnwrapped If you want to flatten object, please use JsonUnwrapped
 *
 * @author iPotato-Work
 * @date 2020/5/18
 */
@Target(PROPERTY)
annotation class JsonFlatten(val times: Int = 1)