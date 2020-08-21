package com.monkeydp.tools.ext.kotlin

import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

/**
 * @author iPotato-Work
 * @date 2020/8/21
 */
fun Number.millisToMinutes() =
        MINUTES.convert(this.toLong(), MILLISECONDS)

fun Number.minutesToMillis() =
        MILLISECONDS.convert(this.toLong(), MINUTES)
