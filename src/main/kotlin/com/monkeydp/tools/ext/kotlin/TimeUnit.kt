package com.monkeydp.tools.ext.kotlin

import java.util.concurrent.TimeUnit.*

/**
 * @author iPotato-Work
 * @date 2020/8/21
 */
fun Number.millisToSeconds() =
        SECONDS.convert(toLong(), MILLISECONDS)

fun Number.secondsToMillis() =
        MILLISECONDS.convert(toLong(), SECONDS)

fun Number.millisToMinutes() =
        MINUTES.convert(toLong(), MILLISECONDS)

fun Number.minutesToMillis() =
        MILLISECONDS.convert(toLong(), MINUTES)

fun Number.millisToHours() =
        HOURS.convert(toLong(), MILLISECONDS)

fun Number.hoursToMillis() =
        MILLISECONDS.convert(toLong(), HOURS)
