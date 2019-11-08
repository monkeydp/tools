package com.monkeydp.tools.ext

import com.monkeydp.tools.util.TypeUtil
import org.apache.commons.lang3.reflect.ConstructorUtils

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun Class<*>.singletonInstance() =
        fields.first { it.name == "INSTANCE" }.get(this)

inline fun <reified C> Class<out C>.singletonInstanceX() =
        fields.first { it.name == "INSTANCE" }.get(this) as C

fun <T> Class<T>.newInstanceX(vararg args: Any): T {
    val argClasses = TypeUtil.getTypes(*args)
    return ConstructorUtils.getMatchingAccessibleConstructor(this, *argClasses)
            .newInstance(*args)
}