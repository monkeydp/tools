package com.monkeydp.tools.util

import com.monkeydp.tools.ext.kotlin.toDays
import com.monkeydp.tools.ext.kotlin.toSeconds
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * @author iPotato-Work
 * @date 2021/2/19
 */
object Countdown {
    /**
     * @return 格式化后的剩余时间. 格式 - dd HH:mm:ss
     */
    fun format(time: Long, unit: TimeUnit = MILLISECONDS): String {
        val secondsTotal = time.toSeconds(unit)
        val days = time.toDays(unit)
        val hours = secondsTotal / (60 * 60)
        val minutes = (secondsTotal / 60) % 60
        val seconds = secondsTotal % 60
        return String.format("%d %02d:%02d:%02d", days, hours, minutes, seconds)
    }
}
