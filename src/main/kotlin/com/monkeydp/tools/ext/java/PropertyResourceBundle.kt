package com.monkeydp.tools.ext.java

import com.monkeydp.tools.ext.kotlin.changeCharset
import com.monkeydp.tools.ext.kotlin.getFieldValue
import com.monkeydp.tools.global.defaultCharset
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.ISO_8859_1
import java.util.*

fun PropertyResourceBundle.getLookup(charset: Charset = defaultCharset) =
        getFieldValue<Map<String, String>>("lookup") { forceAccess = true }
                .map { it.key to it.value.changeCharset(ISO_8859_1, charset) }
                .toMap()