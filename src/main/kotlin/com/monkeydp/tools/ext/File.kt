package com.monkeydp.tools.ext

import com.monkeydp.tools.enumeration.Symbol
import java.io.File

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun File.getClassname(prefix: String = ""): String {
    return path.toStdPath()
            .removePrefix(prefix.toStdPath())
            .removeExtension()
            .replace(Symbol.SLASH, Symbol.DOT)
            .removePrefix(Symbol.DOT)
}

fun mockFile() = File("[MOCK FILE]")