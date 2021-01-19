package com.monkeydp.tools.url

import java.net.URI

/**
 * @author iPotato-Work
 * @date 2021/1/19
 */
fun String.toNormalizeUrl() =
        URI(this).normalize().toString()

fun String.toStdUrl() =
        toNormalizeUrl()
