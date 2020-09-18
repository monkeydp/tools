package com.monkeydp.tools.ext.kotlin

import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * @author iPotato-Work
 * @date 2020/9/18
 */
val Throwable.stackTraceString
    get() = ExceptionUtils.getStackTrace(this
    )
