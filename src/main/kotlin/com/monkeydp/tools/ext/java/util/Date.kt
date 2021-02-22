package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.math.floor

/**
 * @author iPotato-Work
 * @date 2020/8/10
 */
const val HHmmss = "HH:mm:ss"
val HHmmssFormat = SimpleDateFormat(HHmmss)

const val MMddHHmmss = "MM-dd HH:mm:ss"
val MMddHHmmssFormat = SimpleDateFormat(MMddHHmmss)

const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
val yyyyMMddHHmmssFormat = SimpleDateFormat(yyyyMMddHHmmss)

val Date.HHmmss
    get() = HHmmssFormat.format(this)

val Date.MMddHHmmss
    get() = MMddHHmmssFormat.format(this)

val Date.yyyyMMddHHmmss
    get() = yyyyMMddHHmmssFormat.format(this)

val Date.yearx
    get() =
        toCalendar().get(YEAR)

fun Date.toCalendar() =
        getInstance().also {
            it.setTime(this)
        }

fun Date.pretty(pattern: String = yyyyMMddHHmmss): String {
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

/**
 * @param unit the calendar unit like Calendar.YEAR
 * @param amount the amount of date or time to be added to the field
 */
fun Date.plus(amount: Int, unit: Int) =
        toCalendar().apply {
            add(unit, amount)
        }.time

fun Date.plusYear(amount: Int) =
        plus(amount, YEAR)

fun Date.minusYear(amount: Int) =
        plusYear(-amount)

fun Date.plusMouth(amount: Int) =
        plus(amount, MONTH)

fun Date.minusMouth(amount: Int) =
        plusMouth(-amount)

fun Date.plusDay(amount: Int) =
        plus(amount, DATE)

fun Date.minusDay(amount: Int) =
        plusDay(-amount)

fun Date.plusHour(amount: Int) =
        plus(amount, HOUR)

fun Date.minusHour(amount: Int) =
        plusHour(-amount)

fun Date.plusMinute(amount: Int) =
        plus(amount, MINUTE)

fun Date.minusMinute(amount: Int) =
        plusMinute(-amount)

fun Date.plusSecond(amount: Int) =
        plus(amount, SECOND)

fun Date.minusSecond(amount: Int) =
        plusSecond(-amount)

fun Date.plusMillis(amount: Int) =
        plus(amount, MILLISECOND)

fun Date.minusMillis(amount: Int) =
        plusMillis(-amount)


/**
 * 向上取整
 *
 * 比如 19:05:15 -> 19:06:00
 *
 * @param field  like Calendar.MINUTE
 */
fun Date.ceil(field: Int) =
        DateUtils.ceiling(this, field)

/**
 * 向下取整
 *
 * 比如 19:05:15 -> 19:05:00
 *
 * @param field  like Calendar.MINUTE
 */
fun Date.floor(field: Int) =
        DateUtils.ceiling(minusMinute(1), field)

/**
 * 四舍五入
 *
 * 比如
 * - 19:05:15 -> 19:05:00
 * - 19:05:30 -> 19:06:00
 *
 * @param field  like Calendar.MINUTE
 */
fun Date.round(field: Int) =
        DateUtils.round(this, field)
