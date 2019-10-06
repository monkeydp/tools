package com.monkeydp.common.tools.util

import org.apache.commons.lang3.StringUtils


/**
 * @author iPotato
 * @date 2019/10/4
 */
object StringUtil {
    /**
     * 字符串是否为空
     */
    fun isEmpty(string: String?) =
            StringUtils.isEmpty(string)
}