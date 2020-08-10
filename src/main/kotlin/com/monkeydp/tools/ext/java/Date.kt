package com.monkeydp.tools.ext.java

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author iPotato-Work
 * @date 2020/8/10
 */
val Date.HHmmss
    get() = SimpleDateFormat("HH:mm:ss").format(this)

val Date.yyyyMMddHHmmss
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
