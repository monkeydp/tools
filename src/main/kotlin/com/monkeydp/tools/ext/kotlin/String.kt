package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.constant.Symbol.BACKSLASH
import com.monkeydp.tools.constant.Symbol.SLASH
import com.monkeydp.tools.constant.Symbol.SPACE
import com.monkeydp.tools.constant.Symbol.UNDERSCORE

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun String.replaceAt(index: Int, replacement: CharSequence) = this.replaceRange(index, index + 1, replacement)

fun String.toUpperCamelCase() = toCamelCase(true)

fun String.toLowerCamelCase() = toCamelCase(false)

/**
 * @param capitalize
 * null -> do nothing
 * true -> upper camel case
 * false -> lower camel case
 */
fun String.toCamelCase(capitalize: Boolean? = null) =
        this.split(UNDERSCORE)
                .map { it.camelCase2List() }
                .flatten()
                .mapIndexed { index, str ->
                    if (index == 0)
                        when (capitalize) {
                            null -> {
                                val first = str[0].toString()
                                str.toLowerCase().replaceAt(0, first)
                            }
                            true -> str.toLowerCase().capitalize()
                            false -> str.toLowerCase()
                        }
                    else str.toLowerCase().capitalize()
                }.joinToString("")

fun String.toSnakeCase(): String {
    var builder = StringBuilder()
    var isFirst = true
    this.forEach {
        if (it.isUpperCase()) {
            if (isFirst) isFirst = false
            else builder.append(UNDERSCORE)
            builder.append(it.toLowerCase())
        } else builder.append(it)
    }
    return builder.toString()
}

fun String.camelCase2List(): List<String> {
    
    if (isAllUpperCase()) return listOf(this)
    
    val list = mutableListOf<String>()
    val builder = StringBuilder()
    this.forEach {
        if (it.isUpperCase()) {
            list.add(builder.toString())
            builder.clear()
        }
        builder.append(it)
    }
    list.add(builder.toString())
    if (list.first().isEmpty()) list.removeFirst()
    return list.toList()
}

fun String.isAllUpperCase(): Boolean {
    var bool = true
    forEach {
        if (!it.isUpperCase()) {
            bool = false
            return@forEach
        }
    }
    return bool
}

fun String.camelCaseFirst() = camelCase2List().first()

fun String.toStdPath() = this.replace(BACKSLASH, SLASH)

fun String.removeExtension() = replaceFirst("[.][^.]+$".toRegex(), "")

fun String.firstOfSnackCase() = this.split(UNDERSCORE).first()

fun String.camelCaseSeparated(capitalizeEveryWord: Boolean = false, symbol: CharSequence = SPACE): String {
    var strings = camelCase2List()
    if (capitalizeEveryWord) strings = strings.map { it.capitalize() }.toList()
    return strings.joinToString(symbol)
}
