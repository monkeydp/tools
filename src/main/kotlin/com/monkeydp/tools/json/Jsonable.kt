package com.monkeydp.tools.json

import com.monkeydp.tools.ext.kotlin.toJson

/**
 * @author iPotato-Work
 * @date 2020/8/10
 */
interface Jsonable {
    fun toJson(): String
}

abstract class BaseJsonable : Jsonable {
    override fun toJson() =
            toJsonX()
}

private fun Any.toJsonX() =
        toJson()
