package com.monkeydp.tools.ext.kodein.component

import com.monkeydp.tools.ext.kotlin.findAnnot

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinComp {
    /**
     * @see compAnnot The annotation annotated by `compAnnot`
     */
    val annot: Annotation
    
    val compAnnot: KodeinComponent get() = annot.annotationClass.findAnnot()
}

/**
 * @author iPotato
 * @date 2019/12/20
 */
internal abstract class AbstractKodeinComp(override val annot: Annotation) : KodeinComp {
    override fun toString() = "(annot = $annot)"
}