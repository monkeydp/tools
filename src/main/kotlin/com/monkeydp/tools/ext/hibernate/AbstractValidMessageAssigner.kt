package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.constant.Symbol.HYPHEN
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.java.getStringOrNullX
import com.monkeydp.tools.ext.kotlin.camelCase2Chain
import com.monkeydp.tools.ext.reflections.getAnnotatedKClasses
import com.monkeydp.tools.ext.reflections.reflections
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
        private val reflections: Reflections,
        private val fieldPlaceholder: String = DEFAULT_FIELD_PLACEHOLDER
) {
    constructor(packageName: String) : this(reflections(packageName))

    companion object {
        private const val DEFAULT_FIELD_PLACEHOLDER = "{field}"
    }

    fun assignValidMessages() {
        val autoValidMessageKClasses = reflections.getAnnotatedKClasses(AutoValidMessage::class)
        autoValidMessageKClasses.forEach outer@{ kClass ->
            validator.getConstraintsForClass(kClass.java)
                    .constrainedProperties
                    .forEach { propDesc ->
                        propDesc.constraintDescriptors.forEach { cstrDesc ->
                            if (cstrDesc.composingConstraints.isEmpty())
                                cstrDesc.changeMessageTemplate(propDesc, kClass)
                            else cstrDesc.composingConstraints.forEach {
                                it.changeMessageTemplate(propDesc, kClass)
                            }
                        }
                    }
        }
    }

    protected abstract fun ConstraintDescriptor<*>.changeMessageTemplate(propDesc: PropertyDescriptor, kClass: KClass<*>)

    protected fun PropertyDescriptor.getFieldReplacement(kClass: KClass<*>) =
            "${kClass.simpleName!!}.$propertyName".stdFormat()

    protected fun customMessageTemplate(desc: ConstraintDescriptor<*>, fieldReplacement: String): String =
            desc.messageTemplate.run {
//            if (it.matches("^{.*\\\\.Size.message}\\\$".toRegex())) return
                val annot = desc.annotation
                val annotKClass = annot.annotationClass
                val msgTmplKey = annotKClass.simpleName!!.stdFormat()
                val messageTemplate =
                        vpWrapper.resourceBundle.getStringOrNullX(msgTmplKey)
                ResourceBundle.clearCache()
                if (messageTemplate == null)
                    ierror("You must add message template for $annotKClass, like $msgTmplKey=?")
                messageTemplate.replace(fieldPlaceholder, "{$fieldReplacement}")
            }

    private fun String.stdFormat() =
            camelCase2Chain(HYPHEN).toLowerCase()
}

private val validator: Validator by lazy {
    val validator by kodein.instance<Validator>()
    validator
}

private val vpWrapper by lazy {
    val vpWrapper by kodein.instance<ValidationResourceBundleWrapper>()
    vpWrapper
}


interface ValidationResourceBundleWrapper {
    val resourceBundle: ResourceBundle

    companion object {
        operator fun invoke(resourceBundle: ResourceBundle): ValidationResourceBundleWrapper =
                StdValidationResourceBundleWrapper(resourceBundle)
    }
}

private class StdValidationResourceBundleWrapper(
        override val resourceBundle: ResourceBundle
) : ValidationResourceBundleWrapper