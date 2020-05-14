package com.monkeydp.tools.util

import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author iPotato
 * @date 2019/10/18
 */
object RandomUtil {

    private val random = Random()

    /**
     * Random int between min (inclusive) and max (inclusive)
     */
    fun randomInt(min: Int, max: Int): Int = random.nextInt(max - min + 1) + min

    /**
     * Random int between zero (inclusive) and max (exclusive)
     */
    fun nextInt(max: Int) = random.nextInt(max)

    /**
     * Random character and number
     */
    fun randomAlphanumeric(length: Int) = RandomStringUtils.randomAlphanumeric(length)
}