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
     * Random character and number
     */
    fun randomAlphanumeric(length: Int): String =
            RandomStringUtils.randomAlphanumeric(length)
}