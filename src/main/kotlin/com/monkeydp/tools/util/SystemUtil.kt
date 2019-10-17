package com.monkeydp.tools.util

import org.apache.commons.lang3.SystemUtils

/**
 * @author iPotato
 * @date 2019/10/17
 */
object SystemUtil {
    val OS_NAME = SystemUtils.OS_NAME

    val IS_OS_WINDOWS = SystemUtils.IS_OS_WINDOWS
    val IS_OS_UNIX = SystemUtils.IS_OS_UNIX
}