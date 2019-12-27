package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.ext.main.ierror
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaType

/**
 * @author iPotato
 * @date 2019/11/9
 */
// TODO Need test
fun KProperty<*>.getFirstUpperBound(): KClass<*> {
    val returnJavaType = returnType.javaType as ParameterizedTypeImpl
    val wildcardType = returnJavaType.actualTypeArguments[0] as WildcardTypeImpl
    return when (val type = wildcardType.upperBounds[0]) {
        is ParameterizedTypeImpl -> type.rawType!!.kotlin
        is Class<*> -> type.kotlin
        else -> ierror("Unknown type $type!")
    }
}

fun KProperty<*>.getValueKClass(): KClass<*> =
        when (val type = returnType.javaType) {
            is Class<*> -> type
            is ParameterizedTypeImpl -> type.rawType
            else -> ierror("Unsupported type: $type")
        }.kotlin