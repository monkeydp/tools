package com.monkeydp.tools.ext.java

import com.monkeydp.tools.ext.kotlin.changeCharset
import com.monkeydp.tools.global.defaultCharset
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.ISO_8859_1
import java.util.*

/**
 * @author iPotato-Work
 * @date 2020/5/13
 */
fun ResourceBundle.getMessage(key: String, charset: Charset = defaultCharset) =
        getString(key).changeCharset(ISO_8859_1, charset)

fun ResourceBundle.getMessageOrNull(key: String, charset: Charset = defaultCharset): String? =
        getMessage(key, charset)