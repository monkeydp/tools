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
            beanRuleMap.assignPropRuleRef()
        }.toMap()

private fun MutableMap<Parameter, BeanRule>.assignPropRuleRef() {
    forEach { param, beanRule ->
        val clazz = param.parameterizedType as Class<*>
        beanRule.propRules.forEach outer@{ propRule ->
            propRule.propDesc.constraintDescriptors
                    .filter { it.isRef(clazz) }
                    .forEach { cstrDesc ->
                        values.single { it.fullClassname == cstrDesc.refKClass.qualifiedName }
                                .also { beanRule ->
                                    beanRule.propRules.single { it.propName == cstrDesc.refField.name }
                                            .let {
                                                PropertyRuleRef(
                                                        kClass = beanRule.kClass,
                                                        propRule = it
                                                )
                                            }
                                            .run(propRule::addRef)
                                }
                    }
        }
    }
}

private fun PropertyDescriptor.getPropertyRule(clazz: Class<*>): PropertyRule {
    val constraints = mutableListOf<ConstraintRule>()
    constraintDescriptors.forEach { cstrDesc ->
        when {
            !cstrDesc.isComposing ->
                cstrDesc.getCstrRule()
                        .run(constraints::add)
            cstrDesc.isRef(clazz) -> return@forEach
            else -> cstrDesc.composingConstraints
                    .forEach {
                        it.getCstrRule()
                                .run(constraints::add)
                    }
        }
    }
    return PropertyRule(propDesc = this, cstrRules = constraints)
}

private fun ConstraintDescriptor<*>.getCstrRule() =
        ConstraintRule(
                cstrDesc = this,
                cstrAttrs = getExposedAttrMap()
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

    private val _refs = mutableListOf<PropertyRuleRef>()
    val refs: List<PropertyRuleRef> get() = _refs.toList()

    fun addRef(ref: PropertyRuleRef) {
        _refs.add(ref)
    }
}

class PropertyRuleRef(
        val kClass: KClass<*>,
        val propRule: PropertyRule
)

open class ConstraintRule(
        val cstrDesc: ConstraintDescriptor<*>,
        val cstrAttrs: Map<String, Any>
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
        val cstrRules: List<SimpleConstraintRule>,
        val refs: List<SimplePropertyRuleRef>
) {
    constructor(rule: PropertyRule) : this(
            propName = rule.propName,
            cstrRules = rule.cstrRules.map { SimpleConstraintRule(it) },
            refs = rule.refs.map { SimplePropertyRuleRef(it) }
    )

    override fun toString() = propName
}

class SimplePropertyRuleRef(
        val fullClassname: String,
        val propName: String
) {
    constructor(ref: PropertyRuleRef) : this(
            fullClassname = ref.kClass.qualifiedName!!,
            propName = ref.propRule.propName
    )
}

class SimpleConstraintRule(
        constraint: Annotation,
        val cstrAttrs: Map<String, Any>
) {
    private val annotationClass = constraint.annotationClass
    val cstrFullClassname: String = annotationClass.qualifiedName!!
    val cstrClassname: String = annotationClass.simpleName!!

    constructor(rule: ConstraintRule) : this(rule.constraint, rule.cstrAttrs)

    override fun toString() = cstrFullClassname
}