package com.monkeydp.tools.ext.javax.validation

import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS

/**
 * @author iPotato-Work
 * @date 2020/6/12
 */
@Target(ANNOTATION_CLASS)
annotation class CarrierConstraint(
        /**
         * Message before constraint message
         * For example:
         *      value = "{user.name}"
         *      cstrMsg = "must not be blank"
         *      fullMessage = "{user.name} must not be blank"
         */
        val value: String = ""
)