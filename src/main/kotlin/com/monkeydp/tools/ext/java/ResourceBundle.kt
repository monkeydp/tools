package com.monkeydp.tools.ext.java

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.ISO_8859_1
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

/**
 * @author iPotato-Work
 * @date 2020/5/13
 */
fun ResourceBundle.getStringX(key: String, charset: Charset = UTF_8) =
        String(getString(key).toByteArray(ISO_8859_1), charset)