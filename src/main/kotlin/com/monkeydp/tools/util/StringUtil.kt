package com.monkeydp.tools.util

import org.apache.commons.lang3.StringUtils


/**
 * @author iPotato
 * @date 2019/10/4
 */
object StringUtil {
    /**
     * Whether the string is empty
     */
    fun isEmpty(string: String?) =
            StringUtils.isEmpty(string)
}