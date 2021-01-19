package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.constant.Symbol.BACKSLASH
import com.monkeydp.tools.constant.Symbol.SLASH

/**
 * @author iPotato-Work
 * @date 2020/10/13
 */
fun String.toNormalizePath() =
        replace("/+".toRegex(), "/")

fun String.toStdPath() =
        replace(BACKSLASH, SLASH)
                .toNormalizePath()
