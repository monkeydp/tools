package com.monkeydp.tools.ext.cronutils.model

import com.cronutils.model.Cron
import com.cronutils.model.time.ExecutionTime
import java.time.ZonedDateTime

/**
 * @author iPotato-Work
 * @date 2021/1/27
 */
fun Cron.nextExecuteTime(datetime: ZonedDateTime = ZonedDateTime.now()) =
        ExecutionTime.forCron(this)
                .nextExecution(datetime)
                .get()
