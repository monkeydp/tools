package com.monkeydp.tools.ext.temporal

import com.monkeydp.tools.exception.ierror
import java.time.temporal.ChronoUnit.*
import java.util.concurrent.TimeUnit


/**
 * @author iPotato-Work
 * @date 2020/9/9
 */
fun TimeUnit.toChronoUnit() =
        when (this) {
            TimeUnit.DAYS -> DAYS
            TimeUnit.HOURS -> HOURS
            TimeUnit.MINUTES -> MINUTES
            TimeUnit.SECONDS -> SECONDS
            TimeUnit.MICROSECONDS -> MICROS
            TimeUnit.MILLISECONDS -> MILLIS
            TimeUnit.NANOSECONDS -> NANOS
            else -> ierror("There are no ChronoUnit correspond to TimeUnit $this!")
        }
