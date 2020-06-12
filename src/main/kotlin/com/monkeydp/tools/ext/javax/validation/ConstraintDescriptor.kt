package com.monkeydp.tools.ext.javax.validation

import com.monkeydp.tools.ext.kotlin.getAnnotatedField
import javax.validation.metadata.ConstraintDescriptor

/**
 * @author iPotato-Work
 * @date 2020/6/12
 */
val ConstraintDescriptor<*>.isComposing
    get() = composingConstraints.isNotEmpty()

private val internalAnnotationAttributes: Set<String> =
        setOf("message", "groups", "payload")

fun ConstraintDescriptor<*>.getExposedAttrMap() =
        mutableMapOf<String, Any>().apply {
            attributes.forEach { (attributeName: String, attributeValue: Any) ->
                if (!internalAnnotationAttributes.contains(attributeName)) {
                    this[attributeName] = attributeValue
                }
            }
        }

fun ConstraintDescriptor<*>.isRef(clazz: Class<*>) =
        isComposing &&
                annotation.annotationClass.java.enclosingClass != clazz

val ConstraintDescriptor<*>.refKClass
    get() =
        annotation.annotationClass.java.enclosingClass.kotlin

val ConstraintDescriptor<*>.refField
    get() =
        refKClass.getAnnotatedField(annotation.annotationClass)