package com.monkeydp.tools.ext.java.util

import java.util.*
import java.util.Calendar.*

/**
 * @author iPotato-Work
 * @date 2021/1/27
 */
fun Date.isSameDay(another: Date): Boolean {
    val cal1 = getInstance()
    val cal2 = getInstance()
    cal1.time = this
    cal2.time = another
    return cal1[DAY_OF_YEAR] == cal2[DAY_OF_YEAR]
            && cal1[YEAR] == cal2[YEAR]
}

val Date.zeroPoint: Date
    get() {
        val cal = getInstance()
        cal.time = this
        cal.set(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH), 0, 0, 0)
        return cal.time
    }
