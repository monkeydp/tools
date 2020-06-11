package com.monkeydp.tools.ext.javax.validation

import java.lang.reflect.Parameter
import javax.validation.Validator
import javax.validation.metadata.ConstraintDescriptor

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

fun Validator.getValidObjects(parameters: Iterable<Parameter>): List<ValidObject> {
    val validObjs = mutableListOf<ValidObject>()
    parameters.forEach { param ->
        val clazz = param.parameterizedType as Class<*>
        val properties = mutableListOf<ValidProperty>()
        getConstraintsForClass(clazz)
                .constrainedProperties
                .forEach { propDesc ->
                    val constraints = mutableListOf<ValidConstraint>()
                    propDesc.constraintDescriptorsNeedValid.forEach { cstrDesc ->
                        cstrDesc.annotation.annotationClass.apply {
                            ValidConstraint(
                                    name = qualifiedName!!,
                                    simpleName = simpleName!!,
                                    attrs = getExposedAttrMap(cstrDesc)
                            ).run(constraints::add)
                        }
                    }
                    ValidProperty(name = propDesc.propertyName, constraints = constraints)
                            .run(properties::add)
                }
        clazz.apply {
            ValidObject(
                    name = name,
                    simpleName = simpleName,
                    properties = properties
            ).run(validObjs::add)
        }
    }
    return validObjs.toList()
}

class ValidObject(
        val name: String,
        val simpleName: String,
        val properties: List<ValidProperty>
) {
    override fun toString() = name
}

class ValidProperty(
        val name: String,
        val constraints: List<ValidConstraint>
) {
    override fun toString() = name
}

class ValidConstraint(
        val name: String,
        val simpleName: String,
        val attrs: Map<String, Any>
) {
    override fun toString() = name
}