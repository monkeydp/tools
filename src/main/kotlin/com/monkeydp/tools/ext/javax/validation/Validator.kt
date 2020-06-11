package com.monkeydp.tools.ext.javax.validation

import java.lang.reflect.Parameter
import javax.validation.Validator
import javax.validation.metadata.ConstraintDescriptor
import javax.validation.metadata.PropertyDescriptor
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/11
 */
private val internalAnnotationAttributes: Set<String> =
        setOf("message", "groups", "payload")

fun Validator.getExposedAttrMap(
        descriptor: ConstraintDescriptor<*>
) = java.util.TreeMap<String, Any>().apply {
    descriptor.attributes.forEach { (attributeName: String, attributeValue: Any) ->
        if (!internalAnnotationAttributes.contains(attributeName)) {
            this[attributeName] = attributeValue
        }
    }
}

fun Validator.getBeanRules(parameters: Iterable<Parameter>): List<BeanRule> {
    val beanRules = mutableListOf<BeanRule>()
    parameters.forEach { param ->
        val clazz = param.parameterizedType as Class<*>
        val properties = mutableListOf<PropertyRule>()
        getConstraintsForClass(clazz)
                .constrainedProperties
                .forEach { propDesc ->
                    val constraints = mutableListOf<ConstraintRule>()
                    propDesc.constraintDescriptorsNeedValid.forEach { cstrDesc ->
                        ConstraintRule(
                                cstrDesc = cstrDesc,
                                attrs = getExposedAttrMap(cstrDesc)
                        ).run(constraints::add)
                    }
                    PropertyRule(propDesc = propDesc, constraintRules = constraints)
                            .run(properties::add)
                }
        clazz.apply {
            BeanRule(
                    kClass = clazz.kotlin,
                    propertyRules = properties
            ).run(beanRules::add)
        }
    }
    return beanRules.toList()
}

class BeanRule(
        val kClass: KClass<*>,
        val propertyRules: List<PropertyRule>
) {
    val qualifiedClassname: String = kClass.qualifiedName!!
    val classname: String = kClass.simpleName!!
    override fun toString() = qualifiedClassname
}

class PropertyRule(
        val propDesc: PropertyDescriptor,
        val constraintRules: List<ConstraintRule>
) {
    val propName = propDesc.propertyName
    override fun toString() = propName
}

class ConstraintRule(
        val cstrDesc: ConstraintDescriptor<*>,
        val attrs: Map<String, Any>
) {
    val constraint: Annotation = cstrDesc.annotation
    val annotationClass = constraint.annotationClass
    val qualifiedCstrName: String = annotationClass.qualifiedName!!
    val cstrName: String = annotationClass.simpleName!!

    override fun toString() = qualifiedCstrName
}