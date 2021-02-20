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
    fun format(time: Long, unit: TimeUnit = MILLISECONDS) =
            time.toSeconds(unit).let {
                LeftTime(
                        days = time.toDays(unit),
                        hours = it / (60 * 60) % 24,
                        minutes = (it / 60) % 60,
                        seconds = it % 60,
                )
            }
}

class LeftTime(
        val days: Long,
        val hours: Long,
        val minutes: Long,
        val seconds: Long,
) {
    /**
     * 格式 - dd HH:mm:ss
     */
    override fun toString() =
            String.format("%d %02d:%02d:%02d", days, hours, minutes, seconds)
}
