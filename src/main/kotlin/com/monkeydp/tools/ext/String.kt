package com.monkeydp.tools.ext

import com.monkeydp.tools.enumeration.Symbol

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
fun String.toCamelCase(capitalize: Boolean? = null): String {
    return this.split(Symbol.UNDERSCORE).mapIndexed { index, str ->
        if (index == 0)
            when (capitalize) {
                null  -> {
                    val first = str[0].toString()
                    str.toLowerCase().replaceAt(0, first)
                }
                true  -> str.toLowerCase().capitalize()
                false -> str.toLowerCase()
            }
        else str.toLowerCase().capitalize()
    }.joinToString("")
}

fun String.toSnakeCase(): String {
    var builder = StringBuilder()
    var isFirst = true
    this.forEach {
        if (it.isUpperCase()) {
            if (isFirst) isFirst = false
            else builder.append("_")
            builder.append(it.toLowerCase())
        } else builder.append(it)
    }
    return builder.toString()
}

fun String.camelCase2List(): List<String> {
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

fun String.toStdPath() = this.replace(Symbol.BACKSLASH, Symbol.SLASH)

fun String.removeExtension() = replaceFirst("[.][^.]+$".toRegex(), "")