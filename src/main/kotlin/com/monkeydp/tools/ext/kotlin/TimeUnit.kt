package com.monkeydp.tools.ext.kotlin

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*

/**
 * @author iPotato-Work
 * @date 2020/8/21
 */
fun Number.toSeconds(unit: TimeUnit) =
        SECONDS.convert(toLong(), unit)

fun Number.millisToSeconds() =
        SECONDS.convert(toLong(), MILLISECONDS)

fun Number.secondsToMillis() =
        MILLISECONDS.convert(toLong(), SECONDS)

fun Number.toMinutes(unit: TimeUnit) =
        MINUTES.convert(toLong(), unit)

fun Number.millisToMinutes() =
        MINUTES.convert(toLong(), MILLISECONDS)

fun Number.minutesToMillis() =
        MILLISECONDS.convert(toLong(), MINUTES)

fun Number.toHours(unit: TimeUnit) =
        HOURS.convert(toLong(), unit)

fun Number.millisToHours() =
        HOURS.convert(toLong(), MILLISECONDS)

fun Number.hoursToMillis() =
        MILLISECONDS.convert(toLong(), HOURS)

fun Number.toDays(unit: TimeUnit) =
        DAYS.convert(toLong(), unit)

fun Number.millisToDays() =
        DAYS.convert(toLong(), MILLISECONDS)

fun Number.daysToMillis() =
        MILLISECONDS.convert(toLong(), DAYS)
