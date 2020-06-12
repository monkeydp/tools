package com.monkeydp.tools.ext.hibernate

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.ext.java.inKClass
import com.monkeydp.tools.ext.javax.validation.CarrierConstraint
import com.monkeydp.tools.ext.javax.validation.NotCarrierConstraintEx
import com.monkeydp.tools.ext.javax.validation.buildMsgTmpl
import com.monkeydp.tools.ext.javax.validation.isCarrierCstr
import com.monkeydp.tools.ext.reflections.defaultScannerList
import com.monkeydp.tools.ext.reflections.getAnnotatedAnnotKClasses
import com.monkeydp.tools.ext.reflections.getAnnotatedFields
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.global.defaultLocale
import org.kodein.di.generic.instance
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import java.util.*
import javax.validation.Validator
import javax.validation.metadata.ConstraintDescriptor

/**
 * @author iPotato-Work
 * @date 2020/6/8
 */
abstract class AbstractValidMessageAssigner(
        private val reflections: Reflections
) {
    constructor(vararg packageNames: String) : this(
            reflections(
                    packageNames = packageNames.toList(),
                    scanners = defaultScannerList.plusElement(FieldAnnotationsScanner())
            )
    )

    class AssignValidMessagesConfig() {
        var ignoreNotCarrierCstrEx = false
    }

    fun assignValidMessages(init: (AssignValidMessagesConfig.() -> AssignValidMessagesConfig)? = null) {
        val config = AssignValidMessagesConfig().apply { init?.invoke(this) }
        val annotKClasses = reflections.getAnnotatedAnnotKClasses<CarrierConstraint>()
        val annotatedFields = annotKClasses.map {
            reflections.getAnnotatedFields(it)
        }.flatten()
        annotatedFields.map { it.inKClass }.toSet().forEach outer@{ kClass ->
            validator.getConstraintsForClass(kClass.java)
                    .constrainedProperties
                    .forEach middle@{ propDesc ->
                        propDesc.constraintDescriptors.forEach { cstrDesc ->
                            if (!cstrDesc.isCarrierCstr) {
                                if (config.ignoreNotCarrierCstrEx) return@forEach
                                throw NotCarrierConstraintEx(cstrDesc, propDesc, kClass)
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