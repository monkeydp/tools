package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.javax.validation.NotCarrierConstraintDescriptorEx
import com.monkeydp.tools.ext.javax.validation.buildMsgTmpl
import com.monkeydp.tools.ext.javax.validation.isCarrier
import com.monkeydp.tools.ext.kotlin.linesln
import com.monkeydp.tools.ext.kotlin.snakeToLowerCamel
import com.monkeydp.tools.ext.kotlin.wrappedInCurlyBraces
import com.monkeydp.tools.ext.reflections.getAnnotatedKClasses
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.global.defaultLocale
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

    class AssignValidMessagesConfig() {
        var ignoreNotCarrierCstrEx = false
    }

    fun assignValidMessages(init: (AssignValidMessagesConfig.() -> AssignValidMessagesConfig)? = null) {
        val config = AssignValidMessagesConfig().apply { init?.invoke(this) }
        val autoValidMessageKClasses = reflections.getAnnotatedKClasses(AutoValidMessage::class)
        autoValidMessageKClasses.forEach outer@{ kClass ->
            validator.getConstraintsForClass(kClass.java)
                    .constrainedProperties
                    .forEach middle@{ propDesc ->
                        propDesc.constraintDescriptors.forEach { cstrDesc ->
                            if (!cstrDesc.isCarrier) {
                                if (config.ignoreNotCarrierCstrEx) return@forEach
                                throw NotCarrierConstraintDescriptorEx(cstrDesc, propDesc, kClass)
                            }
                            cstrDesc.composingConstraints.forEach {
                                it.changeMessageTemplate(
                                        it.buildMsgTmpl(cstrDesc)
                                )
                            }
                        }
                    }
        }
        ResourceBundle.clearCache()
    }

    protected abstract fun ConstraintDescriptor<*>.changeMessageTemplate(messageTemplate: String)

    protected fun PropertyDescriptor.messageKey(kClass: KClass<*>): String =
            "${kClass.simpleName!!}.$propertyName".toStdFormat()

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


    protected fun String.toStdFormat() =
            snakeToLowerCamel()
}

private class MsgTmplStruct(
        val cstrClassname: String,
        val classname: String,
        val propName: String
) {
    val combinedKeyWithoutCstr: String =
            "$classname.$propName"

    val combinedKey: String =
            "$cstrClassname.$combinedKeyWithoutCstr"

    operator fun component1() = cstrClassname
    operator fun component2() = classname
    operator fun component3() = propName
}

private val validator: Validator by lazy {
    val validator by kodein.instance<Validator>()
    validator
}

private val resourceBundlesWrapper by lazy {
    val resourceBundlesWrapper by kodein.instance<ValidResourceBundleMapWrapper>()
    resourceBundlesWrapper
}


interface ValidResourceBundleMapWrapper {
    val resourceBundleMap: Map<Locale, ResourceBundle>

    fun getResourceBundle() =
            getResourceBundle(defaultLocale)

    fun getResourceBundle(locale: Locale) =
            resourceBundleMap.getValue(locale)

    companion object {
        operator fun invoke(resourceBundleMap: Map<Locale, ResourceBundle>): ValidResourceBundleMapWrapper =
                StdValidResourceBundleMapWrapper(resourceBundleMap)

        operator fun invoke(vararg pairs: Pair<Locale, ResourceBundle>): ValidResourceBundleMapWrapper =
                invoke(pairs.toMap())
    }
}

private class StdValidResourceBundleMapWrapper(
        override val resourceBundleMap: Map<Locale, ResourceBundle>
) : ValidResourceBundleMapWrapper