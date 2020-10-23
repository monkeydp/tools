package com.monkeydp.tools.ext.java.awt

import java.awt.Color

/**
 * @author iPotato-Work
 * @date 2020/10/23
 */
val Color.hex
    get() = "${red.toString(16)}${green.toString(16)}${blue.toString(16)}"
