package com.monkeydp.tools.ext.kotlin

import com.monkeydp.tools.exception.ierror
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * @author iPotato-Work
 * @date 2020/5/18
 */

/**
 * Change annotation attributes
 * @param path example: path.to.attrMap
 */
fun <T : Annotation, R : Any> T.changeAttrs(vararg pairs: Pair<String, R>, path: String = "") {
    if (this !is Proxy) throw RuntimeException("Require proxy of annotation!")
    val h: InvocationHandler = getFieldValue("h") { forceAccess = true }
    val realPath =
            if (h.javaClass.name == "sun.reflect.annotation.AnnotationInvocationHandler")
                "memberValues"
            else path
    if (realPath.isBlank()) ierror("Real path must not be empty!")

    val attrMap: Map<String, Any> = h.getFieldValueByPath(realPath) { forceAccess = true }
    val attrMutableMap = attrMap.toMutableMap()
    pairs.forEach {
        val (name, propertyValue) = it
        attrMutableMap[name] = propertyValue
    }
    h.setFieldValueByPath(realPath, attrMutableMap) { forceAccess = true }
}