package com.monkeydp.tools.util

/**
 * @author iPotato
 * @date 2019/10/14
 */
object TypeUtil {
    /**
     * Returns an array of the given object types
     */
    fun types(vararg objects: Any): Array<Class<*>> {
        if (objects.isEmpty())
            return emptyArray()

        val length = objects.size
        return Array<Class<*>>(length) { index ->
            objects[index].javaClass
        }
    }
}