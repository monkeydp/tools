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

fun Validator.getBeanRuleMap(parameters: Set<Parameter>): Map<Parameter, BeanRule> {
    val beanRuleMap = mutableMapOf<Parameter, BeanRule>()
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
                    PropertyRule(propDesc = propDesc, cstrRules = constraints)
                            .run(properties::add)
                }
        clazz.apply {
            BeanRule(
                    kClass = clazz.kotlin,
                    propRules = properties
            ).apply {
                beanRuleMap[param] = this
            }
        }
    }
    return beanRuleMap.toMap()
}

class BeanRule(
        val kClass: KClass<*>,
        val propRules: List<PropertyRule>
) {
    val qualifiedClassname: String = kClass.qualifiedName!!
    val classname: String = kClass.simpleName!!
    override fun toString() = qualifiedClassname
}

class PropertyRule(
        val propDesc: PropertyDescriptor,
        val cstrRules: List<ConstraintRule>
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

fun Validator.getSimpleBeanRuleMap(parameters: Set<Parameter>): Map<Parameter, SimpleBeanRule> =
        getBeanRuleMap(parameters)
                .map { it.key to SimpleBeanRule(it.value) }
                .toMap()

class SimpleBeanRule(
        val fullClassname: String,
        val classname: String,
        val propRules: List<SimplePropertyRule>
) {
    constructor(rule: BeanRule)
            : this(rule.qualifiedClassname, rule.classname, rule.propRules.map { SimplePropertyRule(it) })

    override fun toString() = fullClassname
}

class SimplePropertyRule(
        val propName: String,
        val cstrRules: List<SimpleConstraintRule>
) {
    constructor(rule: PropertyRule)
            : this(rule.propName, rule.cstrRules.map { SimpleConstraintRule(it) })

    override fun toString() = propName
}

class SimpleConstraintRule(
        constraint: Annotation,
        val attrs: Map<String, Any>
) {
    private val annotationClass = constraint.annotationClass
    val fullCstrClassname: String = annotationClass.qualifiedName!!
    val cstrClassname: String = annotationClass.simpleName!!

    constructor(rule: ConstraintRule) : this(rule.constraint, rule.attrs)

    override fun toString() = fullCstrClassname
}