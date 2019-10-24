package com.monkeydp.tools.util

import org.apache.commons.lang3.reflect.ConstructorUtils
import java.lang.reflect.Constructor

/**
 * @author iPotato
 * @date 2019/10/14
 */
object ClassUtil {

    /**
     * Return matched constructor of the given class
     */
    fun <T> getMatchingAccessibleConstructor(clazz: Class<T>, vararg argTypes: Class<*>): Constructor<T> {
        return ConstructorUtils.getMatchingAccessibleConstructor(clazz, *argTypes)
    }

    /**
     * Return new instance of the given class
     *
     * @param clazz
     * @param args
     * @param <T>
     * @return
    </T> */
    fun <T> newInstance(clazz: Class<T>, vararg args: Any): T {
        val argClasses = TypeUtil.getTypes(*args)
        return getMatchingAccessibleConstructor<T>(clazz, *argClasses)
                .newInstance(*args)
    }
}