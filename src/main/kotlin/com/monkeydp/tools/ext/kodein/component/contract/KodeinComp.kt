package com.monkeydp.tools.ext.kodein.component.contract

import com.monkeydp.tools.ext.kodein.component.KodeinComponent
import com.monkeydp.tools.ext.kotlin.findAnnot

/**
 * @author iPotato
 * @date 2019/12/20
 */
interface KodeinComp {
    /**
     * @see compAnnot The annotation annotated by `compAnnot`
     */
    var annot: Annotation
    
    val compAnnot: KodeinComponent get() = annot.annotationClass.findAnnot()
}