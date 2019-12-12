package com.monkeydp.tools.kodein

import com.monkeydp.tools.kodein.KodeinComponent.Type.SINGLETON
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS

/**
 * @author iPotato
 * @date 2019/12/10
 */
@Target(ANNOTATION_CLASS)
annotation class KodeinComponent(val type: Type = SINGLETON) {
    enum class Type {
        SINGLETON,
        K_CLASS
    }
}