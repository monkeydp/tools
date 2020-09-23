package com.monkeydp.tools.ext.javax.validation

import com.monkeydp.tools.ext.kotlin.singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.properties.Delegates
import kotlin.reflect.KClass

/**
 * The annotated element size must be the specified size.
 * <p>
 * Supported types are:
 * <ul>
 *     <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * </ul>
 *
 * @author iPotato-Work
 * @date 2020/9/23
 */
@Target(FIELD, ANNOTATION_CLASS)
@Constraint(validatedBy = [FixedValidatorForCharSequence::class])
annotation class Fixed(
        /**
         * size
         */
        val value: Int,
        val message: String = "{Fixed}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class FixedValidatorForCharSequence : ConstraintValidator<Fixed, CharSequence> {

    private var size: Int by Delegates.singleton()

    override fun initialize(constraintAnnotation: Fixed) {
        size = constraintAnnotation.value
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext?) =
            if (value == null) true
            else value.length == size
}
