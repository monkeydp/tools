package com.monkeydp.tools.ext.kotlin

import java.util.concurrent.TimeUnit.*

/**
 * @author iPotato-Work
 * @date 2020/8/21
 */
fun Number.millisToSeconds() =
        SECONDS.convert(this.toLong(), MILLISECONDS)

fun Number.secondsToMillis() =
        MILLISECONDS.convert(this.toLong(), SECONDS)

fun Number.millisToMinutes() =
        MINUTES.convert(this.toLong(), MILLISECONDS)

fun Number.minutesToMillis() =
        MILLISECONDS.convert(this.toLong(), MINUTES)

fun Number.millisToHours() =
        HOURS.convert(this.toLong(), MILLISECONDS)

fun Number.hoursToMillis() =
        MILLISECONDS.convert(this.toLong(), HOURS)
