package com.monkeydp.tools.ext.kotlin

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import kotlin.reflect.KProperty1

/**
 * @author iPotato-Work
 * @date 2020/5/18
 */
/**
 * Change annotation attributes
 */
fun <T : Annotation, R : Any> T.changeAttrs(pair: Pair<KProperty1<T, R>, R>) {
    if (this !is Proxy) throw RuntimeException("Require proxy of annotation!")
    val (kProperty1, propertyValue) = pair
    val h: InvocationHandler = this.getFieldValue("h") { forceAccess = true }
    val memberValues: MutableMap<String, Any> = h.getFieldValue("memberValues") { forceAccess = true }
    memberValues[kProperty1.name] = propertyValue
}