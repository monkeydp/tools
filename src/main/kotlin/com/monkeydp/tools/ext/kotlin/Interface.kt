package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.exception.ierror
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

/**
 * @author iPotato-Work
 * @date 2020/7/17
 */
fun KClass<*>.newIInstance(vararg args: Any, constructName: String = "invoke"): Any {
    checkIsInterface()
    return newIInstanceX(*args, constructName = constructName)
}

fun <T : Any> KClass<T>.newIInstanceX(vararg args: Any, constructName: String = "invoke"): T {
    checkIsInterface()
    companionObjectInstance.also {
        if (it != null) {
            val invokeMethod = it::class.getMethodOrNull("invoke", *args)
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

fun KClass<*>.checkIsInterface() {
    if (!java.isInterface) ierror("$this is not interface!")
}