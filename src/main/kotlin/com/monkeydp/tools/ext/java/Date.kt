package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

/**
 * @author iPotato-Work
 * @date 2020/8/10
 */
val Date.HHmmss
    get() = SimpleDateFormat("HH:mm:ss").format(this)

val Date.yyyyMMddHHmmss
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)

fun Date.pretty(elsePattern: String = "yyyy-MM-dd HH:mm:ss"): String {
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
        else -> SimpleDateFormat(elsePattern).format(this)
    }
}
