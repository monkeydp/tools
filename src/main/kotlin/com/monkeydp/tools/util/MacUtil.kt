package com.monkeydp.tools.util

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.matches

/**
 * @author iPotato-Work
 * @date 2020/9/17
 */
object MacUtil {

    fun isMac(cs: CharSequence) =
            cs.length == 12 &&
                    cs matches "^([0-9A-Fa-f]{2}){6}$"

    /**
     * 批量生成唯一的 mac
     */
    fun batchGenerate(number: Int, configInit: (BatchGenMacConfig.() -> Unit)? = null): Set<String> {
        BatchGenMacConfig().apply {
            configInit?.invoke(this)
            var currentMac = initMac.toLong(radix = 16)
            val skipLongMacs = skipMacs.map { it.toLong(radix = 16) }
            val macs = mutableSetOf<String>()
            repeat(number) {
                while (skipLongMacs.contains(currentMac))
                    currentMac++
                macs.add(currentMac.toString(radix = 16))
                currentMac++
            }
            return macs.toSet()
        }
    }
}

class BatchGenMacConfig {
    var initMac = "100000000000"
        set(value) {
            if (value.length != 12)
                ierror("初始 MAC `$value` 不合法, 必须是 12 位")
            field = value
        }
    var skipMacs: Iterable<String> = setOf<String>()
}

fun CharSequence.isMac() =
        MacUtil.isMac(this)
