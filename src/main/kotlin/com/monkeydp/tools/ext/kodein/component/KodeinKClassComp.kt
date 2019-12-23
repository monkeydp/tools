package com.monkeydp.tools.ext.kodein.component

import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinKClassComp : KodeinComp {
    /**
     * @see annot The kClass annotated by `annot`
     */
    val annotatedKClass: KClass<*>
    
    companion object {
        operator fun invoke(
                annot: Annotation,
                annotatedKClass: KClass<*>
        ): KodeinKClassComp = StdKodeinKClassComp(annot, annotatedKClass)
    }
}

internal abstract class AbstractKodeinKClassComp(
        annot: Annotation,
        override val annotatedKClass: KClass<*>
) : KodeinKClassComp, AbstractKodeinComp(annot)

private class StdKodeinKClassComp(
        annot: Annotation,
        annotatedKClass: KClass<*>
) : AbstractKodeinKClassComp(annot, annotatedKClass)