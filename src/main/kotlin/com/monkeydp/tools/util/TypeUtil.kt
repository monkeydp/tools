package com.monkeydp.tools.util

import com.monkeydp.tools.exception.ierror
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author iPotato
 * @date 2019/10/14
 */
@Suppress("UNCHECKED_CAST")
object TypeUtil {

    /**
     * Get class of generic type in the Any
     *
     * @param object
     * @param index
     * @return
     */
    fun <T : Type> getGenericType(any: Any, index: Int = 0): T =
            getGenericType(any.javaClass, index)

    /**
     * Get class of generic type in the Class<*>
     *
     * @param clazz
     * @param index
     * @return
     */
    fun <T : Type> getGenericType(clazz: Class<*>, index: Int = 0): T {
        val genericSuperclass = clazz.genericSuperclass
        val isParameterizedType = genericSuperclass is ParameterizedType

        if (!isParameterizedType) ierror("ParameterizedType not found in class $clazz")

        val parameterizedType = genericSuperclass as ParameterizedType
        return getGenericType(parameterizedType, index)
    }

    /**
     * Get class of generic type in ParameterizedType
     */
    fun <T : Type> getGenericType(parameterizedType: ParameterizedType, index: Int = 0): T =
            parameterizedType.actualTypeArguments[index] as T
}
