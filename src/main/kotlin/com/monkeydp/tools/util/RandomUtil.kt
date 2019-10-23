package com.monkeydp.tools.util

import org.jetbrains.annotations.TestOnly
import java.util.*

/**
 * @author iPotato
 * @date 2019/10/18
 */
object RandomUtil {

    private val random = Random()

    @TestOnly
    fun randomId(min: Int = 1, max: Int = 1000): Long = randomInt(min, max).toLong()

    /**
     * Random int between min (inclusive) and max (inclusive)
     */
    fun randomInt(min: Int, max: Int): Int = random.nextInt(max - min + 1) + min

    /**
     * Random int between zero (inclusive) and max (exclusive)
     */
    fun nextInt(max: Int) = random.nextInt(max)
}