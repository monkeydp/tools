package com.monkeydp.tools.ext.javax.validation

import com.monkeydp.tools.exception.inner.InnerException
import javax.validation.metadata.ConstraintDescriptor
import javax.validation.metadata.PropertyDescriptor
import kotlin.reflect.KClass

/**
 * @author iPotato-Work
 * @date 2020/6/12
 */
class NotCarrierConstraintEx(
        val cstrDesc: ConstraintDescriptor<*>,
        val propDesc: PropertyDescriptor,
        val kClass: KClass<*>
) : InnerException("${cstrDesc.annotation.annotationClass.qualifiedName!!} is not carrier constraint descriptor! Annotated on \n class: $kClass \n property: ${propDesc.propertyName}")