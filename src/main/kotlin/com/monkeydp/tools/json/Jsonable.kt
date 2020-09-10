package com.monkeydp.tools.json

import com.monkeydp.tools.ext.jackson.toJson
import com.monkeydp.tools.ext.jackson.toObject

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

    private fun Any.toJsonX() =
            toJson()
}

inline fun <reified T : Jsonable> Jsonable.fromJson(json: String): T =
        json.toObject()

