package com.monkeydp.tools.module.cron

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.append
import com.monkeydp.tools.ext.kotlin.diff
import com.monkeydp.tools.ext.kotlin.findById
import com.monkeydp.tools.ext.kotlin.integerable
import com.monkeydp.tools.module.enumx.Enumx

/**
 * @author iPotato-Work
 * @date 2021/1/29
 */
enum class CronWeek(val number: Int, val zh: String) : Enumx<CronWeek> {
    MONDAY(1, "星期一"),
    TUESDAY(2, "星期二"),
    WEDNESDAY(3, "星期三"),
    THURSDAY(4, "星期四"),
    FRIDAY(5, "星期五"),
    SATURDAY(6, "星期六"),
    SUNDAY(7, "星期日");

    companion object {
        private val workingDays = listOf(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)

        /**
         * @param weekexp like `4`, `2,3,7`
         */
        fun parseTimeDesc(weekexp: String): String =
                when {
                    weekexp.contains("*") -> "每天"
                    weekexp.contains("?") -> "不重复"
                    weekexp.integerable() || weekexp.contains(",") -> {
                        val timeDescParts = mutableListOf<String>()
                        weekexp.split(",")
                                .map { weekNumber ->
                                    CronWeek::class.findById(weekNumber.toInt()) { it.number }
                                }
                                .toSet()
                                .also {
                                    if (it.size == 7) return "每天"
                                    if (it.size == 0) ierror("Cannot parse week expression `$weekexp`, there is not any week day!")
                                }
                                .sortedBy { it.number }
                                .let { cronWeeks ->
                                    if (cronWeeks.containsAll(workingDays)) {
                                        timeDescParts.append("工作日")
                                        cronWeeks.diff(workingDays).forEach {
                                            timeDescParts.append(it.zh)
                                        }
                                    } else
                                        cronWeeks.forEach {
                                            timeDescParts.append(it.zh)
                                        }
                                }
                        timeDescParts.joinToString(", ")
                    }
                    else -> "Cannot parse week expression `$weekexp`!"
                }
    }
}
