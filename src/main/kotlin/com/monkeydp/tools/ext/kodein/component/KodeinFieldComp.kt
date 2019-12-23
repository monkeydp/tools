package com.monkeydp.tools.ext.kodein.component

import java.lang.reflect.Field

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinFieldComp : KodeinComp {
    /**
     * @see annot The field annotated by `annot`
     */
    val field: Field
    
    /**
     * @see field the value of `field`
     */
    val value: Any
    
    companion object {
        operator fun invoke(
                annot: Annotation,
                field: Field,
                value: Any
        ): KodeinFieldComp = StdKodeinFieldComp(annot, field, value)
    }
}

internal abstract class AbstractKodeinFieldComp(
        annot: Annotation,
        override val field: Field,
        override val value: Any
) : KodeinFieldComp, AbstractKodeinComp(annot)

private class StdKodeinFieldComp(
        annot: Annotation,
        field: Field,
        value: Any
) : AbstractKodeinFieldComp(annot, field, value)