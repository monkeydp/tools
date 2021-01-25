package com.monkeydp.tools.module.cron

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.java.MMddHHmmssFormat
import com.monkeydp.tools.ext.java.yearx
import com.monkeydp.tools.ext.java.yyyyMMddHHmmssFormat
import com.monkeydp.tools.ext.kotlin.hoursToMillis
import com.monkeydp.tools.ext.kotlin.integerable
import com.monkeydp.tools.ext.kotlin.minutesToMillis
import com.monkeydp.tools.ext.kotlin.secondsToMillis
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author iPotato-Work
 * @date 2021/1/25
 */
interface Cron {
    val expression: CharSequence

    val second: CharSequence
    val minute: CharSequence
    val hour: CharSequence
    val day: CharSequence
    val month: CharSequence
    val week: CharSequence
    val year: CharSequence?

    val date: Date

    fun plusSeconds(seconds: Int): Cron
    fun minusSeconds(seconds: Int): Cron
    fun plusMinutes(minutes: Int): Cron
    fun minusMinutes(minutes: Int): Cron
    fun plusHours(hours: Int): Cron
    fun minusHours(hours: Int): Cron

    companion object {
        operator fun invoke(expression: CharSequence): Cron =
                CronImpl(expression)
    }
}

/**
 * @param datetimePart one of datetime like minute
 */
class CronImpl(
        override val expression: CharSequence
) : Cron {
    private val expsplit = expression.split(" ")

    override val second = expsplit[0]
    override val minute = expsplit[1]
    override val hour = expsplit[2]
    override val day = expsplit[3]
    override val month = expsplit[4]
    override val week = expsplit[5]
    override val year = expsplit.getOrNull(6)

    override val date
        get() = dateOrNull ?: ierror("Cron expression `$expression` cannot convert to date!")

    private val datePattern =
            if (year == null)
                "$month-$day $hour:$minute:$second"
            else
                "$year-$month-$day $hour:$minute:$second"

    private val dateFormat =
            if (year == null)
                MMddHHmmssFormat
            else yyyyMMddHHmmssFormat

    private val dateOrNull = run {
        val datetimeParts = mutableListOf(second, minute, hour, day, month)
        if (year != null)
            datetimeParts.add(year)
        return@run if (datetimeParts.any { !toString().integerable() })
            null
        else dateFormat.parse(datePattern)
    }

    private fun plusMillSeconds(millSeconds: Long): Cron {
        val date = Date(date.time + millSeconds)
        val expression = date.cronExp {
            week = this@CronImpl.week
            if (listOf(null, "*").contains(this@CronImpl.year))
                year = this@CronImpl.year
        }
        return Cron(expression)
    }

    override fun plusSeconds(seconds: Int) =
            plusMillSeconds(seconds.secondsToMillis())

    override fun minusSeconds(seconds: Int) =
            plusSeconds(-seconds)

    override fun plusMinutes(minutes: Int) =
            plusMillSeconds(minutes.minutesToMillis())

    override fun minusMinutes(minutes: Int) =
            plusMinutes(-minutes)

    override fun plusHours(hours: Int) =
            plusMillSeconds(hours.hoursToMillis())

    override fun minusHours(hours: Int) =
            plusHours(-hours)
}

fun Date.cronExp(options: (CronExpOptions.() -> Unit)? = null) =
        cronExp(CronExpOptions(options))

fun Date.cronExp(options: CronExpOptions) =
        options.run {
            val year = when (year) {
                null -> null
                "" -> this@cronExp.yearx
                else -> year
            }
            SimpleDateFormat("ss mm HH dd MM $week $year".trim())
                    .format(this)
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
