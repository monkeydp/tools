package com.monkeydp.tools.ext

/**
 * @author iPotato
 * @date 2019/11/9
 */

/**
 *  If match once, return matched element
 *  else throw ex
 */
inline fun <T> Iterable<T>.matchOne(predicate: (T) -> Boolean): T {
    val matched = filter(predicate)
    if (matched.size != 1)
        ierror(
                """ Element is matched ${matched.size} times, not once!
                    Following elements are matched:
                    $this
                """
        )
    return matched.first()
}