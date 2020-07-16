package com.moonlight.iot.business.fake

import com.github.javafaker.DateAndTime
import com.github.javafaker.Internet
import com.monkeydp.tools.constant.Symbol.COLON
import java.util.concurrent.TimeUnit

/**
 * @author iPotato-Work
 * @date 2020/5/14
 */
fun DateAndTime.random(atMost: Int = 10, timeUnit: TimeUnit = TimeUnit.DAYS) = future(atMost, timeUnit)

/**
 * Without `:`
 */
fun Internet.macAddressSimplify() =
        macAddress().split(COLON).joinToString("")