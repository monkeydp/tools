package com.monkeydp.tools.ext

import com.monkeydp.tools.ext.DevMode.DEBUG
import com.monkeydp.tools.ext.DevMode.NORMAL
import org.slf4j.Logger

/**
 * @author iPotato
 * @date 2019/12/2
 */
enum class DevMode {
    DEBUG,
    NORMAL
}

private var devModel = NORMAL

fun enableDebugMode() {
    devModel = DEBUG
}

fun enableNormalMode() {
    devModel = NORMAL
}

fun isDebugMode() = devModel == DEBUG

fun isNormalMode() = devModel == NORMAL

fun Logger.debugMode(msg: String): Unit = debug("<DEBUG MODE> $msg")