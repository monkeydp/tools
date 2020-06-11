package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.getAnnotatedField
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
                        propDesc.constraintDescriptors.forEach { cstrDesc ->
                            if (cstrDesc.composingConstraints.isEmpty())
                                cstrDesc.changeMessageTemplate(
                                        MsgTmplStruct(
                                                cstrClassname = cstrDesc.annotation.annotationClass.simpleName!!,
                                                classname = kClass.simpleName!!.toStdFormat(),
                                                propName = propDesc.propertyName.toStdFormat()
                                        ).run(::buildMessageTemplate)
                                )
                            else {
                                val annotationClass = cstrDesc.annotation.annotationClass
                                val enclosingClass = annotationClass.java.enclosingClass
                                val classname = enclosingClass.simpleName.toStdFormat()
                                val propName = enclosingClass.kotlin
                                        .getAnnotatedField(annotationClass)
                                        .name.toStdFormat()
                                cstrDesc.composingConstraints.forEach {
                                    it.changeMessageTemplate(
                                            MsgTmplStruct(
                                                    cstrClassname = it.annotation.annotationClass.simpleName!!,
                                                    classname = classname,
                                                    propName = propName
                                            ).run(::buildMessageTemplate)
                                    )
                                }
                            }
                        }
                    }
        }
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