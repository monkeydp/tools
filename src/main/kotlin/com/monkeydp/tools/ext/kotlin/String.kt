package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.constant.Symbol.SPACE
import com.monkeydp.tools.constant.Symbol.UNDERSCORE
import java.net.URL
import java.nio.charset.Charset

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun String.replaceAt(index: Int, replacement: CharSequence) =
        this.replaceRange(index, index + 1, replacement)

/**
 * Starts with any prefix in list
 */
fun String.startsWithIn(vararg prefixes: String, ignoreCase: Boolean = false): Boolean {
    prefixes.forEach {
        if (startsWith(it, ignoreCase))
            return true
    }
    return false
}

fun String.snakeToUpperCamel() = snakeToCamel(true)

fun String.snakeToLowerCamel() = snakeToCamel(false)

/**
 * @param capitalize
 * null -> do nothing
 * true -> upper camel case
 * false -> lower camel case
 */
fun String.snakeToCamel(capitalize: Boolean? = null) =
        this.split(UNDERSCORE)
                .map { it.camelToList() }
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

fun String.camelToSnake(): String =
        StringBuilder()
                .also { builder ->
                    forEachIndexed { index, it ->
                        if (it.isUpperCase() && index != 0)
                            builder.append(UNDERSCORE)
                                    .append(it.toLowerCase())
                        else builder.append(it)
                    }
                }.toString()

fun String.snakeToChain(joiner: String): String = split(UNDERSCORE).joinToString(joiner)

fun String.camelToMutableList(): MutableList<String> {

    if (isAllUpperCase()) return mutableListOf(this)

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
    return list
}

fun String.camelToList(): List<String> =
        camelToMutableList().toList()

fun String.camelToChain(joiner: String) = camelToList().joinToString(joiner)

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

fun String.removeExtension() = replaceFirst("[.][^.]+$".toRegex(), "")

fun String.firstOfSnack() = this.split(UNDERSCORE).first()

fun String.camelSeparated(
        capitalizeEveryWord: Boolean = false,
        symbol: CharSequence = SPACE
): String {
    var strings = camelToList()
    if (capitalizeEveryWord) strings = strings.map { it.capitalize() }.toList()
    return strings.joinToString(symbol)
}


// ==== Resource ====

/**
 * Example:
 *  src/main/resources/my.txt
 *  "/my.txt".asResource()
 */
fun String.asResource(): URL = asResourceOrNull()!!

fun String.asResourceOrNull(): URL? = object {}.javaClass.getResource(this)


// ==== Wrap ====

fun String.unwrapFromCurlyBraces() =
        removePrefix("{").removeSuffix("}")

fun String.wrapInCurlyBraces() =
        "{$this}"


// ==== Charset ====

fun String.changeCharset(from: Charset, to: Charset) =
        String(this.toByteArray(from), to)


// ==== Radix ====

fun String.radixPlus(i: Int, radix: Int = 10) =
        toLong(radix = radix).plus(i).toString(radix = radix)

fun String.hexPlus(i: Int) =
        radixPlus(i = i, radix = 16)


// ==== Number ====

fun String.integerable() =
        toString().toIntOrNull() != null
