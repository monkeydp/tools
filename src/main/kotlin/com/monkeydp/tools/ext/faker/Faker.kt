package com.moonlight.iot.business.fake

import com.github.javafaker.DateAndTime
import java.util.concurrent.TimeUnit

/**
 * @author iPotato-Work
 * @date 2020/5/14
 */
fun DateAndTime.random(atMost: Int = 10, timeUnit: TimeUnit = TimeUnit.DAYS) = future(atMost, timeUnit)