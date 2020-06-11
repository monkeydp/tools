package com.monkeydp.tools.ext.javax.validation

import javax.validation.metadata.PropertyDescriptor

/**
 * @author iPotato-Work
 * @date 2020/6/11
 */
val PropertyDescriptor.constraintDescriptorsNeedValid
    get() =
        constraintDescriptors.map {
            if (it.composingConstraints.isEmpty()) setOf(it)
            else it.composingConstraints
        }.flatten()