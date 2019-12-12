package com.monkeydp.tools.config

import com.monkeydp.tools.config.DevMode.DEBUG
import com.monkeydp.tools.config.DevMode.NORMAL
import com.monkeydp.tools.ext.kotlin.lines
import org.slf4j.Logger

/**
 * @author iPotato
 * @date 2019/12/2
 */
enum class DevMode {
    DEBUG,
    NORMAL;
}

var devModel = NORMAL

fun enableDebugMode() {
    devModel = DEBUG
}

fun Logger.debugMode(e: Throwable): Unit =
        debug("<DEBUG MODE> ${e.message}${System.lineSeparator()}${e.stackTrace.toList().lines()}}")