package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.YEAR
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
