package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.java.newIInstance
import com.monkeydp.tools.ext.java.newIInstanceX
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/7/17
 */
// ==== new interface instance ====

fun KClass<*>.newIInstance(vararg args: Any, constructName: String = "invoke"): Any =
        java.newIInstance(*args, constructName = constructName)

fun <T : Any> KClass<T>.newIInstanceX(vararg args: Any, constructName: String = "invoke"): T =
        java.newIInstanceX(*args, constructName = constructName)