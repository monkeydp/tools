package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.javax.validation.constraintDescriptorsNeedValid
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

    fun assignValidMessages() {
        val autoValidMessageKClasses = reflections.getAnnotatedKClasses(AutoValidMessage::class)
        autoValidMessageKClasses.forEach outer@{ kClass ->
            validator.getConstraintsForClass(kClass.java)
                    .constrainedProperties
                    .forEach { propDesc ->
                        propDesc.constraintDescriptorsNeedValid.forEach {
                            it.changeMessageTemplate(
                                    it.customMsgTmplStruct(propDesc, kClass)
                                            .run(::buildMessageTemplate)
                            )
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

    private fun buildMessageTemplate(msgTmplStruct: MsgTmplStruct): String =
            msgTmplStruct.let {
                val str = resourceBundlesWrapper.getResourceBundle().run {
                    val (cstrName, objName, propName) = it
                    when {
                        containsKey(it.combinedKey) -> it.combinedKey.wrappedInCurlyBraces()
                        !containsKey(cstrName) -> {
                            val list = listOf(it.combinedKey, cstrName)
                            ierror("Cannot find any following key in `$baseBundleName`: ${list.linesln()}")
                        }
                        containsKey(it.combinedKeyWithoutCstr) ->
                            "{$objName.$propName}{$cstrName}"
                        containsKey(objName) && containsKey(propName) ->
                            "{$objName}{$propName}{$cstrName}"
                        !containsKey(objName) && containsKey(propName) ->
                            "{$propName}{$cstrName}"
                        else -> {
                            val list = listOf(it.combinedKey, "$objName & $propName", propName)
                            ierror("Cannot find any following key in `$baseBundleName`: ${list.linesln()}")
                        }
                    }
                }
                ResourceBundle.clearCache()
                str
            }

    private fun ConstraintDescriptor<*>.customMsgTmplStruct(propDesc: PropertyDescriptor, kClass: KClass<*>) =
            MsgTmplStruct(
                    constraintName = annotation.annotationClass.simpleName!!,
                    objectName = kClass.simpleName!!.stdFormat(),
                    propertyName = propDesc.propertyName.stdFormat()
            )

    protected fun String.stdFormat() =
            snakeToLowerCamel()
}

private class MsgTmplStruct(
        val constraintName: String,
        val objectName: String,
        val propertyName: String
) {
    val combinedKeyWithoutCstr: String =
            "$objectName.$propertyName"

    val combinedKey: String =
            "$constraintName.$combinedKeyWithoutCstr"

    operator fun component1() = constraintName
    operator fun component2() = objectName
    operator fun component3() = propertyName
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