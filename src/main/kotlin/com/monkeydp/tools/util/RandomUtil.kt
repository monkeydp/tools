package com.monkeydp.tools.util

import java.util.*

/**
 * @author iPotato
 * @date 2019/10/18
 */
object RandomUtil {

    private val random = Random()

    /**
     * Random Integer between min and max
     */
    fun randomInt(min: Int, max: Int): Int {
        return random.nextInt(max - min + 1) + min
    }
}