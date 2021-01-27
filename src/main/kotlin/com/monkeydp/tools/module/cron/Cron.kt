package com.monkeydp.tools.module.cron

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.java.yearx
import com.monkeydp.tools.ext.java.yyyyMMddHHmmssFormat
import com.monkeydp.tools.ext.kotlin.hoursToMillis
import com.monkeydp.tools.ext.kotlin.integerable
import com.monkeydp.tools.ext.kotlin.minutesToMillis
import com.monkeydp.tools.ext.kotlin.secondsToMillis
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author iPotato-Work
 * @date 2021/1/25
 */
interface Cron {
    val exp: CharSequence

    val second: CharSequence
    val minute: CharSequence
    val hour: CharSequence
    val day: CharSequence
    val month: CharSequence
    val week: CharSequence
    val year: CharSequence
    val yearOrNull: CharSequence?

    val date: Date
    val dateOrNull: Date?
    val time: Time
    val timeOrNull: Time?

    fun plusSeconds(seconds: Long): Cron
    fun minusSeconds(seconds: Long): Cron
    fun plusMinutes(minutes: Long): Cron
    fun minusMinutes(minutes: Long): Cron
    fun plusHours(hours: Long): Cron
    fun minusHours(hours: Long): Cron

    companion object {
        operator fun invoke(expression: CharSequence): Cron =
                CronImpl(expression)
    }
}

/**
 * @param datetimePart one of datetime like minute
 */
class CronImpl(
        expression: CharSequence
) : Cron {
    override val exp = expression.trim()
    private val expsplit = exp.split(" ")

    override val second = expsplit[0]
    override val minute = expsplit[1]
    override val hour = expsplit[2]
    override val day = expsplit[3]
    override val month = expsplit[4]
    override val week = expsplit[5]
    override val year get() = yearOrNull ?: throw NullPointerException()
    override val yearOrNull = expsplit.getOrNull(6)

    private val datePattern = if (yearOrNull == null) "${Date().yearx}-$month-$day" else "$yearOrNull-$month-$day"
    private val timePattern = "$hour:$minute:$second"
    private val datetimePattern = "$datePattern $timePattern"

    override val date
        get() = dateOrNull ?: ierror("Cron expression `$exp` cannot convert to date!")

    override val dateOrNull = run {
        val datetimeParts = mutableListOf(second, minute, hour, day, month)
        if (yearOrNull != null)
            datetimeParts.add(yearOrNull)
        return@run if (datetimeParts.any { !it.integerable() })
            null
        else yyyyMMddHHmmssFormat.parse(datetimePattern)
    }

    override val time
        get() = timeOrNull ?: ierror("Cron expression `$exp` cannot convert to time!")

    override val timeOrNull = run {
        val datetimeParts = mutableListOf(second, minute, hour)
        return@run if (datetimeParts.any { !it.integerable() })
            null
        else Time.valueOf(timePattern)
    }

    private fun plusMillSeconds(millSeconds: Long): Cron {
        val date = Date(date.time + millSeconds)
        val expression = date.cronExp {
            week = this@CronImpl.week
            if (listOf(null, "*").contains(this@CronImpl.yearOrNull))
                year = this@CronImpl.yearOrNull
        }
        return Cron(expression)
    }

    override fun plusSeconds(seconds: Long) =
            plusMillSeconds(seconds.secondsToMillis())

    override fun minusSeconds(seconds: Long) =
            plusSeconds(-seconds)

    override fun plusMinutes(minutes: Long) =
            plusMillSeconds(minutes.minutesToMillis())

    override fun minusMinutes(minutes: Long) =
            plusMinutes(-minutes)

    override fun plusHours(hours: Long) =
            plusMillSeconds(hours.hoursToMillis())

    override fun minusHours(hours: Long) =
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
