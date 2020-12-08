package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato-Work
 * @date 2020/12/8
 */
infix fun CharSequence.matches(pattern: String) =
        matches(Regex(pattern))
