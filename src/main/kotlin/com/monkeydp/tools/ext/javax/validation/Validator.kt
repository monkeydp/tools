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
fun Validator.getBeanRuleMap(parameters: Iterable<Parameter>): Map<Parameter, BeanRule> =
        mutableMapOf<Parameter, BeanRule>().also { beanRuleMap ->
            parameters.forEach { param ->
                val clazz = param.parameterizedType as Class<*>
                val properties = mutableListOf<PropertyRule>()
                getConstraintsForClass(clazz)
                        .constrainedProperties
                        .forEach {
                            it.getPropertyRule(clazz)
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
        }.toMap()


private fun PropertyDescriptor.getPropertyRule(clazz: Class<*>): PropertyRule {
    val constraints = mutableListOf<ConstraintRule>()
    constraintDescriptors.forEach { cstrDesc ->
        when {
            !cstrDesc.isCarrier ->
                throw NotCarrierConstraintDescriptorEx(cstrDesc, this, clazz.kotlin)
            else -> cstrDesc.composingConstraints
                    .forEach {
                        it.getCstrRule(cstrDesc)
                                .run(constraints::add)
                    }
        }
    }
    return PropertyRule(propDesc = this, cstrRules = constraints)
}

private fun ConstraintDescriptor<*>.getCstrRule(carrier: ConstraintDescriptor<*>) =
        ConstraintRule(
                cstrDesc = this,
                cstrAttrs = getExposedAttrMap(),
                msgTmpl = buildMsgTmpl(carrier)
        )

class BeanRule(
        val kClass: KClass<*>,
        val propRules: List<PropertyRule>
) {
    val fullClassname: String = kClass.qualifiedName!!
    val classname: String = kClass.simpleName!!
    override fun toString() = fullClassname
}

class PropertyRule(
        val propDesc: PropertyDescriptor,
        val cstrRules: List<ConstraintRule> = emptyList()
) {
    val propName = propDesc.propertyName
    override fun toString() = propName
}

open class ConstraintRule(
        val cstrDesc: ConstraintDescriptor<*>,
        val cstrAttrs: Map<String, Any>,
        val msgTmpl: String
) {
    val constraint: Annotation = cstrDesc.annotation
    val annotationClass = constraint.annotationClass
    val cstrFullClassname: String = annotationClass.qualifiedName!!
    val cstrClassname: String = annotationClass.simpleName!!

    override fun toString() = cstrFullClassname
}

fun Validator.getSimpleBeanRuleMap(parameters: Iterable<Parameter>): Map<Parameter, SimpleBeanRule> =
        getBeanRuleMap(parameters)
                .map { it.key to SimpleBeanRule(it.value) }
                .toMap()

class SimpleBeanRule(
        val fullClassname: String,
        val classname: String,
        val propRules: List<SimplePropertyRule>
) {
    constructor(rule: BeanRule)
            : this(rule.fullClassname, rule.classname, rule.propRules.map { SimplePropertyRule(it) })

    override fun toString() = fullClassname
}

class SimplePropertyRule(
        val propName: String,
        val cstrRules: List<SimpleConstraintRule>
) {
    constructor(rule: PropertyRule) : this(
            propName = rule.propName,
            cstrRules = rule.cstrRules.map { SimpleConstraintRule(it) }
    )

    override fun toString() = propName
}

class SimpleConstraintRule(
        constraint: Annotation,
        val cstrAttrs: Map<String, Any>,
        val msgTmpl: String
) {
    private val annotationClass = constraint.annotationClass
    val cstrClassname: String = annotationClass.simpleName!!

    constructor(rule: ConstraintRule) : this(rule.constraint, rule.cstrAttrs, rule.msgTmpl)

    override fun toString() = annotationClass.qualifiedName!!
}