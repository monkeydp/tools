package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.constant.Symbol.HYPHEN
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.camelCase2Chain
import com.monkeydp.tools.ext.kotlin.linesln
import com.monkeydp.tools.ext.kotlin.wrappedInCurlyBraces
import com.monkeydp.tools.ext.reflections.getAnnotatedKClasses
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.global.locale
import org.kodein.di.generic.instance
import org.reflections.Reflections
import java.util.*
import javax.validation.Validator
import javax.validation.metadata.ConstraintDescriptor
import javax.validation.metadata.PropertyDescriptor
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/8
 */
abstract class AbstractValidMessageAssigner(
        private val reflections: Reflections
) {
    constructor(packageName: String) : this(reflections(packageName))

    fun assignValidMessages() {
        val autoValidMessageKClasses = reflections.getAnnotatedKClasses(AutoValidMessage::class)
        autoValidMessageKClasses.forEach outer@{ kClass ->
            validator.getConstraintsForClass(kClass.java)
                    .constrainedProperties
                    .forEach { propDesc ->
                        propDesc.constraintDescriptors.forEach { cstrDesc ->
                            val cstrDescs =
                                    if (cstrDesc.composingConstraints.isEmpty())
                                        setOf(cstrDesc)
                                    else cstrDesc.composingConstraints
                            cstrDescs.forEach {
                                it.changeMessageTemplate(
                                        it.customMessageTemplate(propDesc, kClass)
                                )
                            }
                        }
                    }
        }
    }

    protected abstract fun ConstraintDescriptor<*>.changeMessageTemplate(messageTemplate: String)

    protected fun PropertyDescriptor.messageKey(kClass: KClass<*>): String =
            "${kClass.simpleName!!}.$propertyName".stdFormat()

    protected fun PropertyDescriptor.getFieldReplacement(kClass: KClass<*>): String =
            messageKey(kClass).let {
                resourceBundlesWrapper.getResourceBundle().run {
                    val (objName, propName) = it.split(".")
                    when {
                        containsKey(it) -> it.wrappedInCurlyBraces()
                        containsKey(objName) && containsKey(propName) ->
                            "${objName.wrappedInCurlyBraces()}${propName.wrappedInCurlyBraces()}"
                        !containsKey(objName) && containsKey(propName) -> propName.wrappedInCurlyBraces()
                        else -> {
                            val list = listOf(it, "$objName & $propName", propName)
                            ierror("Cannot find any following key in `$baseBundleName`: ${list.linesln()}")
                        }
                    }
                }
            }

    private fun ConstraintDescriptor<*>.customMessageTemplate(propDesc: PropertyDescriptor, kClass: KClass<*>) =
            "${annotation.annotationClass.simpleName}.${propDesc.messageKey(kClass)}"
                    .wrappedInCurlyBraces()

    private fun String.stdFormat() =
            camelCase2Chain(HYPHEN).toLowerCase()
}

private val validator: Validator by lazy {
    val validator by kodein.instance<Validator>()
    validator
}

private val resourceBundlesWrapper by lazy {
    val resourceBundlesWrapper by kodein.instance<ValidationResourceBundlesWrapper>()
    resourceBundlesWrapper
}


interface ValidationResourceBundlesWrapper {
    val resourceBundleMap: Map<Locale, ResourceBundle>

    fun getResourceBundle() =
            getResourceBundle(locale)

    fun getResourceBundle(locale: Locale) =
            resourceBundleMap.getValue(locale)

    companion object {
        operator fun invoke(resourceBundleMap: Map<Locale, ResourceBundle>): ValidationResourceBundlesWrapper =
                StdValidationResourceBundlesWrapper(resourceBundleMap)

        operator fun invoke(vararg pairs: Pair<Locale, ResourceBundle>): ValidationResourceBundlesWrapper =
                invoke(pairs.toMap())
    }
}

private class StdValidationResourceBundlesWrapper(
        override val resourceBundleMap: Map<Locale, ResourceBundle>
) : ValidationResourceBundlesWrapper