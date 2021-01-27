package com.monkeydp.tools.ext.java.time

import java.time.ZonedDateTime
import java.util.*

/**
 * @author iPotato-Work
 * @date 2021/1/27
 */
fun ZonedDateTime.toDate() =
        Date.from(toInstant())
