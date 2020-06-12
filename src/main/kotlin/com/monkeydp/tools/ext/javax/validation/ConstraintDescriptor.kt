package com.monkeydp.tools.ext.javax.validation

import com.monkeydp.tools.ext.kotlin.hasAnnot
import javax.validation.metadata.ConstraintDescriptor

/**
 * @author iPotato-Work
 * @date 2020/6/12
 */
val ConstraintDescriptor<*>.isCarrierCstr
    get() = annotation.annotationClass.hasAnnot<CarrierConstraint>()

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

fun ConstraintDescriptor<*>.buildMsgTmpl(carrierCstr: ConstraintDescriptor<*>) =
        "${carrierCstr.messageTemplate}{${annotation.annotationClass.simpleName!!}}"