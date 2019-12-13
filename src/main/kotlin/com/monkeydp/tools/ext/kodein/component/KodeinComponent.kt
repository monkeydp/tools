package com.monkeydp.tools.ext.kodein.component

import com.monkeydp.tools.ext.kodein.component.KodeinComponent.RegisterItem.COMPONENT
import com.monkeydp.tools.ext.kodein.component.KodeinComponent.Type.SINGLETON
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/10
 */
@Target(ANNOTATION_CLASS)
annotation class KodeinComponent<T : Any>(
        val type: Type = SINGLETON,
        val registerItems: Array<RegisterItem> = [COMPONENT],
        val mapGeneratorKClass: KClass<out KodeinMapGenerator<out Any, T>> = Nothing::class
) {
    enum class Type {
        SINGLETON,
        K_CLASS,
    }
    
    enum class RegisterItem {
        COMPONENT,
        LIST,
        SET,
        MAP,
    }
}