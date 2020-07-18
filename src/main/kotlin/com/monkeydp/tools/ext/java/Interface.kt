package com.monkeydp.tools.ext.java

import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.getMethodOrNull
import com.monkeydp.tools.ext.kotlin.invoke
import kotlin.reflect.full.companionObjectInstance

/**
 * @author iPotato-Work
 * @date 2020/7/17
 */
// ==== new interface instance ====

fun Class<*>.newIInstance(vararg args: Any, constructName: String = "invoke"): Any {
    checkIsInterface()
    return newIInstanceX(*args, constructName = constructName)
}

fun <T : Any> Class<T>.newIInstanceX(vararg args: Any, constructName: String = "invoke"): T {
    checkIsInterface()
    kotlin.companionObjectInstance.also {
        if (it != null) {
            val invokeMethod = it::class.getMethodOrNull(constructName, *args)
            if (invokeMethod != null)
                return it.invoke(invokeMethod, *args)
        }
    }
    val paramsFormat =
            args.mapIndexed { index, it -> "$${index.plus(1)}: ${it.javaClass}" }
                    .joinToString(", ")
    val argsFormat = args.mapIndexed { index, it -> "$${index.plus(1)} = $it" }
            .joinToString(", ")
    ierror("Cannot construct interface $this. It must have `fun $constructName($paramsFormat): ${this.simpleName}` in companion object! Arguments: $argsFormat")
}

fun Class<*>.checkIsInterface() {
    if (!isInterface) ierror("$this is not interface!")
}