package com.monkeydp.tools.ext.cronutils.model

import com.cronutils.model.Cron
import com.cronutils.model.time.ExecutionTime
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.append
import com.monkeydp.tools.ext.kotlin.diff
import com.monkeydp.tools.ext.kotlin.findById
import com.monkeydp.tools.ext.kotlin.integerable
import com.monkeydp.tools.module.enumx.Enumx
import java.time.ZonedDateTime

/**
 * @author iPotato-Work
 * @date 2021/1/27
 */
fun Cron.nextExecuteTime(datetime: ZonedDateTime = ZonedDateTime.now()) =
        ExecutionTime.forCron(this)
                .nextExecution(datetime)
                .get()
