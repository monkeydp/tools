package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.util.RandomUtil

/**
 * @author iPotato-Work
 * @date 2020/5/7
 */
fun Int.random(min: Int = 1) = RandomUtil.randomInt(min, this)