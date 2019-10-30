package com.monkeydp.tools.ext

/**
 * @author iPotato
 * @date 2019/10/30
 */
fun Class<*>.singletonInstance() =
        fields.first { it.name == "INSTANCE" }.get(this)

inline fun <reified C> Class<C>.singletonInstanceX() =
        fields.first { it.name == "INSTANCE" }.get(this) as C