package com.monkeydp.tools.util

import com.monkeydp.tools.exception.ierror

/**
 * @author iPotato-Work
 * @date 2020/9/17
 */
object MacUtil {
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
