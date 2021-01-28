package com.monkeydp.tools.module.cron

import com.monkeydp.tools.ext.java.yearx
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author iPotato-Work
 * @date 2021/1/28
 */
fun Date.cronExp(options: (CronExpOptions.() -> Unit)? = null) =
        cronExp(CronExpOptions(options))

fun Date.cronExp(options: CronExpOptions) =
        options.run {
            val year = when (year) {
                null -> null
                "" -> this@cronExp.yearx
                else -> year
            }
            SimpleDateFormat("ss mm HH dd MM $week ${year ?: ""}".trim())
                    .format(this@cronExp)
        }

class CronExpOptions(init: (CronExpOptions.() -> Unit)? = null) {

    var week: String = "?"

    /**
     * empty string means current year
     */
    var year: String? = null

    init {
        init?.invoke(this)
    }
}
