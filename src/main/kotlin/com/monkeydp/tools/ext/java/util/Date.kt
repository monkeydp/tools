package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.java.util.toDate
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.math.floor

/**
 * @author iPotato-Work
 * @date 2020/8/10
 */
val HHmmssFormat = SimpleDateFormat("HH:mm:ss")

val Date.HHmmss
    get() = HHmmssFormat.format(this)

val MMddHHmmssFormat = SimpleDateFormat("MM-dd HH:mm:ss")

val Date.MMddHHmmss
    get() = MMddHHmmssFormat.format(this)

val yyyyMMddHHmmssFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

val Date.yyyyMMddHHmmss
    get() = yyyyMMddHHmmssFormat.format(this)

val Date.yearx
    get() =
        toCalendar().get(YEAR)

fun Date.toCalendar() =
        Calendar.getInstance().also {
            it.setTime(this)
        }

fun Date.pretty(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val now = Date()
    if (now < this)
        ierror("Time must be earlier than now. Time: $time, now: ${now.time}")

    // seconds
    val diff = (now.time - time) / 1000
    return when {
        diff < 60 -> "刚刚"
        diff < 3600 -> "${(floor(diff.toDouble() / 60)).toInt()} 分钟前"
        diff < 3600 * 24 -> "${(floor(diff.toDouble() / 3600)).toInt()} 小时前"
        diff < 3600 * 24 * 7 -> "${(floor(diff.toDouble() / (3600 * 24))).toInt()} 天前"
        else -> SimpleDateFormat(pattern).format(this)
    }
}

fun Date.plus(field: Int, amount: Int) =
        toCalendar().apply {
            add(field, amount)
        }.toDate()

fun Date.plusYear(amount: Int) =
        plus(YEAR, amount)

fun Date.minusYear(amount: Int) =
        plusYear(-amount)

fun Date.plusMouth(amount: Int) =
        plus(MONTH, amount)

fun Date.minusMouth(amount: Int) =
        plusMouth(-amount)

fun Date.plusDay(amount: Int) =
        plus(DATE, amount)

fun Date.minusDay(amount: Int) =
        plusDay(-amount)

fun Date.plusHour(amount: Int) =
        plus(HOUR, amount)

fun Date.minusHour(amount: Int) =
        plusHour(-amount)

fun Date.plusMinute(amount: Int) =
        plus(MINUTE, amount)

fun Date.minusMinute(amount: Int) =
        plusMinute(-amount)

fun Date.plusSecond(amount: Int) =
        plus(SECOND, amount)

fun Date.minusSecond(amount: Int) =
        plusSecond(-amount)

fun Date.plusMillis(amount: Int) =
        plus(MILLISECOND, amount)

fun Date.minusMillis(amount: Int) =
        plusMillis(-amount)
